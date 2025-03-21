import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Path {
    private List<Point> pathPoints;

    public Path() {
        pathPoints = new ArrayList<>();
        // Example: Define a simple horizontal path
        pathPoints.add(new Point(0, 300));
        pathPoints.add(new Point(800, 300));
    }

    public List<Point> getPathPoints() {
        return pathPoints;
    }
}
