package entities;

import java.util.ArrayList;
import java.util.List;

public class WaveManager {
    private List<Enemy> enemies = new ArrayList<>();

    public void startNextWave(int count) {
        enemies.clear();
        for (int i = 0; i < count; i++) {
            enemies.add(new Enemy());
        }
    }

    public void update() {
        enemies.removeIf(e -> e.getHealth() <= 0);
    }

    public boolean isWaveFinished() {
        return enemies.isEmpty();
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }
}