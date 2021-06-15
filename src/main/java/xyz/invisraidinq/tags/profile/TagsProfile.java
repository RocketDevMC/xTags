package xyz.invisraidinq.tags.profile;

import org.bukkit.entity.Player;
import xyz.invisraidinq.tags.TagsPlugin;
import xyz.invisraidinq.tags.tags.Tag;

import java.util.UUID;

public class TagsProfile {

    private final TagsPlugin plugin;
    private final Player player;
    private final UUID uuid;
    private Tag tag;

    public TagsProfile(TagsPlugin plugin, Player player) {
        this.plugin = plugin;

        this.player = player;
        this.uuid = player.getUniqueId();
    }

    public Player getPlayer() {
        return this.player;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Tag getTag() {
        return this.tag;
    }

    public void removeTag() {
        this.tag = null;
    }


}
