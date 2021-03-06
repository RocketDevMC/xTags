package xyz.invisraidinq.tags.tags.commands.xtag;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import xyz.invisraidinq.tags.TagsPlugin;
import xyz.invisraidinq.tags.tags.Tag;
import xyz.invisraidinq.tags.utils.CC;
import xyz.invisraidinq.tags.utils.command.CommandArgument;

public class XTagCreateArgument extends CommandArgument implements Listener {

    private final TagsPlugin plugin;

    public XTagCreateArgument(TagsPlugin plugin) {
        super("create", "Create a tag in-game", "new");

        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin); //Register here so it doesn't create two instances and fuck up
    }

    @Override
    public String getUsage(String label) {
        return CC.colour(this.plugin.getLangFile().getString("XTAG.CREATE.USAGE"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(this.getUsage(label));
            return true;
        }

        if (this.plugin.getTagManager().getTagByName(args[1]) != null) {
            sender.sendMessage(CC.colour(this.plugin.getLangFile().getString("XTAG.CREATE.TAG-ALREADY-EXISTS")));
            return true;
        }

        String permission = "xtags.tag." + args[1];
        Tag tag = new Tag(args[1], args[2], permission);
        this.plugin.getTagManager().getTagList().add(tag);
        sender.sendMessage(CC.colour(this.plugin.getLangFile().getString("XTAG.CREATE.CREATED"))
                .replace("%tagName%", tag.getTagName())
                .replace("%format%", CC.colour(tag.getFormat()))
                .replace("%permission%", tag.getPermission()));
        return true;
    }
}
