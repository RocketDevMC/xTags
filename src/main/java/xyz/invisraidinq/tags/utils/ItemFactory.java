package xyz.invisraidinq.tags.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * Easily create itemstacks, without messing your hands.
 * <i>Note that if you do use this in one of your projects, leave this notice.</i>
 * <i>Please do credit me if you do use this in one of your projects.</i>
 * @author NonameSL
 */
public class ItemFactory {

    private ItemStack is;

    /**
     * Create a new ItemFactory from scratch.
     * @param m The material to create the ItemFactory with.
     */
    public ItemFactory(Material m) {
        this(m, 1);
    }

    /**
     * Create a new ItemFactory over an existing itemstack.
     * @param is The itemstack to create the ItemFactory over.
     */
    public ItemFactory(ItemStack is) {
        this.is = is;
    }

    /**
     * Create a new ItemFactory from scratch.
     * @param m The material of the item.
     * @param amount The amount of the item.
     */
    public ItemFactory(Material m, int amount) {
        is = new ItemStack(m, amount);
    }

    /**
     * Create a new ItemFactory from scratch.
     * @param m The material of the item.
     * @param amount The amount of the item.
     * @param durability The durability of the item.
     */
    public ItemFactory(Material m, int amount, byte durability) {
        is = new ItemStack(m, amount, durability);
    }

    /**
     * Clone the ItemFactory into a new one.
     * @return The cloned instance.
     */
    public ItemFactory clone() {
        return new ItemFactory(is);
    }

    /**
     * Change the durability of the item.
     * @param dur The durability to set it to.
     */
    public ItemFactory setDurability(short dur) {
        is.setDurability(dur);
        return this;
    }

    /**
     * Set the displayname of the item.
     * @param name The name to change it to.
     */
    public ItemFactory setName(String name) {
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(CC.colour(name));
        is.setItemMeta(im);
        return this;
    }

    /**
     * Add an unsafe enchantment.
     * @param ench The enchantment to add.
     * @param level The level to put the enchant on.
     */
    public ItemFactory addUnsafeEnchantment(Enchantment ench, int level) {
        is.addUnsafeEnchantment(ench, level);
        return this;
    }

    public static ItemStack get(Material material, int amount, short datavalue, String displayName, List<String> lore)
    {
        ItemStack item = new ItemStack(material, amount, datavalue);
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        itemmeta.setLore(lore);
        item.setItemMeta(itemmeta);
        return item;
    }

    /**
     * Remove a certain enchant from the item.
     * @param ench The enchantment to remove
     */
    public ItemFactory removeEnchantment(Enchantment ench) {
        is.removeEnchantment(ench);
        return this;
    }

    /**
     * Set the skull owner for the item. Works on skulls only.
     * @param owner The name of the skull's owner.
     */
    public ItemFactory setSkullOwner(String owner) {
        try {
            SkullMeta im = (SkullMeta) is.getItemMeta();
            im.setOwner(owner);
            is.setItemMeta(im);
        } catch (ClassCastException expected) {}
        return this;
    }

    /**
     * Sets infinity durability on the item by setting the durability to Short.MAX_VALUE.
     */
    public ItemFactory setInfinityDurability() {
        is.setDurability(Short.MAX_VALUE);
        return this;
    }

    /**
     * Re-sets the lore.
     * @param lore The lore to set it to.
     */
    public ItemFactory setLore (String...lore) {
        ItemMeta im = is.getItemMeta();
        im.setLore(CC.colour(Arrays.asList(lore)));
        is.setItemMeta(im);
        return this;
    }

    /**
     * Re-sets the lore.
     * @param lore The lore to set it to.
     */
    public ItemFactory setLore(List <String> lore) {
        ItemMeta im = is.getItemMeta();
        im.setLore(CC.colour(lore));
        is.setItemMeta(im);
        return this;
    }

    /**
     * Add item flags to an item
     * @param flags Itemflags to set
     */
    public ItemFactory setFlags(ItemFlag...flags) {
        ItemMeta im = is.getItemMeta();
        im.addItemFlags(flags);
        is.setItemMeta(im);
        return this;
    }

    public ItemFactory setEnchanted() {
        is.addUnsafeEnchantment(Enchantment.LURE, 1);
        ItemMeta im = is.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        is.setItemMeta(im);
        return this;
    }

    public ItemFactory hideEnchants (boolean hidden) {
        if(hidden) {
            ItemMeta im = is.getItemMeta();
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            is.setItemMeta(im);
        }
        return this;
    }

    /**
     * Remove itemflags from an item
     * @param flags ItemFlags to remove
     */
    public ItemFactory removeFlags(ItemFlag...flags) {
        ItemMeta im = is.getItemMeta();
        im.removeItemFlags(flags);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Remove a lore line.
     * @param line The lore to remove.
     */
    public ItemFactory removeLoreLine(String line) {
        ItemMeta im = is.getItemMeta();
        List <String> lore = new ArrayList(im.getLore());
        if (!lore.contains(line)) return this;
        lore.remove(line);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }
    /**
     * Remove a lore line.
     * @param index The index of the lore line to remove.
     */
    public ItemFactory removeLoreLine(int index) {
        ItemMeta im = is.getItemMeta();
        List < String > lore = new ArrayList <String> (im.getLore());
        if (index < 0 || index > lore.size()) return this;
        lore.remove(index);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    /*
     * Set an item as unbreakable
     */
    public ItemFactory setUnbreakable() {
        ItemMeta im = is.getItemMeta();
        im.spigot().setUnbreakable(true);
        is.setItemMeta(im);
        return this;
    }
    /**
     * Add a lore line.
     * @param line The lore line to add.
     */
    public ItemFactory addLoreLine(String line) {
        ItemMeta im = is.getItemMeta();
        List < String > lore = new ArrayList <String> ();
        if (im.hasLore()) lore = new ArrayList <String> (im.getLore());
        lore.add(line);
        im.setLore(CC.colour(lore));
        is.setItemMeta(im);
        return this;
    }

    /**
     * Add a lore line.
     * @param line The lore line to add.
     * @param pos The index of where to put it.
     */
    public ItemFactory addLoreLine(String line, int pos) {
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        lore.set(pos, line);
        im.setLore(CC.colour(lore));
        is.setItemMeta(im);
        return this;
    }

    /**
     * Sets the armor color of a leather armor piece. Works only on leather armor pieces.
     * @param color The color to set it to.
     */
    public ItemFactory setLeatherArmorColor (Color color) {
        try {
            LeatherArmorMeta im = (LeatherArmorMeta) is.getItemMeta();
            im.setColor(color);
            is.setItemMeta(im);
        } catch (ClassCastException expected) {}
        return this;
    }

    public ItemStack build() {
        return is;
    }
}
