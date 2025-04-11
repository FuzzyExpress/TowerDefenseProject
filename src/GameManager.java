import Entity.Enemy;  // Correct import
import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private final List<Enemy> enemies;
    private final int baseHealth;
    private final int waveCount;
    // You can also include other fields such as the path for enemies, etc.

    public GameManager() {
        enemies = new ArrayList<>();
        baseHealth = 100;
        waveCount = 0;
    }

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    public void update() {
        for (Enemy enemy : enemies) {
            enemy.update();
            // Add any collision or game state logic here.
        }
    }
    
    public static void main(String[] args) {
        GameManager gm = new GameManager();
        // Create an enemy with 100 health, speed 1.0, starting at position (0, 0)
        Enemy enemy1 = new Enemy(100, 1.0f, 0, 0);
        gm.addEnemy(enemy1);
        
        gm.update(); // update game state
        System.out.println("GameManager updated. Enemy at (" + enemy1.getX() + ", " + enemy1.getY() + ").");
    }
}
