package Map;

import java.awt.Point;
import java.util.*;

public class PathFinder {
    private int[][] mapData;
    private int width;
    private int height;

    public PathFinder(int[][] mapData, int width, int height) {
        this.mapData = mapData;
        this.width = width;
        this.height = height;
    }

    public List<Point> findPath() {
        Point start = findTile(Tiles.SPAWNER);
        Point end = findTile(Tiles.END);

        if (start == null || end == null) {
            System.out.println("❌ Could not find SPAWNER or END tile.");
            return new ArrayList<>();
        }

        Set<Point> closedSet = new HashSet<>();
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.fCost));
        openSet.add(new Node(start, null, 0, estimateDistance(start, end)));
        Map<Point, Integer> gScore = new HashMap<>();
        gScore.put(start, 0);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            if (current.position.equals(end)) {
                return reconstructPath(current);
            }

            closedSet.add(current.position);
            int[][] dirs = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};

            for (int[] dir : dirs) {
                int nx = current.position.x + dir[0];
                int ny = current.position.y + dir[1];

                if (nx < 0 || ny < 0 || nx >= width || ny >= height) continue;

                Point neighbor = new Point(nx, ny);
                if (closedSet.contains(neighbor)) continue;

                Tiles tile = TileMapper.GetTile(mapData[nx][ny]);
                if (tile != Tiles.PATH && tile != Tiles.SPAWNER && tile != Tiles.END) continue;

                int tentativeG = gScore.get(current.position) + 1;
                if (tentativeG < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    gScore.put(neighbor, tentativeG);
                    openSet.add(new Node(neighbor, current, tentativeG, estimateDistance(neighbor, end)));
                }
            }
        }

        System.out.println("❌ Path not found.");
        return new ArrayList<>();
    }

    private List<Point> reconstructPath(Node endNode) {
        List<Point> path = new ArrayList<>();
        for (Node curr = endNode; curr != null; curr = curr.parent) {
            path.add(0, curr.position);
        }
        return path;
    }

    private int estimateDistance(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private Point findTile(Tiles target) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (TileMapper.GetTile(mapData[x][y]) == target) {
                    return new Point(x, y);
                }
            }
        }
        return null;
    }

    private static class Node {
        Point position;
        Node parent;
        int gCost;
        int hCost;
        int fCost;

        Node(Point position, Node parent, int gCost, int hCost) {
            this.position = position;
            this.parent = parent;
            this.gCost = gCost;
            this.hCost = hCost;
            this.fCost = gCost + hCost;
        }
    }
}
