package xyz.invisraidinq.tags.database.impl;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bukkit.entity.Player;
import xyz.invisraidinq.tags.TagsPlugin;
import xyz.invisraidinq.tags.database.IDatabase;
import xyz.invisraidinq.tags.profile.TagsProfile;
import xyz.invisraidinq.tags.tags.Tag;

import java.util.Collections;

public class MongoDBImpl implements IDatabase {

    private final TagsPlugin plugin;

    private final MongoClient mongoClient;
    private final MongoCollection<Document> tagsCollection;
    private final MongoCollection<Document> profilesCollection;

    public MongoDBImpl(TagsPlugin plugin) {
        this.plugin = plugin;

        final String databasePath = "DATABASE.MONGO.";
        ServerAddress serverAddress = new ServerAddress(plugin.getSettingsFile().getString(databasePath + "ADDRESS"),
                plugin.getSettingsFile().getInt(databasePath + "PORT"));

        if (plugin.getSettingsFile().getBoolean(databasePath + "AUTH.ENABLED")) {
            this.mongoClient = new MongoClient(serverAddress, Collections.singletonList(MongoCredential.createCredential(
                plugin.getSettingsFile().getString(databasePath + "AUTH.USERNAME"),
                plugin.getSettingsFile().getString(databasePath + "AUTH.DB"),
                plugin.getSettingsFile().getString(databasePath + "AUTH.PASSWORD").toCharArray())));
        } else {
            this.mongoClient = new MongoClient(serverAddress);
        }

        MongoDatabase mongoDatabase = this.mongoClient.getDatabase(plugin.getSettingsFile().getString(databasePath + "DATABASE"));

        this.tagsCollection = mongoDatabase.getCollection("Tags");
        this.profilesCollection = mongoDatabase.getCollection("Profiles");

        this.loadTags();
    }

    @Override
    public void loadTags() {
        for (Document document : this.tagsCollection.find()) {
            this.plugin.getTagManager().getTagList().add(new Tag(document.getString("name"), document.getString("format"),
                    document.getString("permission")));
        }
    }

    @Override
    public void saveTags() {
        //Have to delete all tags in the collection first
        for (Document document : this.tagsCollection.find()) {
            this.tagsCollection.deleteMany(document);
        }

        for (Tag tag : this.plugin.getTagManager().getTagList()) {
            Document document = new Document();

            document.put("name", tag.getTagName());
            document.put("format", tag.getFormat());
            document.put("permission", tag.getPermission());

            this.tagsCollection.replaceOne(Filters.eq("name", tag.getTagName()), document, new UpdateOptions().upsert(true));
        }
    }

    @Override
    public void loadProfile(Player player) {
        this.plugin.getProfileManager().getProfileMap().put(player.getUniqueId(), new TagsProfile(this.plugin, player));
        TagsProfile profile = this.plugin.getProfileManager().getProfileByPlayer(player);

        Document document = this.profilesCollection.find(Filters.eq("uuid", player.getUniqueId().toString())).first();

        if (document != null) {
            Tag tag = this.plugin.getTagManager().getTagByName(document.getString("tag"));
            if (tag != null) {
                profile.setTag(tag);
            } else {
                profile.setTag(null);
            }
        } else {
            profile.setTag(null);
        }
    }

    @Override
    public void saveProfile(Player player) {
        TagsProfile profile = this.plugin.getProfileManager().getProfileByPlayer(player);

        Document document = new Document();

        document.put("player", profile.getPlayer().getName());
        document.put("uuid", profile.getUuid().toString());
        document.put("tag", profile.getTag() != null ? profile.getTag().getTagName() : null);

        this.profilesCollection.replaceOne(Filters.eq("uuid", player.getUniqueId().toString()), document, new UpdateOptions().upsert(true));
        this.plugin.getProfileManager().getProfileMap().remove(player.getUniqueId());
    }

    @Override
    public void closeDatabaseConnection() {
        this.mongoClient.close();
    }
}
