package database;

public final class User {
    private final int id;
    private final String username;
    private int actualLevel;

    public User(int id, String username, int actualLevel) {
        this.id = id;
        this.username = username;
        this.actualLevel = actualLevel;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getActualLevel() {
        return actualLevel;
    }

    public void updateLevel(int newLevel) {
        if (newLevel > 0) {
            this.actualLevel = newLevel;
        }
    }
}