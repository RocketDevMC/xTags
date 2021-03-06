package xyz.invisraidinq.tags;

import com.samjakob.spigui.SpiGUI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.invisraidinq.tags.database.IDatabase;
import xyz.invisraidinq.tags.database.impl.FlatFileImpl;
import xyz.invisraidinq.tags.database.impl.MongoDBImpl;
import xyz.invisraidinq.tags.database.impl.MySQLImpl;
import xyz.invisraidinq.tags.license.LicenseChecker;
import xyz.invisraidinq.tags.profile.ProfileManager;
import xyz.invisraidinq.tags.profile.listeners.ProfileJoinListener;
import xyz.invisraidinq.tags.profile.listeners.ProfileQuitListener;
import xyz.invisraidinq.tags.tags.TagManager;
import xyz.invisraidinq.tags.tags.commands.TagCommand;
import xyz.invisraidinq.tags.tags.commands.xtag.XTagCommand;
import xyz.invisraidinq.tags.tags.listeners.TagChatListener;
import xyz.invisraidinq.tags.utils.CC;
import xyz.invisraidinq.tags.utils.ConfigFile;
import xyz.invisraidinq.tags.utils.command.CommandRegister;

import java.util.Arrays;

public class TagsPlugin extends JavaPlugin {

    private ConfigFile settingsFile;
    private ConfigFile langFile;
    private ConfigFile tagsFile;
    private IDatabase databaseType;
    private TagManager tagManager;
    private ProfileManager profileManager;
    private SpiGUI spiGUI;

    @Override
    public void onEnable() {
        CC.out("Enabling plugin...");

        long start = System.currentTimeMillis();

        this.settingsFile = new ConfigFile(this, "settings.yml");

        if (!new LicenseChecker().checkLicenseValidity(this)) {
            return;
        }

        this.langFile = new ConfigFile(this, "lang.yml");
        this.tagsFile = new ConfigFile(this, "tags.yml");

        this.tagManager = new TagManager(this, this.langFile);
        this.profileManager = new ProfileManager(this);

        this.setupDatabase();

        this.spiGUI = new SpiGUI(this);

        CommandRegister commandRegister = new CommandRegister();
        commandRegister.registerCommand(new TagCommand(this), this, false);
        commandRegister.registerCommand(new XTagCommand(this), this, true);

        Arrays.asList(
                new ProfileJoinListener(this),
                new ProfileQuitListener(this)
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));

        if (this.settingsFile.getBoolean("HOOK-CHAT")) {
            Bukkit.getPluginManager().registerEvents(new TagChatListener(this), this);
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderHandler(this.profileManager);
        }

        CC.out("Enabled plugin in " + (System.currentTimeMillis() - start) + "ms");
    }

    @Override
    public void onDisable() {
        this.getDatabaseType().saveTags();
        Bukkit.getOnlinePlayers().forEach(player -> this.getDatabaseType().saveProfile(player));
        this.getDatabaseType().closeDatabaseConnection();
    }

    private void setupDatabase() {
        String selectedDatabase = this.settingsFile.getString("DATABASE.STORAGE-TYPE");
        switch (selectedDatabase.toUpperCase()) {
            case "FLATFILE":
                CC.out("Detected and setting up storage type " + selectedDatabase);
                this.databaseType = new FlatFileImpl(this, this.tagsFile);
                CC.out("Successfully setup storage type " + selectedDatabase);
                break;
            case "MONGO":
                CC.out("Detected and setting up storage type " + selectedDatabase);
                this.databaseType = new MongoDBImpl(this);
                CC.out("Successfully setup storage type " + selectedDatabase);
                break;
            case "MYSQL":
                CC.out("Detected and setting up storage type " + selectedDatabase);
                this.databaseType = new MySQLImpl(this);
                CC.out("Successfully setup storage type " + selectedDatabase);
                break;
            default:
                CC.out("ERROR: No valid storage type set in settings.yml");
                break;
        }
    }

    public ConfigFile getSettingsFile() {
        return this.settingsFile;
    }

    public ConfigFile getLangFile() {
        return this.langFile;
    }

    public ConfigFile getTagsFile() {
        return this.tagsFile;
    }

    public IDatabase getDatabaseType() {
        return this.databaseType;
    }

    public TagManager getTagManager() {
        return this.tagManager;
    }

    public ProfileManager getProfileManager() {
        return this.profileManager;
    }

    public SpiGUI getMenuAPI() {
        return this.spiGUI;
    }
}
