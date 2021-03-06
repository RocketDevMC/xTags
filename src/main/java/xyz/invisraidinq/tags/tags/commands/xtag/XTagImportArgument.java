package xyz.invisraidinq.tags.tags.commands.xtag;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.invisraidinq.tags.TagsPlugin;
import xyz.invisraidinq.tags.tags.Tag;
import xyz.invisraidinq.tags.utils.CC;
import xyz.invisraidinq.tags.utils.command.CommandArgument;

import java.util.HashSet;
import java.util.Set;

public class XTagImportArgument extends CommandArgument {

    private final TagsPlugin plugin;
    private final Set<CommandSender> confirmationSet = new HashSet<>();

    public XTagImportArgument(TagsPlugin plugin) {
        super("import", "Admin command to import tags from tags.yml");

        this.plugin = plugin;
    }

    @Override
    public String getUsage(String label) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!this.confirmationSet.contains(sender)) {
            sender.sendMessage(CC.colour(this.plugin.getLangFile().getString("XTAG.IMPORT.IMPORT-CONFIRMATION")));
            this.confirmationSet.add(sender);
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> {
                this.confirmationSet.remove(sender);
            }, 600L);
            return true;
        }

        this.plugin.getTagManager().getTagList().clear();
        YamlConfiguration tagsYaml = this.plugin.getTagsFile().getAsYaml();
        for (String tag : tagsYaml.getConfigurationSection("").getKeys(false)) {
            Tag tagObject = new Tag(tagsYaml.getString(tag + ".NAME"),
                    tagsYaml.getString(tag + ".FORMAT"), tagsYaml.getString(tag + ".PERMISSION"));
            this.plugin.getTagManager().getTagList().add(tagObject);
            sender.sendMessage(CC.colour(this.plugin.getLangFile().getString("XTAG.IMPORT.IMPORT-LOADED"))
                .replace("%tagName%", tagObject.getTagName())
                .replace("%format%", CC.colour(tagObject.getFormat()))
                .replace("%permission%", tagObject.getPermission()));
        }
        return true;
    }
}
