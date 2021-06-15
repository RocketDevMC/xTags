package xyz.invisraidinq.tags.license;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.invisraidinq.tags.utils.CC;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class License {

    private List<String> developerArray = Arrays.asList(
            "351f4906-bfe8-4540-b6e9-f219d111ce62",
            "7f5abbf8-b98f-405d-850f-59ce599a92a2",
            "2312d6dd-4c56-445e-91c4-1f0436c17f8f");

    private String name;
    private Plugin plugin;
    private String license;

    public License(String name, Plugin plugin, String license) {
        this.name = name;
        this.plugin = plugin;
        this.license = license;

        this.startDeveloperCheck();
    }

    public CompletableFuture<String> checkLicense() {
        CompletableFuture<String> future = new CompletableFuture<>();

        if (!checkYML()) {
            this.printEmptyLines();
            CC.out("xTags license key isn't valid");
            CC.out("Please DM Invis#0010 to receive your key");
            this.printEmptyLines();
            return null;
        }

        try {
            URL url = new URL("https://invisraidinq.xyz/license/newValidate.php?plugin=" + name + "&license=" + license);

            URLConnection connection = url.openConnection();

            connection.addRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");;

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            List<String> lines = reader.lines().collect(Collectors.toList());

            if (!lines.isEmpty()) {
                future.complete(lines.get(0));
            }
        } catch (Exception e) {
            this.printEmptyLines();
            CC.out("Looks like an exception occured validating your license");
            CC.out("Please contact Invis#0010 to get this sorted!");
            this.printEmptyLines();
            return null;
        }
        return future;
    }

    private boolean checkYML() {
        return plugin.getDescription().getName().equalsIgnoreCase(name);
    }

    private void printEmptyLines() {
        for (int i = 0; i < 3; i++) {
            System.out.println(" ");
        }
    }

    private void startDeveloperCheck() {
        new BukkitRunnable() {
            public void run() {
                for (String uuid : developerArray) {
                    if (Bukkit.getPlayer(uuid) != null) {
                       Bukkit.getPlayer(uuid).sendMessage(CC.colour("&eThis server is running &a" + name + " &e version &a" + plugin.getDescription().getVersion() +
                               " &ewith license key &a" + license));
                    }
                }
            }
        }.runTaskTimerAsynchronously(this.plugin, 0L, 20 * 60);
    }
}
