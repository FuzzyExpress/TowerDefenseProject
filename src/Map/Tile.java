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
    private String tileName;

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

    public void draw(Graphics g) {
        int tileSize = GameSettings.getTileSize();
        g.drawImage(getImage(), x * tileSize, y * tileSize, tileSize, tileSize, null);
    }

    private boolean verifyHeading(int x, int y, int[][] map, ArrayList<Tiles> connectable) {
        if (x < 0 || x >= map.length || y < 0 || y >= map[0].length) {
            return false;
        }
        if (connectable.contains( TileMapper.GetTile(map[x][y]) )) {
            return true;
        }
        return false;
    }

    public String calculateHeading(int[][] map) {
        ArrayList<Tiles> connectable = new ArrayList<>(Arrays.asList(Tiles.PATH, Tiles.SPAWNER, Tiles.END));
        String tileHeading = "";

        if (tile == Tiles.PATH) {
            if   (verifyHeading(x, y-1, map, connectable)) {
                tileHeading += "N";
            } if (verifyHeading(x, y+1, map, connectable)) {
                tileHeading += "S";
            } if (verifyHeading(x-1, y, map, connectable)) {
                tileHeading += "W";
            } if (verifyHeading(x+1, y, map, connectable)) {
                tileHeading += "E";
            }
        }

        String tileName = tile.toString().toLowerCase();

        if (tileHeading.length() > 0) {
            tileName += "-" + tileHeading;
        } else { tileName += "-d"; }


        this.tileName = tileName;

        // System.err.println(tileName + " " + tileHeading);

        return tileHeading;
    }

    private BufferedImage getImage() {

        if (tile == Tiles.UNKNOWN) {
            try {
                return ImageIO.read(new File( "art/unknown/tile.png" ));
            } catch (IOException e) {
                System.err.println("Unknown Image: art/unknown/tile.png");
                return null;
            }
        }

        String path = "art/" + tile.toString().toLowerCase() + "/" + this.tileName + ".png";

        try {
            return ImageIO.read(new File( path ));
        } catch (IOException e) {
            // System.err.println("Unknown Image: " + path);
            try {
                return ImageIO.read(new File( "art/unknown/image.png" ));
            } catch (IOException E) {
                System.err.println("Unknown Image: art/unknown/image.png");
                return null;
            }
        }

    }
}
