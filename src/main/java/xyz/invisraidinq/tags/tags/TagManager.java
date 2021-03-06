package xyz.invisraidinq.tags.tags;

import com.samjakob.spigui.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import xyz.invisraidinq.tags.TagsPlugin;
import xyz.invisraidinq.tags.profile.TagsProfile;
import xyz.invisraidinq.tags.utils.CC;
import xyz.invisraidinq.tags.utils.ConfigFile;
import xyz.invisraidinq.tags.utils.ItemFactory;

import java.util.ArrayList;
import java.util.List;

public class TagManager {

    private final TagsPlugin plugin;
    private final ConfigFile langFile;

    private final List<Tag> tagList = new ArrayList<>();

    public TagManager(TagsPlugin plugin, ConfigFile langFile) {
        this.plugin = plugin;
        this.langFile = langFile;

    }

    public List<Tag> getTagList() {
        return this.tagList;
    }

    public Tag getTagByName(String name) {
        for (Tag tag : this.tagList) {
            if (tag.getTagName().equalsIgnoreCase(name)) {
                return tag;
            }
        }
        return null;
    }

    public void openTagMenu(Player player) {
        TagsProfile profile = plugin.getProfileManager().getProfileByPlayer(player);
        final String lockedPath = "TAG.MENU.LOCKED-TAG.";
        final String unlockedPath = "TAG.MENU.UNLOCKED-TAG.";


        SGMenu tagMenu = this.plugin.getMenuAPI().create(this.langFile.getString("TAG.MENU.TITLE"), this.langFile.getInt("TAG.MENU.SIZE"));

        for (Tag tag : this.tagList) {
            if (!tag.hasPermission()) {
                tag.setPermission(("xtags.tag." + tag.getTagName()).toLowerCase());
            }

            ItemFactory factory;
            if (player.hasPermission(tag.getPermission())) {
                factory = new ItemFactory(Material.valueOf(langFile.getString(unlockedPath + "MATERIAL")));

                factory.setName(langFile.getString(unlockedPath + "ITEM-NAME")
                        .replace("%tagName%", tag.getTagName())
                        .replace("%tagFormat%", CC.colour(tag.getFormat()))
                        .replace("%player%", player.getName()));

                for (String lore : langFile.getStringList(unlockedPath + "ITEM-LORE")) {
                    factory.addLoreLine(lore
                            .replace("%tagName%", tag.getTagName())
                            .replace("%tagFormat%", CC.colour(tag.getFormat()))
                            .replace("%player%", player.getName()));
                }

                if (this.langFile.getBoolean(unlockedPath + "ENCHANTED")) {
                    factory.setEnchanted();
                }

            } else {
                factory = new ItemFactory(Material.valueOf(langFile.getString(lockedPath + "MATERIAL")));

                factory.setName(langFile.getString(lockedPath + "ITEM-NAME")
                        .replace("%tagName%", tag.getTagName())
                        .replace("%tagFormat%", CC.colour(tag.getFormat()))
                        .replace("%player%", player.getName()));

                for (String lore : langFile.getStringList(lockedPath + "ITEM-LORE")) {
                    factory.addLoreLine(lore
                            .replace("%tagName%", tag.getTagName())
                            .replace("%tagFormat%", CC.colour(tag.getFormat()))
                            .replace("%player%", player.getName()));
                }

                if (this.langFile.getBoolean(lockedPath + "ENCHANTED")) {
                    factory.setEnchanted();
                }
            }

            SGButton button = new SGButton(factory.build()).withListener((InventoryClickEvent event) -> {
                if (player.hasPermission(tag.getPermission())) {
                    profile.setTag(tag);
                    player.sendMessage(CC.colour(langFile.getString("TAG.TAG-CHANGED")
                            .replace("%tagName%", tag.getTagName())
                            .replace("%tagFormat%", CC.colour(tag.getFormat()))));
                    } else {
                        player.sendMessage(CC.colour(langFile.getString("TAG.TAG-NOT-UNLOCKED")));
                    }
                player.closeInventory();
            });

            tagMenu.addButton(button);
        }

        final String removePath = "TAG.MENU.REMOVE-TAG.";
        ItemFactory removeItem = new ItemFactory(Material.valueOf(this.langFile.getString(removePath + "MATERIAL")))
                .setName(this.langFile.getString(removePath + "ITEM-NAME"));

        for (String lore : this.langFile.getStringList(removePath + "ITEM-LORE")) {
            removeItem.addLoreLine(lore);
        }

        if (this.langFile.getBoolean(removePath + "ENCHANTED")) {
            removeItem.setEnchanted();
        }

        SGButton button = new SGButton(removeItem.build()).withListener((InventoryClickEvent event) -> {
            profile.removeTag();
            player.sendMessage(CC.colour(this.langFile.getString("TAG.TAG-REMOVED")));
            player.closeInventory();
        });

        tagMenu.addButton(button);


        player.openInventory(tagMenu.getInventory());
    }
}