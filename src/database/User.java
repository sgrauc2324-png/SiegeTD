package database;

public class User {
    private int id;
    private String username;
    private int level;

    public User(int id, String username, int level) {
        this.id = id;
        this.username = username;
        this.level = level;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public int getLevel() { return level; }
}