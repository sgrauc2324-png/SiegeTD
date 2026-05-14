package database;

public final class Game {
    private final int gameId;
    private final int userId;
    private final int level;
    private final String time;

    public Game(int gameId, int userId, int level, String time) {
        this.gameId = gameId;
        this.userId = userId;
        this.level = level;
        this.time = time;
    }

    public int getGameId() {
        return gameId;
    }

    public int getUserId() {
        return userId;
    }

    public int getLevel() {
        return level;
    }

    public String getTime() {
        return time;
    }
}