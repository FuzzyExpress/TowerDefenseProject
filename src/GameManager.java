import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.JOptionPane;

import Entity.Enemy;
import Entity.BasicBug;
import Entity.Broodmother;
import Map.MapLoader;
import Map.PathFinder;
import Turret.TurretBase;
import Utility.GameSettings;

public class GameManager {
    private List<Enemy> enemies;
    private List<TurretBase> turrets;
    private int baseHealth;
    private int waveCount;
    private List<Point> path;
    private boolean gameOver;

    private boolean waitingForNextWave = false;
    private long waveCountdownStart = 0;
    private final int WAVE_DELAY_MS = 10_000;

    private boolean isFirstWave = true;
    private long firstWaveStartTime;

    // Staggered spawn queue
    private List<Enemy> spawnQueue = new ArrayList<>();
    private long lastSpawnTime = 0;
    private final int SPAWN_DELAY_MS = 500;

    public GameManager() {
        enemies = new ArrayList<>();
        turrets = new ArrayList<>();
        baseHealth = 100;
        waveCount = 0;
        gameOver = false;

        MapLoader mapLoader = new MapLoader();
        int[][] mapData = mapLoader.loadMap("src/Map/Test.png");
        path = new PathFinder(mapData, mapLoader.getWidth(), mapLoader.getHeight()).findPath();

        firstWaveStartTime = System.currentTimeMillis();
        lastSpawnTime = System.currentTimeMillis();
    }

    public void startGame() {
        GameLoop loop = new GameLoop(this);
        loop.start();
    }

    public void update() {
        if (gameOver) return;

        long currentTime = System.currentTimeMillis();

        // First wave message
        if (isFirstWave) {
            if (currentTime - firstWaveStartTime >= 3000) {
                isFirstWave = false;
                startNextWave();
            }
            return;
        }

        // Wait between waves
        if (waitingForNextWave) {
            if (currentTime - waveCountdownStart >= WAVE_DELAY_MS) {
                waitingForNextWave = false;
                startNextWave();
            }
            return;
        }

        // Staggered enemy spawn
        if (!spawnQueue.isEmpty() && currentTime - lastSpawnTime >= SPAWN_DELAY_MS) {
            Enemy e = spawnQueue.remove(0);
            addEnemy(e);
            lastSpawnTime = currentTime;
        }

        List<Enemy> toAdd = new ArrayList<>();
        List<Enemy> toRemove = new ArrayList<>();

        for (Enemy e : new ArrayList<>(enemies)) {
            e.update();

            if (!e.isAlive()) {
                if (e instanceof Broodmother broodmother) {
                    toAdd.add(new BasicBug(broodmother.getX(), broodmother.getY()));
                    toAdd.add(new BasicBug(broodmother.getX(), broodmother.getY()));
                    toAdd.add(new BasicBug(broodmother.getX(), broodmother.getY()));
                    broodmother.onDeath();
                }
                toRemove.add(e);
            } else if (e.hasReachedEnd()) {
                baseHealth -= 10;
                toRemove.add(e);
            }
        }

        enemies.removeAll(toRemove);
        enemies.addAll(toAdd);

        for (TurretBase turret : turrets) {
            turret.attackEnemies(enemies);
        }

        if (enemies.isEmpty() && spawnQueue.isEmpty() && !waitingForNextWave) {
            waitingForNextWave = true;
            waveCountdownStart = currentTime;
        }

        if (baseHealth <= 0 && !gameOver) {
            gameOver = true;
            showGameOverDialog();
        }
    }

    private void startNextWave() {
        waveCount++;
        int enemiesToSpawn = waveCount * 5;

        spawnQueue.clear();
        for (int i = 0; i < enemiesToSpawn; i++) {
            if (waveCount % 2 == 0) {
                spawnQueue.add(new Broodmother(0, 0)); // Even waves
            } else {
                spawnQueue.add(new BasicBug(0, 0));    // Odd waves
            }
        }

        lastSpawnTime = System.currentTimeMillis();
        System.out.println("Wave " + waveCount + " queued with " + enemiesToSpawn +
                (waveCount % 2 == 0 ? " Broodmothers." : " Basic Bugs."));
    }

    private void showGameOverDialog() {
        int choice = JOptionPane.showOptionDialog(null,
                "Game Over! Would you like to restart?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Restart", "Exit"},
                "Restart");

        if (choice == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            System.exit(0);
        }
    }

    private void resetGame() {
        enemies.clear();
        turrets.clear();
        baseHealth = 100;
        waveCount = 0;
        gameOver = false;
        waitingForNextWave = false;
        isFirstWave = true;
        spawnQueue.clear();
        firstWaveStartTime = System.currentTimeMillis();
    }

    public void addEnemy(Enemy enemy) {
        if (path != null && !path.isEmpty()) {
            Point start = path.get(0);
            enemy.setPosition(GameSettings.tileToScreen(start.x), GameSettings.tileToScreen(start.y));
            enemy.setPath(new ArrayList<>(path));
        }
        enemies.add(enemy);
    }

    public void addTurret(TurretBase turret) {
        turrets.add(turret);
    }

    public List<Enemy> getEnemies() {
        return new ArrayList<>(enemies);
    }

    public List<TurretBase> getTurrets() {
        return turrets;
    }

    public int getBaseHealth() {
        return baseHealth;
    }

    public int getWaveCount() {
        return waveCount;
    }

    public boolean isWaitingForNextWave() {
        return waitingForNextWave;
    }

    public boolean isFirstWave() {
        return isFirstWave;
    }

    public int getCountdownTimeLeft() {
        if (!waitingForNextWave) return 0;
        long elapsed = System.currentTimeMillis() - waveCountdownStart;
        return (int) Math.ceil((WAVE_DELAY_MS - elapsed) / 1500.0);
    }
}
