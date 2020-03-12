import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FastCollinearPoints {

    private static final int LINE_SEGMENT_MIN_LENGTH = 3;
    private final ArrayList<LineSegment> lineSegments = new ArrayList<>();

    public FastCollinearPoints(final Point[] points) {
        validate(points);
        findCollinearPoints(points);
    }

    private void validate(final Point[] a) {
        if (a == null) throw new IllegalArgumentException("Object cannot be null");
        for (Point p : a)
            if (p == null) throw new IllegalArgumentException("Object cannot be null");
    }

    public static void main(final String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            if (x < 0 || x > 32767)
                throw new IllegalArgumentException("value must be <0 - 32,767>");
            int y = in.readInt();
            if (y < 0 || y > 32767)
                throw new IllegalArgumentException("value must be <0 - 32,767>");
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[] { });
    }

    private void findCollinearPoints(Point[] points) {
        Point[] aux = points.clone();
        for (Point point : aux) {
            final List<Point> slopeSortedPoints = new ArrayList<Point>(Arrays.asList(aux));
            slopeSortedPoints.sort(point.slopeOrder());
            searchSortedPoints(point, slopeSortedPoints.subList(1, slopeSortedPoints.size()));
        }
    }

    private void searchSortedPoints(final Point point, final List<Point> slopeSortedPoints) {
        List<Point> equalSlopes = new ArrayList<>();
        if (slopeSortedPoints.get(0).slopeTo(point) == Double.NEGATIVE_INFINITY)
            throw new IllegalArgumentException("Equal points passed");
        equalSlopes.add(slopeSortedPoints.get(0));

        for (int i = 1; i < slopeSortedPoints.size(); i++) {
            Point slopeSortedPoint = slopeSortedPoints.get(i);
            // add points with equal slopes
            if (slopeSortedPoint.slopeTo(point) == equalSlopes.get(0).slopeTo(point)) {
                equalSlopes.add(slopeSortedPoint);
            }
            else {
                // no more equal slopes, continue searching starting from current point
                checkAndAddSegment(point, equalSlopes);
                equalSlopes.clear();
                equalSlopes.add(slopeSortedPoint);
            }
        }
        checkAndAddSegment(point, equalSlopes);
    }

    private void checkAndAddSegment(final Point point, final List<Point> equalSlopes) {
        if (equalSlopes.size() >= LINE_SEGMENT_MIN_LENGTH) {
            equalSlopes.add(point);
            equalSlopes.sort(pointsComparator());
            LineSegment newSegment =
                    new LineSegment(equalSlopes.get(0),
                                    equalSlopes.get(equalSlopes.size() - 1));
            if (lineSegments.stream().noneMatch(
                    segment -> segment.toString().equals(newSegment.toString())))
                lineSegments.add(newSegment);
        }
    }

    private Comparator<Point> pointsComparator() {
        return Point::compareTo;
    }

    public int numberOfSegments() {
        return lineSegments.size();
    }
}