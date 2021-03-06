package xyz.invisraidinq.tags.database.impl;

import org.bukkit.entity.Player;
import xyz.invisraidinq.tags.TagsPlugin;
import xyz.invisraidinq.tags.database.IDatabase;
import xyz.invisraidinq.tags.profile.TagsProfile;
import xyz.invisraidinq.tags.tags.Tag;
import xyz.invisraidinq.tags.utils.CC;

import java.sql.*;

//Sorry if this is a mess, I HATE mysql
public class MySQLImpl implements IDatabase {

    private final TagsPlugin plugin;
    private Connection connection;

    public MySQLImpl(TagsPlugin plugin) {
        this.plugin = plugin;
        final String databasePath = "DATABASE.MYSQL.";

        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + plugin.getSettingsFile().getString(databasePath + "ADDRESS") + ":"
                            + plugin.getSettingsFile().getInt(databasePath + "PORT") + "/" + plugin.getSettingsFile().getString(databasePath + "DATABASE")
                            + "?useSSL=" + plugin.getSettingsFile().getBoolean(databasePath + "USE-SSL"),
                            plugin.getSettingsFile().getString(databasePath + "USERNAME"), plugin.getSettingsFile().getString(databasePath + "PASSWORD"));

            PreparedStatement profileTable = this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS profiles (Player VARCHAR(100), UUID VARCHAR(100), Tag VARCHAR(100), PRIMARY KEY (Player))");
            PreparedStatement tagsTable = this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS tags (Name VARCHAR(100), Format VARCHAR(100), Permission VARCHAR(100), PRIMARY KEY (Name))");

            profileTable.executeUpdate();
            tagsTable.executeUpdate();

        } catch (Exception e) {
            CC.out("Failed to connect to MySQL Database, please check the credentials!");
            e.printStackTrace();
            return;
        }

        this.loadTags();

    }


    @Override
    public void loadTags() {
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM tags ORDER BY Name");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Tag tag = new Tag(resultSet.getString("Name"),
                        resultSet.getString("Format"), resultSet.getString("Permission"));
                this.plugin.getTagManager().getTagList().add(tag);
            }

        } catch (Exception e) {
            CC.out("Failed to load tags from the database!");
        }
    }

    @Override
    public void saveTags() {
        //Need to delete all values first
        try {
            PreparedStatement clearDataStatement = this.connection.prepareStatement("DELETE FROM tags");
            clearDataStatement.executeUpdate();

            for (Tag tag : this.plugin.getTagManager().getTagList()) {
                PreparedStatement tagStatement = this.connection.prepareStatement("INSERT INTO tags (Name, Format, Permission) VALUES (?,?,?)");
                tagStatement.setString(1, tag.getTagName());
                tagStatement.setString(2, tag.getFormat());
                tagStatement.setString(3, tag.getPermission());
                tagStatement.executeUpdate();
            }
        } catch (Exception e) {
            CC.out("Failed to save tags!");
            e.printStackTrace();
        }
    }

    @Override
    public void loadProfile(Player player) {
        this.plugin.getProfileManager().getProfileMap().put(player.getUniqueId(), new TagsProfile(this.plugin, player));
        TagsProfile profile = this.plugin.getProfileManager().getProfileByPlayer(player);

        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM profiles WHERE UUID=?");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Tag tag = this.plugin.getTagManager().getTagByName(resultSet.getString("Tag"));
                if (tag != null) {
                    profile.setTag(tag);
                } else {
                    profile.setTag(null);
                }
            } else {
                profile.setTag(null);
            }
        } catch (Exception e) {
            CC.out("Something went wrong querying the database for the playerdata of " + player.getName());
            e.printStackTrace();
        }
    }

    @Override
    public void saveProfile(Player player) {
        TagsProfile profile = this.plugin.getProfileManager().getProfileByPlayer(player);
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM profiles WHERE UUID=?");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                PreparedStatement deleteStatement = this.connection.prepareStatement("DELETE FROM profiles WHERE UUID=?");
                deleteStatement.setString(1, player.getUniqueId().toString());
                deleteStatement.executeUpdate();
            }

            PreparedStatement profileUpdateStatement = this.connection.prepareStatement("INSERT INTO profiles (Player, UUID, Tag) VALUES (?,?,?)");

            profileUpdateStatement.setString(1, player.getName());
            profileUpdateStatement.setString(2, player.getUniqueId().toString());
            profileUpdateStatement.setString(3, profile.getTag().getTagName());

            profileUpdateStatement.executeUpdate();
        } catch (Exception e) {
            CC.out("Failed to save player profile of " + player.getName());
            e.printStackTrace();
        }
    }

    @Override
    public void closeDatabaseConnection() {
        if (this.isConnected()) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                CC.out("Failed to save MySQL Database, this will break something!");
                e.printStackTrace();
            }
        }
    }

    private boolean isConnected() {
        return this.connection != null;
    }
}
