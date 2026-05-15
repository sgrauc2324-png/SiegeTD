package entities;

public class Tower {
    public int x, y, level = 1, cooldown = 0;
    public int damage = 30;
    public int attackSpeed = 40;

    public Tower(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void upgrade() {
        level++;
        damage += 10;
        attackSpeed += 5;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getLevel() { return level; }
}