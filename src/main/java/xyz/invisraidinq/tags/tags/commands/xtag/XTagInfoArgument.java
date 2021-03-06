package xyz.invisraidinq.tags.tags.commands.xtag;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import xyz.invisraidinq.tags.TagsPlugin;
import xyz.invisraidinq.tags.tags.Tag;
import xyz.invisraidinq.tags.utils.CC;
import xyz.invisraidinq.tags.utils.command.CommandArgument;

public class XTagInfoArgument extends CommandArgument {

    private final TagsPlugin plugin;

    public XTagInfoArgument(TagsPlugin plugin) {
        super("info", "Check the information of a tag", "information", "about");

        this.plugin = plugin;
    }

    @Override
    public String getUsage(String label) {
        return CC.colour(this.plugin.getLangFile().getString("XTAG.INFO.USAGE"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(this.getUsage(label));
            return true;
        }

        Tag tag = this.plugin.getTagManager().getTagByName(args[1]);
        if (tag == null) {
            sender.sendMessage(this.plugin.getLangFile().getString("XTAG.INFO.TAG-DOES-NOT-EXIST"));
            return true;
        }

        for (String message : this.plugin.getLangFile().getStringList("XTAG.INFO.INFO-MESSAGE")) {
            sender.sendMessage(CC.colour(message)
                    .replace("%tagName%", tag.getTagName())
                    .replace("%tagFormat%", CC.colour(tag.getFormat()))
                    .replace("%tagPermission%", CC.colour(tag.getPermission())));
        }
        return true;
    }
}
