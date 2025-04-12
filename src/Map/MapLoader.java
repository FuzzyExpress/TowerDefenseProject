package Map;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Point;
import java.util.*;
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

    public List<Point> generatePath() {
        List<Point> path = new ArrayList<>();
        Point start = findTile(Tiles.SPAWNER);
        if (start == null) return path;

        Set<Point> visited = new HashSet<>();
        Queue<Point> queue = new LinkedList<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            if (visited.contains(current)) continue;
            visited.add(current);
            path.add(current);

            int[][] directions = {{0,-1},{0,1},{-1,0},{1,0}};
            for (int[] dir : directions) {
                int nx = current.x + dir[0];
                int ny = current.y + dir[1];
                if (nx >= 0 && ny >= 0 && nx < getWidth() && ny < getHeight()) {
                    Tiles neighbor = TileMapper.GetTile(mapData[nx][ny]);
                    if ((neighbor == Tiles.PATH || neighbor == Tiles.END) && !visited.contains(new Point(nx, ny))) {
                        queue.add(new Point(nx, ny));
                    }
                }
            }
        }

        return path;
    }

    private Point findTile(Tiles target) {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                if (TileMapper.GetTile(mapData[x][y]) == target) {
                    return new Point(x, y);
                }
            }
        }
        return null;
    }
}
