import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {

    private static final int LINE_SEGMENT_MIN_LENGTH = 3;
    private final List<LineSegment> lineSegments = new ArrayList<>();
    private final List<Point> smallerTip = new ArrayList<>();
    private final List<Point> biggerTip = new ArrayList<>();

    public FastCollinearPoints(final Point[] in) {
        validate(in);
        List<Point> points = Arrays.asList(in);
        if (in.length <= 1) return;
        for (Point point : points) {
            List<Point> aux = new ArrayList<>(points);
            // TODO: 
            //  do manual sorting here and create a new list of LineSlope which stores the slopes
            // then pass this list to searchSortedPoints()
            // this way you don't have to call slopeTo again in searchSortedPoints()
            aux.sort(point.slopeOrder());
            if (aux.get(1).slopeTo(point) == Double.NEGATIVE_INFINITY)
                throw new IllegalArgumentException("Equal points passed");
            searchSortedPoints(point, aux.subList(1, aux.size()));
        }
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

    private void searchSortedPoints(final Point anchorPoint, final List<Point> slopeSortedPoints) {
        List<Point> equalSlopes = new ArrayList<>();
        equalSlopes.add(slopeSortedPoints.get(0));
        double lastSlope = equalSlopes.get(0).slopeTo(anchorPoint);

        for (int i = 1; i < slopeSortedPoints.size(); i++) {
            Point currentPoint = slopeSortedPoints.get(i);
            // add points with equal slopes
            double currentSlope = currentPoint.slopeTo(anchorPoint);
            if (currentSlope == lastSlope) {
                equalSlopes.add(currentPoint);
            } else {
                // no more equal slopes, continue searching starting from current anchorPoint
                checkAndAddSegment(anchorPoint, equalSlopes);
                equalSlopes.clear();
                equalSlopes.add(currentPoint);
                lastSlope = currentSlope;
            }
        }
        checkAndAddSegment(anchorPoint, equalSlopes);
    }

    private void checkAndAddSegment(final Point point, final List<Point> equalSlopes) {
        if (equalSlopes.size() >= LINE_SEGMENT_MIN_LENGTH) {
            equalSlopes.add(point);
            equalSlopes.sort(Point::compareTo);
            boolean found = false;
            for (int i = 0; i < smallerTip.size(); i++) {
                if (smallerTip.get(i).compareTo(equalSlopes.get(0)) == 0) {
                    if (biggerTip.get(i).compareTo(equalSlopes.get(equalSlopes.size() - 1)) == 0) {
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                lineSegments.add(new LineSegment(equalSlopes.get(0),
                                                 equalSlopes.get(equalSlopes.size() - 1)));
                smallerTip.add(equalSlopes.get(0));
                biggerTip.add(equalSlopes.get(equalSlopes.size() - 1));
            }
        }
    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

    private class LineSlope implements Comparable<LineSlope> {
        private Point a;
        private Point b;
        private Double slope;

        public LineSlope(Point a, Point b) {
            this.a = a;
            this.b = b;
        }

        public void setSlope(Double slope) {
            this.slope = slope;
        }

        public Point getA() {
            return a;
        }

        public Point getB() {
            return b;
        }

        public int compareTo(LineSlope o) {
            final int compare = this.a.compareTo(o.a);
            return compare == 0 ? this.b.compareTo(o.b) : compare;
        }
    }
}