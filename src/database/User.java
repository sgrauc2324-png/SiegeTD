package database;

public class User {
    protected int id;
    protected String username;
    protected int actualLevel;

    protected User(int id, String username, int actualLevel) {
        this.id = id;
        this.username = username;
        this.actualLevel = actualLevel;
    }

    public void actualizarNivel(int newLevel) {
        this.actualLevel = newLevel;
    }
}