package xyz.invisraidinq.tags.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class CC {

    public static final String DOUBLE_ARROW = "»";

    public static final String AQUA = ChatColor.AQUA.toString();
    public static final String BLACK = ChatColor.BLACK.toString();
    public static final String BLUE = ChatColor.BLUE.toString();
    public static final String GOLD = ChatColor.GOLD.toString();
    public static final String GRAY = ChatColor.GRAY.toString();
    public static final String GREEN = ChatColor.GREEN.toString();
    public static final String PINK = ChatColor.LIGHT_PURPLE.toString();
    public static final String RED = ChatColor.RED.toString();
    public static final String WHITE = ChatColor.WHITE.toString();
    public static final String YELLOW = ChatColor.YELLOW.toString();

    public static final String D_AQUA = ChatColor.DARK_AQUA.toString();
    public static final String D_BLUE = ChatColor.DARK_BLUE.toString();
    public static final String D_GRAY = ChatColor.DARK_GRAY.toString();
    public static final String D_GREEN = ChatColor.DARK_GREEN.toString();
    public static final String D_PURPLE = ChatColor.DARK_PURPLE.toString();
    public static final String D_RED = ChatColor.DARK_RED.toString();

    public static final String BOLD = ChatColor.BOLD.toString();
    public static final String STRIKETHGOUGH = ChatColor.STRIKETHROUGH.toString();
    public static final String RESET = ChatColor.RESET.toString();
    public static final String RANDOM = ChatColor.MAGIC.toString();
    public static final String UNDERLINE = ChatColor.UNDERLINE.toString();
    public static final String ITALIC = ChatColor.ITALIC.toString();
    public static final String LG_SPACER = colour("&7&m----------------------------");
    public static final String DG_SPACER = colour("&8&m----------------------------");

    public static List<String> colour (List<String> list) {
        List<String> translated = new ArrayList<>();
        for (String string : list) translated.add(colour(string));
        return translated;
    }

    public static String colour(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String out(String out) {
        Bukkit.getConsoleSender().sendMessage(colour("[xTags] " + out));
        return out;
    }

    public static String strip(String str) {
        return ChatColor.stripColor(str);
    }
}
