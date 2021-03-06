package xyz.invisraidinq.tags.tags;

public class Tag {

    private final String tagName;
    private final String format;
    private String permission;

    public Tag(String tagName, String format, String permission) {
        this.tagName = tagName;
        this.format = format;
        this.permission = permission;
    }

    public String getTagName() {
        return this.tagName;
    }

    public String getFormat() {
        return this.format;
    }

    public String getPermission() {
        return this.permission != null ? this.permission : "None set";
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean hasPermission() {
        return this.permission != null;
    }

}