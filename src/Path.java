import java.awt.Point;
import java.util.List;

public class Path {
    private List<Point> points;

    public Path() {
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }

    public boolean isEmpty() {
        return points == null || points.isEmpty();
    }

    public Point get(int index) {
        return points.get(index);
    }
}
