package xyz.invisraidinq.tags.tags.commands.xtag;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.PlayerNamePrompt;
import xyz.invisraidinq.tags.TagsPlugin;
import xyz.invisraidinq.tags.profile.TagsProfile;
import xyz.invisraidinq.tags.tags.Tag;
import xyz.invisraidinq.tags.utils.CC;
import xyz.invisraidinq.tags.utils.command.CommandArgument;

public class XTagDeleteArgument extends CommandArgument {

    private final TagsPlugin plugin;

    public XTagDeleteArgument(TagsPlugin plugin) {
        super("delete", "Delete a tag", "remove");

        this.plugin = plugin;
    }

    @Override
    public String getUsage(String label) {
        return CC.colour(this.plugin.getLangFile().getString("XTAG.DELETE.USAGE"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(this.getUsage(label));
            return true;
        }

        Tag tag = this.plugin.getTagManager().getTagByName(args[1]);
        if (tag == null) {
            sender.sendMessage(CC.colour("XTAG.DELETE.TAG-DOES-NOT-EXIST"));
            return true;
        }

        this.plugin.getTagManager().getTagList().remove(tag);
        sender.sendMessage(CC.colour(this.plugin.getLangFile().getString("XTAG.DELETE.TAG-DELETED")));
        if (this.plugin.getLangFile().getBoolean("XTAG.DELETE.REMOVE-DELETED-TAG-FROM-PLAYERS")) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                TagsProfile profile = this.plugin.getProfileManager().getProfileByPlayer(player);
                if (profile.getTag().equals(tag)) {
                    profile.removeTag();
                }
            });
        }
        return true;
    }
}
