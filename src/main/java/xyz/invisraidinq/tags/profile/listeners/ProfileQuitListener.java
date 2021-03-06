package xyz.invisraidinq.tags.profile.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.invisraidinq.tags.TagsPlugin;

public class ProfileQuitListener implements Listener {

    private final TagsPlugin plugin;

    public ProfileQuitListener(TagsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onProfileQuit(PlayerQuitEvent event) {
        this.plugin.getProfileManager().saveProfile(event.getPlayer());
    }
}
