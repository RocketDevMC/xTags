package xyz.invisraidinq.tags.database.impl;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.invisraidinq.tags.TagsPlugin;
import xyz.invisraidinq.tags.database.IDatabase;
import xyz.invisraidinq.tags.profile.TagsProfile;
import xyz.invisraidinq.tags.tags.Tag;
import xyz.invisraidinq.tags.utils.CC;
import xyz.invisraidinq.tags.utils.ConfigFile;

import java.io.File;

public class FlatFileImpl implements IDatabase {

    private final TagsPlugin plugin;
    private final File userDataFolder;
    private ConfigFile tagsFile;

    public FlatFileImpl(TagsPlugin plugin, ConfigFile tagsFile) {
        this.plugin = plugin;
        this.tagsFile = tagsFile;
        this.userDataFolder = new File(plugin.getDataFolder() + File.separator + "users" + File.separator);

        if (!this.userDataFolder.exists()) {
            this.userDataFolder.mkdirs();
        }

        this.loadTags();
    }

    @Override
    public void loadTags() {
        YamlConfiguration tagsYaml = this.tagsFile.getAsYaml();
        for (String tag : tagsYaml.getConfigurationSection("").getKeys(false)) {
            this.plugin.getTagManager().getTagList().add(new Tag(tagsYaml.getString(tag + ".NAME"),
                    tagsYaml.getString(tag + ".FORMAT"), tagsYaml.getString(tag + ".PERMISSION")));
        }
    }

    @Override
    public void saveTags() {
        YamlConfiguration tagsYaml = this.tagsFile.getAsYaml();
        for (Tag tag : this.plugin.getTagManager().getTagList()) {
            String path = tag.getTagName() + ".";
            tagsYaml.set(path + "NAME", tag.getTagName());
            tagsYaml.set(path + "FORMAT", tag.getFormat());
            tagsYaml.set(path + "PERMISSION", tag.getPermission());
            this.tagsFile.save();
        }
    }

    @Override
    public void loadProfile(Player player) {
        this.plugin.getProfileManager().getProfileMap().put(player.getUniqueId(), new TagsProfile(this.plugin, player));

        File dataFile = new File(this.userDataFolder, player.getUniqueId().toString() + ".yml");
        TagsProfile profile = this.plugin.getProfileManager().getProfileByPlayer(player);
        if (dataFile.exists()) {
            YamlConfiguration userData = YamlConfiguration.loadConfiguration(dataFile);
            Tag tag = this.plugin.getTagManager().getTagByName(userData.getString("TAG"));
            if (tag != null) {
                profile.setTag(this.plugin.getTagManager().getTagByName(userData.getString("TAG")));
            } else {
                profile.setTag(null);
            }
        } else {
            profile.setTag(null);
        }

    }

    @Override
    public void saveProfile(Player player) {
        TagsProfile profile = this.plugin.getProfileManager().getProfileByPlayer(player);

        File userFile = new File(this.userDataFolder, profile.getUuid().toString() + ".yml");
        if (!userFile.exists()) {
            try {
                userFile.createNewFile();
            } catch (Exception e) {
                CC.out("Failed to save file for " + profile);
                e.printStackTrace();
            }
        }
        YamlConfiguration userData = YamlConfiguration.loadConfiguration(userFile);
        if (profile.getTag() == null) {
            userData.set("TAG", "null");
        } else {
            userData.set("TAG", profile.getTag().getTagName());
        }
        try {
            userData.save(userFile);
        } catch (Exception e) {
            CC.out("Failed to save userdata file for " + profile);
            e.printStackTrace();
        }
        this.plugin.getProfileManager().getProfileMap().remove(player.getUniqueId());
    }

    @Override
    public void closeDatabaseConnection() {
        this.tagsFile.save();
    }
}
