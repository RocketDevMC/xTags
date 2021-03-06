package xyz.invisraidinq.tags.tags.commands.xtag;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import xyz.invisraidinq.tags.TagsPlugin;
import xyz.invisraidinq.tags.tags.Tag;
import xyz.invisraidinq.tags.utils.CC;
import xyz.invisraidinq.tags.utils.command.CommandArgument;

public class XTagSetPermissionArgument extends CommandArgument {

    private final TagsPlugin plugin;

    public XTagSetPermissionArgument(TagsPlugin plugin) {
        super("setpermission", "Set the permission of a tag", "permissionset");

        this.plugin = plugin;
    }

    @Override
    public String getUsage(String label) {
        return CC.colour(this.plugin.getLangFile().getString("XTAG.SETPERMISSION.USAGE"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(this.getUsage(label));
            return true;
        }

        Tag tag = this.plugin.getTagManager().getTagByName(args[1]);
        String permission = args[2];
        if (tag == null) {
            sender.sendMessage(CC.colour(this.plugin.getLangFile().getString("XTAG.SETPERMISSION.TAG-DOES-NOT-EXIST")));
            return true;
        }

        tag.setPermission(permission);
        sender.sendMessage(CC.colour(this.plugin.getLangFile().getString("XTAG.SETPERMISSION.PERMISSION-UPDATED"))
            .replace("%tagName%", tag.getTagName())
            .replace("%permission%", permission));
        return true;
    }
}
