import Map.MapLoader;
import Map.RGBConverter;
import Map.TileMapper;
import Map.Tile;

import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GamePanel extends JPanel {
    private GameManager gameManager;

    public GamePanel(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw enemies, towers, and the path based on the gameManager state
        // For example, loop through the enemy list and draw each enemy

        MapLoader mapLoader = new MapLoader();
        mapLoader.loadMap("src/Map/Test.png");
        int[][] map = mapLoader.getMapData();
        Tile[][] tiles = new Tile[mapLoader.getWidth()][mapLoader.getHeight()];
        for (int x = 0; x < mapLoader.getWidth(); x++) {
            for (int y = 0; y < mapLoader.getHeight(); y++) {
                tiles[x][y] = new Tile(x, y, map);
            }
        }

        for (int x = 0; x < mapLoader.getWidth(); x++) {
            for (int y = 0; y < mapLoader.getHeight(); y++) {
                tiles[x][y].draw(g);
            }
        }
    }
}