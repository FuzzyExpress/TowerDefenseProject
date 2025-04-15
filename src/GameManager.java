import Entity.*;
import Map.MapLoader;
import Map.PathFinder;
import java.util.ArrayList;
import java.util.List;
import java.awt.Point;

public class GameManager {
    private final List<Enemy> enemies;
    private final int baseHealth;
    private final int waveCount;
    private List<Point> path;
    private MapLoader mapLoader;
    // You can also include other fields such as the path for enemies, etc.

    public GameManager() {
        enemies = new ArrayList<>();
        baseHealth = 100;
        waveCount = 0;
        mapLoader = new MapLoader();
    }
    
    public void loadMap(String mapPath) {
        mapLoader.loadMap(mapPath);
        int[][] mapData = mapLoader.getMapData();
        
        // Generate path using PathFinder
        PathFinder pathFinder = new PathFinder(mapData, mapLoader.getWidth(), mapLoader.getHeight());
        path = pathFinder.findPath();
        System.out.println("Path loaded with " + path.size() + " points");
    }
    
    public List<Point> getPath() {
        return path;
    }

    public void addEnemy(Enemy enemy) {
        if (path != null && !path.isEmpty()) {
            // Set enemy starting position to the first point in the path
            Point start = path.get(0);
            enemy.setPosition(start.x * 40, start.y * 40); // Multiply by tile size
            enemy.setPath(path);
        }
        enemies.add(enemy);
    }

    public void update() {
        List<Enemy> enemiesToRemove = new ArrayList<>();
        
        for (Enemy enemy : enemies) {
            enemy.update();
            
            // Check if enemy reached the end
            if (enemy.hasReachedEnd()) {
                enemiesToRemove.add(enemy);
                // Apply damage to base health, etc.
            }
            
            // Check if enemy is dead
            if (enemy.getHealth() <= 0) {
                enemiesToRemove.add(enemy);
                // Add score, gold, etc.
            }
        }
        
        // Remove enemies that reached the end or died
        enemies.removeAll(enemiesToRemove);
    }
    
    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void startGame() {
        // Initialize game state
        loadMap("src/Map/Test.png");
        System.out.println("Game started!");
    }
    
    public static void main(String[] args) {
        GameManager gm = new GameManager();
        // Create an enemy with 100 health, speed 1.0, starting at position (0, 0)
        Enemy enemy1 = new BasicBug(0, 0);
        gm.addEnemy(enemy1);
        
        gm.update(); // update game state
        System.out.println("GameManager updated. Enemy at (" + enemy1.getX() + ", " + enemy1.getY() + ").");
    }
}
