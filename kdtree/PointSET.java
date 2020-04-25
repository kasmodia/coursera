import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class PointSET {

    private TreeSet<Point2D> treeSet;

    public PointSET() {
        treeSet = new TreeSet<>(Point2D::compareTo);
    }

    public static void main(String[] args) {
        final List<Point2D> pointsList = Arrays.asList(
                new Point2D(2, 6),
                new Point2D(3, 4),
                new Point2D(4, 5),
                new Point2D(6, 5),
                new Point2D(6, 7),
                new Point2D(7, 6),
                new Point2D(8, 7),
                new Point2D(8, 9),
                new Point2D(10, 4),
                new Point2D(10, 8),
                new Point2D(12, 5)
        );
        PointSET set = new PointSET();
        pointsList.forEach(set::insert);
        RectHV rect = new RectHV(5, 3, 10, 8);
        final Iterable<Point2D> range = set.range(rect);
        Set<Point2D> expected = Set.of(new Point2D(6, 5),
                                       new Point2D(6, 7),
                                       new Point2D(7, 6),
                                       new Point2D(8, 7),
                                       new Point2D(10, 4),
                                       new Point2D(10, 8));
        if (!expected.equals(range))
            throw new RuntimeException("range test failed");
    }

    public boolean isEmpty() {
        return treeSet.isEmpty();
    }

    public int size() {
        return treeSet.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null point");
        treeSet.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null point");
        return treeSet.contains(p);
    }

    public void draw() {
        treeSet.forEach(Point2D::draw);
    }

    public Iterable<Point2D> range(RectHV rect) {
        return treeSet.stream()
                      .filter(point2D -> point2D.x() >= rect.xmin()
                              && point2D.y() >= rect.ymin()
                              && point2D.x() <= rect.xmax()
                              && point2D.y() <= rect.ymax())
                      .collect(Collectors.toSet());
    }

    public Point2D nearest(Point2D p) {
        return null;
    }
}