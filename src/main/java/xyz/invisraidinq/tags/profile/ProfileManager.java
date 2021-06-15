package xyz.invisraidinq.tags.profile;

import org.bukkit.entity.Player;
import xyz.invisraidinq.tags.TagsPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileManager {

    private final TagsPlugin plugin;

    private final Map<UUID, TagsProfile> profileMap = new HashMap<>();

    public ProfileManager(TagsPlugin plugin) {
        this.plugin = plugin;
    }


    public TagsProfile getProfileByPlayer(Player player) {
        return this.profileMap.get(player.getUniqueId());
    }

    public TagsProfile getProfileByUUID(UUID uuid) {
        return this.profileMap.get(uuid);
    }

    public void loadProfile(Player player) {
        this.plugin.getDatabaseType().loadProfile(player);
    }

    public void saveProfile(Player player) {
        this.plugin.getDatabaseType().saveProfile(player);
    }

    public Map<UUID, TagsProfile> getProfileMap() {
        return this.profileMap;
    }
}
