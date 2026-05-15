package entities;

public class Tower {
    private int x;
    private int y;
    private int level = 1;

    public Tower(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLevel() {
        return level;
    }

    public void upgrade() {
        level++;
    }
}