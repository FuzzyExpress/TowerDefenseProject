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
    
    /**
     * Finds the path from the SPAWNER to the END tile
     * @return List of Points representing the path
     */
    public List<Point> findPath() {
        // Find start and end points
        Point start = findTile(Tiles.SPAWNER);
        Point end = findTile(Tiles.END);
        
        if (start == null || end == null) {
            System.out.println("Error: Could not find start or end point on the map");
            return new ArrayList<>();
        }
        
        // The set of visited nodes
        Set<Point> closedSet = new HashSet<>();
        
        // The set of discovered nodes that need to be evaluated
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.fCost));
        openSet.add(new Node(start, null, 0, estimateDistance(start, end)));
        
        // Maps a point to its parent node for path reconstruction
        Map<Point, Node> cameFrom = new HashMap<>();
        
        // Contains the current best known cost to reach each point
        Map<Point, Integer> gScore = new HashMap<>();
        gScore.put(start, 0);
        
        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            
            // If we've reached the end, reconstruct and return the path
            if (current.position.equals(end)) {
                return reconstructPath(current);
            }
            
            closedSet.add(current.position);
            
            // Check each neighboring tile
            int[][] directions = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}}; // N, E, S, W
            for (int[] dir : directions) {
                int nx = current.position.x + dir[0];
                int ny = current.position.y + dir[1];
                Point neighbor = new Point(nx, ny);
                
                // Skip if out of bounds or already evaluated
                if (nx < 0 || ny < 0 || nx >= width || ny >= height || closedSet.contains(neighbor)) {
                    continue;
                }
                
                // Check if the neighbor is a valid tile to move on
                Tiles neighborTile = TileMapper.GetTile(mapData[nx][ny]);
                if (neighborTile != Tiles.PATH && neighborTile != Tiles.END && neighborTile != Tiles.SPAWNER) {
                    continue;
                }
                
                // Calculate tentative gScore
                int tentativeGScore = gScore.getOrDefault(current.position, Integer.MAX_VALUE) + 1;
                
                // If this path to neighbor is better than any previous one, record it
                if (tentativeGScore < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    Node neighborNode = new Node(
                        neighbor, 
                        current, 
                        tentativeGScore, 
                        estimateDistance(neighbor, end)
                    );
                    
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    
                    // Add to open set if not already there
                    boolean found = false;
                    for (Node node : openSet) {
                        if (node.position.equals(neighbor)) {
                            found = true;
                            // Update node if this path is better
                            if (tentativeGScore < node.gCost) {
                                openSet.remove(node);
                                openSet.add(neighborNode);
                            }
                            break;
                        }
                    }
                    
                    if (!found) {
                        openSet.add(neighborNode);
                    }
                }
            }
        }
        
        // No path found
        System.out.println("No path found from spawner to end");
        return new ArrayList<>();
    }
    
    private List<Point> reconstructPath(Node endNode) {
        List<Point> path = new ArrayList<>();
        Node current = endNode;
        
        while (current != null) {
            path.add(0, current.position); // Add to beginning of list
            current = current.parent;
        }
        
        return path;
    }
    
    private int estimateDistance(Point a, Point b) {
        // Manhattan distance
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
    
    /**
     * Node class for A* algorithm
     */
    private static class Node {
        Point position;
        Node parent;
        int gCost; // Cost from start to this node
        int hCost; // Estimated cost from this node to end
        int fCost; // gCost + hCost
        
        public Node(Point position, Node parent, int gCost, int hCost) {
            this.position = position;
            this.parent = parent;
            this.gCost = gCost;
            this.hCost = hCost;
            this.fCost = gCost + hCost;
        }
    }
} 