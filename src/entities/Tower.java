package entities;

public class Tower {
    private int price;
    private int dmg;
    private double fireRate;
    private int range;

    public Tower(int price, double dmg, double fireRate, double range) {
        this.price = 100;
        this.dmg = 10;
        this.fireRate = 1.2;
        this.range = 30;
    }

    public void shoot() {
    }

    public void build() {
    }

    public void remove() {
    }

    public void upgrade() {
        this.dmg += 5;
        this.range += 10;
        this.fireRate += 0.3;
        this.price += 50;
    }

    public int getPrice() {
        return price;
    }

    public int getDmg() {
        return dmg;
    }

    public double getFireRate() {
        return fireRate;
    }

    public int getRange() {
        return range;
    }
}