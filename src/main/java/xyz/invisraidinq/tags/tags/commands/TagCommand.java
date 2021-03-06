package xyz.invisraidinq.tags.tags.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import xyz.invisraidinq.tags.TagsPlugin;
import xyz.invisraidinq.tags.utils.CC;
import xyz.invisraidinq.tags.utils.command.ExecutableCommand;

public class TagCommand extends ExecutableCommand {

    private final TagsPlugin plugin;

    public TagCommand(TagsPlugin plugin) {
        super("tag", "Base tag command", "prefix", "tags");

        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(CC.colour(this.plugin.getLangFile().getString("GENERAL.INVALID-EXECUTOR")));
            return true;
        }

        Player player = (Player) sender;
        this.plugin.getTagManager().openTagMenu(player);
        return true;
    }
}
