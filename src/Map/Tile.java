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
            if (edgeConnect) {
                return invert ? false : true;
            }
            return invert ? true : false;
        }
        if (connectable.contains( TileMapper.GetTile(map[x][y]) )) {
            return invert ? false : true;
        }
        return invert ? true : false;
    }

    public String calculateHeading(int[][] map) {
        ArrayList<Tiles> connectable = new ArrayList<>();
        if (tile == Tiles.PATH || tile == Tiles.SPAWNER || tile == Tiles.END) {
            connectable = new ArrayList<>(Arrays.asList(Tiles.PATH, Tiles.SPAWNER, Tiles.END));
        } else if (tile == Tiles.GRASS) {
            connectable = new ArrayList<>(Arrays.asList(Tiles.GRASS));
        } else if (tile == Tiles.TREES) {
            connectable = new ArrayList<>(Arrays.asList(Tiles.TREES));
        } else if (tile == Tiles.WATER) {
            connectable = new ArrayList<>(Arrays.asList(Tiles.WATER));
        } else if (tile == Tiles.SAND) {
            connectable = new ArrayList<>(Arrays.asList(Tiles.SAND));
        } else if (tile == Tiles.STONE) {
            connectable = new ArrayList<>(Arrays.asList(Tiles.STONE));
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

    public void draw(Graphics g) {
        BufferedImage image;

        if (tile == Tiles.UNKNOWN) {
            try {
                image = ImageIO.read(new File( "art/unknown/tile.png" ));
            } catch (IOException e) {
                System.err.println("Unknown Image: art/unknown/tile.png");
                return;
            }
        }

        String tileName = tile.toString().toLowerCase();
        
        if (tile == Tiles.SPAWNER || tile == Tiles.END) {
            tileName = "path";
        }

        String path = "art/" + tileName + "/" + tileName + "-" + heading + ".png";

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
        g.drawImage(image, x * tileSize, y * tileSize, tileSize, tileSize, null);

    }
}
