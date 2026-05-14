package database;

public class Game {
    protected int gameId;
    protected int userId;
    protected int level;
    protected int time;

    public Game(int gameId, int userId, int level, int time) {
        this.gameId = gameId;
        this.userId = userId;
        this.level = level;
        this.time = time;
    }
}