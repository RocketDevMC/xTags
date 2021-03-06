package xyz.invisraidinq.tags;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.invisraidinq.tags.profile.ProfileManager;
import xyz.invisraidinq.tags.profile.TagsProfile;
import xyz.invisraidinq.tags.utils.CC;

public class PlaceholderHandler extends PlaceholderExpansion {

    private final ProfileManager profileManager;

    public PlaceholderHandler(ProfileManager profileManager) {
        CC.out("Detected PlaceholderAPI, now hooking into the plugin...");
        this.profileManager = profileManager;

        this.register();
        CC.out("Hooked into PlaceholderAPI");
    }

    @Override
    public @NotNull String getIdentifier() {
        return "xtags";
    }

    @Override
    public @NotNull String getAuthor() {
        return "InvisRaidinq";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        TagsProfile profile = this.profileManager.getProfileByPlayer(player);
        if (identifier.equalsIgnoreCase("tag")) {
            if (profile.getTag() == null) {
                return "";
            }
            return profile.getTag().getFormat();
        }

        if (identifier.equalsIgnoreCase("tagName")) {
            if (profile.getTag() == null) {
                return "";
            }
            return profile.getTag().getTagName();
        }
        return "[xTags] Invalid placeholder requested";
    }
}
