package Map;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;

public class MapLoader {
    int[][] mapData;

    public int[][] loadMap(String path) {
        BufferedImage map = null;
        try {
            map = ImageIO.read(new File(path));
            mapData = new int[map.getWidth()][map.getHeight()];
            FastRGB fastRGB = new FastRGB(map);
            
            for (int x = 0; x < map.getWidth(); x++) {
                for (int y = 0; y < map.getHeight(); y++) {
                    int rgb = fastRGB.getRGB(x, y);
                    mapData[x][y] = rgb;
                }
            }

            for (int[] row : mapData) {
                for (int cell : row) {
                    int[] rgb = RGBConverter.rgbIntToArray(cell);
                    // System.out.print("[" + rgb[0] + ", " + rgb[1] + ", " + rgb[2] + "] ");
                }
                // System.out.println();
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
        return mapData.length;
    }

    public int getHeight() {
        return mapData[0].length;
    }
}
