package xyz.invisraidinq.tags.profile.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.invisraidinq.tags.TagsPlugin;

public class ProfileJoinListener implements Listener {

    private final TagsPlugin plugin;

    public ProfileJoinListener(TagsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onProfileJoin(PlayerJoinEvent event) {
        this.plugin.getProfileManager().loadProfile(event.getPlayer());
    }
}
