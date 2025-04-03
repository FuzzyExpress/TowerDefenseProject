package Map;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Arrays;


public class TileMapper {
    private static final HashMap<String, Tiles> tiles = new HashMap<>();
    private static boolean loaded = false;
    
    private static void load()
    {
        loaded = true;
        tiles.put("255, 204, 0", Tiles.PATH);
        tiles.put("0, 69, 0", Tiles.GRASS);
        tiles.put("0, 255, 0", Tiles.TREES);
        tiles.put("0, 0, 255", Tiles.WATER);
        tiles.put("255, 255, 0", Tiles.SAND);
        tiles.put("180, 180, 180", Tiles.STONE);
        tiles.put("255, 0, 0", Tiles.SPAWNER);
        tiles.put("0, 255, 255", Tiles.END);
    };

    public static Tiles GetTileType(int[] rgb) {
        if (!loaded) { load(); }
        return tiles.get(rgb[0] + ", " + rgb[1] + ", " + rgb[2]);
    }
    
    public static Tiles GetTile(int _rgb) {

        int[] rgb = RGBConverter.rgbIntToArray(_rgb);

        if (!loaded) { load(); }

        Tiles tile = tiles.get(rgb[0] + ", " + rgb[1] + ", " + rgb[2]);

        if (tile == null) {
            System.out.println("Unknown Tile: " + rgb[0] + ", " + rgb[1] + ", " + rgb[2]);
            return Tiles.UNKNOWN;
        }

        return tile;
    }
}
