package xyz.invisraidinq.tags.license;

import xyz.invisraidinq.tags.TagsPlugin;
import xyz.invisraidinq.tags.utils.CC;

import java.util.concurrent.TimeUnit;

public class LicenseChecker {

    public boolean checkLicenseValidity (TagsPlugin plugin) {
        License license = new License("xTags", plugin, plugin.getSettingsFile().getString("LICENSE-KEY"));
        try {
            String name = license.checkLicense().get(5, TimeUnit.SECONDS);
            if (name == null || name.isEmpty()) {
                CC.out("Your license key is invalid. Please contact development with the error code ERR_NME_EMT");
                return false;
            }
            CC.out(" ");
            CC.out("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            CC.out("Your xTags license has has been authenticated");
            CC.out("Thank you for purchasing the plugin, " + name);
            CC.out("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            CC.out(" ");
            return true;
        } catch (Exception e) {
            CC.out("A plugin error has occured. Please contact development with the error code ERR_SRT_UP");
            e.printStackTrace();
            return false;
        }
    }
}
