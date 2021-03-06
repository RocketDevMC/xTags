package xyz.invisraidinq.tags.tags.commands.xtag;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import xyz.invisraidinq.tags.TagsPlugin;
import xyz.invisraidinq.tags.utils.CC;
import xyz.invisraidinq.tags.utils.command.CommandArgument;

public class XTagReloadArgument extends CommandArgument {

    private final TagsPlugin plugin;

    public XTagReloadArgument(TagsPlugin plugin) {
        super("reload", "Reload the plugin", "rl");

        this.plugin = plugin;
    }

    @Override
    public String getUsage(String label) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.plugin.getLangFile().reload(false);
        sender.sendMessage(CC.GREEN + "Reloaded lang.yml. If you wish to reload anything else, please re-start the server. Note that reloading may cause issues and I ALWAYS advise that you restart");
        return true;
    }
}
