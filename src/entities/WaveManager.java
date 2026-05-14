package entities;

import java.util.ArrayList;
import java.util.List;

public class WaveManager {
    private int waveNumber;
    private List<Enemy> enemies;

    public WaveManager() {
        this.waveNumber = 0;
        this.enemies = new ArrayList<>();
    }

    public void startNextWave(int enemyCount) {
        waveNumber++;
        enemies.clear();
        for (int i = 0; i < enemyCount; i++) {
            enemies.add(new Enemy(0, 0));
        }
    }

    public void update() {
        for (int i = enemies.size() - 1; i >= 0; i--) {
            if (enemies.get(i).getHealth() <= 0) {
                enemies.remove(i);
            }
        }
    }

    public boolean isWaveFinished() {
        return enemies.isEmpty();
    }

    public int getWaveNumber() {
        return waveNumber;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }
}