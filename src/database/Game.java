package database;

import java.sql.SQLException;

public final class Game {
    public int userId;
    public int level;
    public String time;

    public Game(int userId, int level, String time) {
        this.userId = userId;
        this.level = level;
        this.time = time;
    }

    public void saveGame(DatabaseManager db) throws SQLException {
        db.saveGame(this);
    }
}