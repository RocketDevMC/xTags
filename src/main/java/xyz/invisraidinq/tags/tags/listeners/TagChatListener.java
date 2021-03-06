package xyz.invisraidinq.tags.tags.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.invisraidinq.tags.TagsPlugin;
import xyz.invisraidinq.tags.profile.TagsProfile;
import xyz.invisraidinq.tags.utils.CC;

public class TagChatListener implements Listener {

    private final TagsPlugin plugin;

    public TagChatListener(TagsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        TagsProfile profile = this.plugin.getProfileManager().getProfileByPlayer(event.getPlayer());

        if (profile.getTag() == null) {
            event.setFormat(event.getFormat()
                    .replace("{XTAGS_TAG}", "")
                    .replace("{XTAGS_TAGNAME}", ""));
        } else {
            event.setFormat(event.getFormat()
                    .replace("{XTAGS_TAG}", CC.colour(profile.getTag().getFormat()))
                    .replace("{XTAGS_TAGNAME}", profile.getTag().getTagName()));
        }
    }
}