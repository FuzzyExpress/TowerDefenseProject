package Map;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Tiles {
    private static final HashMap<String, String> tiles = new HashMap<>();
    private static boolean loaded = false;
    private static void load()
    {
        loaded = true;
        tiles.put("255, 204, 0", "path");
        tiles.put("0, 69, 0", "grass");
        tiles.put("0, 255, 0", "trees");
        tiles.put("0, 0, 255", "water");
        tiles.put("255, 255, 0", "sand");
        tiles.put("180, 180, 180", "stone");
    };

    
    public static BufferedImage GetTile(int[] rgb) throws IOException {

        if (!loaded) { load(); }

        String tile = tiles.get(rgb[0] + ", " + rgb[1] + ", " + rgb[2]);
        String path = "art/" + tile + "/" + tile + "-d.png";
        try {
            System.out.println("Loading tile: " + path);
            return ImageIO.read(new File( path ));
        } catch (IOException e) {
            return ImageIO.read(new File( "art/null/null-null.png" ));
        }
    }
}
