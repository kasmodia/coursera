import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {

    private static final int LINE_SEGMENT_MIN_LENGTH = 3;
    private final List<LineSegment> lineSegments = new ArrayList<>();
    private final List<LineSlope> smallerTip = new ArrayList<>();
    private final List<LineSlope> biggerTip = new ArrayList<>();

    public FastCollinearPoints(final Point[] in) {
        validate(in);
        List<Point> points = Arrays.asList(in);
        if (in.length <= 1) return;
        for (Point point : points) {
            List<Point> aux = new ArrayList<>(points);
            aux.sort(point.slopeOrder());
            // LineSlope[] slopes = new LineSlope[points.size()];
            // sort(aux, point, slopes);
            if (slopes[1].getSlope() == Double.NEGATIVE_INFINITY)
                throw new IllegalArgumentException("Equal points passed");
            searchSortedPoints(point, Arrays.asList(slopes).subList(1, slopes.length));
        }
    }

    private void sort(List<Point> otherPoints, Point anchorPoint, LineSlope[] slopesToAnchor) {
        mergesort(otherPoints, new ArrayList<Point>(otherPoints), anchorPoint, slopesToAnchor, 0,
                  otherPoints.size() - 1);
    }

    private void mergesort(List<Point> otherPoints, List<Point> aux,
                           Point anchorPoint, LineSlope[] slopesToAnchor, int low, int hi) {
        if (low < hi) {
            int mid = (low + hi) / 2;
            mergesort(otherPoints, aux, anchorPoint, slopesToAnchor, low, mid);
            mergesort(otherPoints, aux, anchorPoint, slopesToAnchor, mid + 1, hi);
            merge(otherPoints, aux, anchorPoint, slopesToAnchor, low, mid, hi);
        }
    }

    private void merge(List<Point> otherPoints, List<Point> aux,
                       Point anchorPoint, LineSlope[] slopesToAnchor, int low, int mid,
                       int hi) {
        int i = low, j = mid + 1;
        for (int k = low; k <= hi; k++) {
            aux.set(k, otherPoints.get(k));
        }
        for (int k = low; k <= hi; k++) {
            if (i > mid) {
                otherPoints.set(k, aux.get(j));
                slopesToAnchor[k] = new LineSlope(aux.get(j), anchorPoint.slopeTo(aux.get(j++)));
            }
            else if (j > hi) {
                otherPoints.set(k, aux.get(i));
                slopesToAnchor[k] = new LineSlope(aux.get(i), anchorPoint.slopeTo(aux.get(i++)));
            }
            else {
                final double slopToI = anchorPoint.slopeTo(aux.get(i));
                final double slopeToJ = anchorPoint.slopeTo(aux.get(j));
                if (slopToI < slopeToJ) {
                    otherPoints.set(k, aux.get(i));
                    slopesToAnchor[k] = new LineSlope(aux.get(i++), slopToI);
                }
                else {
                    otherPoints.set(k, aux.get(j));
                    slopesToAnchor[k] = new LineSlope(aux.get(j++), slopeToJ);
                }
            }
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

    private void searchSortedPoints(final Point anchorPoint, final List<LineSlope> sortedSlopes) {
        List<LineSlope> equalSlopes = new ArrayList<>();
        equalSlopes.add(sortedSlopes.get(0));
        double lastSlope = equalSlopes.get(0).getSlope();

        for (int i = 1; i < sortedSlopes.size(); i++) {
            LineSlope currentPoint = sortedSlopes.get(i);
            // add points with equal slopes
            double currentSlope = currentPoint.getSlope();
            if (currentSlope == lastSlope) {
                equalSlopes.add(currentPoint);
            }
            else {
                // no more equal slopes, continue searching starting from current anchorPoint
                checkAndAddSegment(anchorPoint, equalSlopes);
                equalSlopes.clear();
                equalSlopes.add(currentPoint);
                lastSlope = currentSlope;
            }
        }
        checkAndAddSegment(anchorPoint, equalSlopes);
    }

    private void checkAndAddSegment(final Point point, final List<LineSlope> equalSlopes) {
        if (equalSlopes.size() >= LINE_SEGMENT_MIN_LENGTH) {
            equalSlopes.add(new LineSlope(point, Double.NEGATIVE_INFINITY));
            equalSlopes.sort(LineSlope::compareTo);
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
                lineSegments.add(new LineSegment(equalSlopes.get(0).getA(),
                                                 equalSlopes.get(equalSlopes.size() - 1).getA()));
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
        private Double slope;

        public LineSlope(Point a, Double slope) {
            this.a = a;
            this.slope = slope;
        }

        public Point getA() {
            return a;
        }

        public Double getSlope() {
            return slope;
        }

        public int compareTo(LineSlope o) {
            return this.a.compareTo(o.a);
        }
    }
}