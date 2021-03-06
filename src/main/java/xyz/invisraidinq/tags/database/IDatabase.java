package xyz.invisraidinq.tags.database;

import org.bukkit.entity.Player;

public interface IDatabase {

    void loadTags();
    void saveTags();

    void loadProfile(Player player);
    void saveProfile(Player player);

    void closeDatabaseConnection();

}