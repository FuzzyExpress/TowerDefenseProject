import java.util.ArrayList;
import java.util.List;

import Entity.Enemy;

public class GameManager {
    private List<Enemy> enemies;
    private int baseHealth;
    private int waveCount;
    private Path path; // Level path

    public GameManager() {
        enemies = new ArrayList<>();
        baseHealth = 100; // Initial base health
        waveCount = 0;
        path = new Path(); // Create a fixed path
    }

    // Start the game and initialize the game loop
    public void startGame() {
        // Initialize enemies, waves, and other state variables
        GameLoop loop = new GameLoop(this);
        loop.start();
    }

    // Update the game state: enemy movement, collision detection, wave management, etc.
    public void update() {
        for (Enemy enemy : enemies) {
            enemy.update(enemy.getSpeed());
            // Add logic here to check if an enemy reaches the base and reduce base health
        }
        // Add logic to update wave count and spawn new enemies as needed
    }

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    // Getters and setters omitted for brevity...
}
