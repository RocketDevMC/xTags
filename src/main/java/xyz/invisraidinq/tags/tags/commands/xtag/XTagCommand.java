package xyz.invisraidinq.tags.tags.commands.xtag;

import xyz.invisraidinq.tags.TagsPlugin;
import xyz.invisraidinq.tags.utils.command.ExecutableCommand;

public class XTagCommand extends ExecutableCommand {

    public XTagCommand(TagsPlugin plugin) {
        super("xtag", "Admin command for xTags", "xtags", "tagsplugin");

        this.setUsage(true);

        this.addArgument(new XTagImportArgument(plugin));
        this.addArgument(new XTagCreateArgument(plugin));
        this.addArgument(new XTagDeleteArgument(plugin));
        this.addArgument(new XTagInfoArgument(plugin));
        this.addArgument(new XTagSetPermissionArgument(plugin));
        this.addArgument(new XTagReloadArgument(plugin));
    }
}
