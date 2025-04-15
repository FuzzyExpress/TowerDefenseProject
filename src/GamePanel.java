import Map.MapLoader;
import Map.RGBConverter;
import Map.TileMapper;
import Map.Tile;
import Map.PathFinder;

import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Point;
import java.util.List;

public class GamePanel extends JPanel {
    private GameManager gameManager;
    private MapLoader mapLoader;
    private Tile[][] tiles;
    private List<Point> path;

    public GamePanel(GameManager gameManager) {
        this.gameManager = gameManager;
        
        // Load the map once when the panel is created
        mapLoader = new MapLoader();
        mapLoader.loadMap("src/Map/Test.png");
        int[][] map = mapLoader.getMapData();
        
        // Create tiles
        tiles = new Tile[mapLoader.getWidth()][mapLoader.getHeight()];
        for (int x = 0; x < mapLoader.getWidth(); x++) {
            for (int y = 0; y < mapLoader.getHeight(); y++) {
                tiles[x][y] = new Tile(x, y, map);
            }
        }
        
        // Find path from start to end
        PathFinder pathFinder = new PathFinder(map, mapLoader.getWidth(), mapLoader.getHeight());
        path = pathFinder.findPath();
        System.out.println("Path found with " + path.size() + " points");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw the tiles
        for (int x = 0; x < mapLoader.getWidth(); x++) {
            for (int y = 0; y < mapLoader.getHeight(); y++) {
                tiles[x][y].draw(g);
            }
        }
        
        // Draw the path with small yellow circles
        if (path != null && !path.isEmpty()) {
            g.setColor(Color.YELLOW);
            for (Point p : path) {
                // Draw a small circle at each point of the path
                g.fillOval(p.x * 40 + 15, p.y * 40 + 15, 10, 10);
            }
        }
        
        // Draw enemies from the game manager
        for (Entity.Enemy enemy : gameManager.getEnemies()) {
            enemy.draw(g);
        }
    }
}