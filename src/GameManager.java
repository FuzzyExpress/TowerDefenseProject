import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.JOptionPane;

import Entity.Enemy;
import Entity.BasicBug;
import Entity.Broodmother;
import Entity.FastBug;
import Map.MapLoader;
import Map.PathFinder;
import Turret.TurretBase;
import Utility.GameSettings;
import Utility.ScoreManager;


public class GameManager {
    private List<Enemy> enemies;
    private List<TurretBase> turrets;
    private int baseHealth;
    private int waveCount;
    private List<Point> path;
    private boolean gameOver;
    private ScoreManager scoreManager = new ScoreManager();

    private boolean waitingForNextWave = false;
    private long waveCountdownStart = 0;
    private final int WAVE_DELAY_MS = 10_000;

    private boolean isFirstWave = true;
    private long firstWaveStartTime;

    //Pause
    private boolean isPaused = false;
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

    //Pause
    public void togglePause()
    {
        isPaused = !isPaused;
    }

    public boolean isPaused()
    {
        return isPaused;
    }

    public void startGame() {
        GameLoop loop = new GameLoop(this);
        loop.start();
    }

    public void update() {
        if (gameOver || isPaused) return;

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
            if (e != null) {
                addEnemy(e);
            }
            lastSpawnTime = currentTime;
        }

        List<Enemy> toAdd = new ArrayList<>();
        List<Enemy> toRemove = new ArrayList<>();

        for (Enemy e : new ArrayList<>(enemies)) {
            e.update();

            if (!e.isAlive()) {
                scoreManager.addPoints(e.getPointValue());
                if (e instanceof Broodmother broodmother) {
                    int motherX = broodmother.getX();
                    int motherY = broodmother.getY();

                    int closestIndex = 0;
                    double closestDistance = Double.MAX_VALUE;

                    for (int i = 0; i < path.size(); i++) {
                        Point p = path.get(i);
                        int pathX = GameSettings.tileToScreen(p.x);
                        int pathY = GameSettings.tileToScreen(p.y);
                        double dist = Math.hypot(motherX - pathX, motherY - pathY);
                        if (dist < closestDistance) {
                            closestDistance = dist;
                            closestIndex = i;
                        }
                    }

                    for (int i = 0; i < 3; i++) {
                        BasicBug baby = new BasicBug(motherX, motherY);
                        baby.setPath(new ArrayList<>(path.subList(closestIndex, path.size())));
                        toAdd.add(baby);
                    }

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

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    private void startNextWave() {
        waveCount++;
        spawnQueue.clear();

        switch (waveCount) {
            case 1 -> spawnEnemies(5, 0, 0); // 5 Basic
            case 2 -> spawnAlternatingEnemies(10); // 10 alternating Basic/Fast
            case 3 -> spawnEnemies(5, 5, 5); // 5,5,5
            case 4 -> spawnEnemies(10, 5, 5); // 10,5,5
            case 5 -> spawnEnemies(15, 10, 0); // 15,10
            case 6 -> spawnEnemies(10, 5, 15); // 10,5,15
            default -> {
                // After wave 6 just scale up gradually
                int basics = 10 + (waveCount - 6) * 5;
                int fasts = 5 + (waveCount % 3) * 3;
                int broods = (waveCount % 2 == 0) ? 10 : 5;
                spawnEnemies(basics, fasts, broods);
            }
        }

        lastSpawnTime = System.currentTimeMillis();
        // System.out.println("Wave " + waveCount + " spawned with " + spawnQueue.size() + " enemies.");
    }

    private void spawnEnemies(int basic, int fast, int brood) {
        for (int i = 0; i < basic; i++) {
            spawnQueue.add(new BasicBug(0, 0));
            spawnQueue.add(null); //Add a little more space between the basic bugs
        }
        for (int i = 0; i < fast; i++) {
            spawnQueue.add(new FastBug(0, 0));
        }
        // Spread Broodmother spawns out by adding a "null" placeholder
        for (int i = 0; i < brood; i++) {
            spawnQueue.add(new Broodmother(0, 0));
            // Add some padding for delay
            for (int j = 0; j < 3; j++) {
                spawnQueue.add(null); // add dummy slots between Broodmothers
            }
        }
    }


    private void spawnAlternatingEnemies(int total) {
        for (int i = 0; i < total; i++) {
            if (i % 2 == 0) {
                spawnQueue.add(new BasicBug(0, 0));
            } else {
                spawnQueue.add(new FastBug(0, 0));
            }
        }
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
        scoreManager.reset();
    }

    public boolean tryPlaceTurret(TurretBase turret) {
        if (scoreManager.getScore() >= turret.getCost()) {
            scoreManager.deductScore(turret.getCost());
            turrets.add(turret);
            return true;
        }
        return false;
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
