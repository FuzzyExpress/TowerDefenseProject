package Map;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.*;

public class MapLoader {
    private int[][] mapData;
    private int width;
    private int height;

    public int[][] loadMap(String path) {
        try {
            BufferedImage map = ImageIO.read(new File(path));
            width = map.getWidth();
            height = map.getHeight();
            mapData = new int[width][height];
            FastRGB fastRGB = new FastRGB(map);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    mapData[x][y] = fastRGB.getRGB(x, y);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return mapData;
    }

    public int[][] getMapData() {
        return mapData;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
