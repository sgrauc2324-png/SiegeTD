package entities;

public class Enemy {
    private int x;
    private int y;
    private int health;
    private String direction;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
        this.health = 30;
        this.direction = "NONE";
    }

    public void move(boolean up, boolean down, boolean left, boolean right) {
        if (direction.equals("UP") && up) {
            y--;
            return;
        }
        if (direction.equals("DOWN") && down) {
            y++;
            return;
        }
        if (direction.equals("LEFT") && left) {
            x--;
            return;
        }
        if (direction.equals("RIGHT") && right) {
            x++;
            return;
        }

        if (up && !direction.equals("DOWN")) {
            y--;
            direction = "UP";
        } else if (down && !direction.equals("UP")) {
            y++;
            direction = "DOWN";
        } else if (left && !direction.equals("RIGHT")) {
            x--;
            direction = "LEFT";
        } else if (right && !direction.equals("LEFT")) {
            x++;
            direction = "RIGHT";
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}