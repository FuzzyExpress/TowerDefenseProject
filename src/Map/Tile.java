package Map;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Arrays;
import java.util.ArrayList;
import Utility.GameSettings;

public class Tile {
    private int x;
    private int y;
    private Tiles tile;
    private String heading;

    public Tile(int x, int y, int[][] map) {
        this.x = x;
        this.y = y;
        this.tile = TileMapper.GetTile(map[x][y]);
        this.heading = calculateHeading(map);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private boolean verifyHeading(int x, int y, int[][] map, ArrayList<Tiles> connectable, boolean invert, boolean edgeConnect) {
        if (x < 0 || x >= map.length || y < 0 || y >= map[0].length) {
            if (true) {// (edgeConnect) {
                return invert ? false : true;
            }
            return invert ? true : false;
        }
        if (connectable.contains( TileMapper.GetTile(map[x][y]) )) {
            return invert ? false : true;
        }
        return invert ? true : false;
    }

    public Tiles getTile() {
        return tile;
    }

    public String calculateHeading(int[][] map) {
        ArrayList<Tiles> connectable = new ArrayList<>();
        if (tile == Tiles.PATH || tile == Tiles.SPAWNER || tile == Tiles.END) {
            connectable = new ArrayList<>(Arrays.asList(Tiles.PATH, Tiles.SPAWNER, Tiles.END));
        } else if (tile == Tiles.GRASS || tile == Tiles.TREES) {
            connectable = new ArrayList<>(Arrays.asList(Tiles.GRASS, Tiles.TREES));
        } else {
            connectable = new ArrayList<>(Arrays.asList(tile));
        }

        String tileHeading = "";

        boolean invert = true; // tile != Tiles.PATH;
        
        boolean edgeConnect = tile == Tiles.SPAWNER || tile == Tiles.END;
        int[][] directions = {
            {-1, -1}, {0, -1}, {1,  -1},  // Top row
            {1,   0},              // Middle row
            {1,   1}, {0,  1}, {-1,  1},     // Bottom row
            {-1,  0}
        };
        
        for (int[] dir : directions) {
            tileHeading += verifyHeading(x + dir[0], y + dir[1], map, connectable, invert, edgeConnect) ? "1" : "0";
        }


        return tileHeading;
    }

    private String getTileName() {
        String tileName = tile.toString().toLowerCase();

        if (heading.length() > 0) {
            tileName += "-" + heading;
        } else { tileName += "-d"; }

        return tileName;
    }

    public void draw(Graphics g, int baseHealth) {
        Tiles.PATH.draw(g, heading, x, y);
        String path = "art/sugar/sugar-" + baseHealth/10 + ".png";
        BufferedImage image;

        try {
            image = ImageIO.read(new File( path ));
        } catch (IOException e) {
            System.err.println("Unknown Image: " + path);
            try {
                image = ImageIO.read(new File( "art/unknown/image.png" ));
            } catch (IOException E) {
                System.err.println("Unknown Image: art/unknown/image.png");
                return;
            }
        }
        int tileSize = GameSettings.getTileSize();
        g.drawImage(image, x * tileSize - tileSize/3, y * tileSize - tileSize/3, tileSize + tileSize/2, tileSize + tileSize/2, null);
    }

    public void draw(Graphics g) {
        
        if (tile == Tiles.SPAWNER || tile == Tiles.END) {
            Tiles.PATH.draw(g, heading, x, y);
        } else if (tile == Tiles.TREES) {
            Tiles.GRASS.draw(g, heading, x, y);
        } else {
            tile.draw(g, heading, x, y);
        }
    }
}
