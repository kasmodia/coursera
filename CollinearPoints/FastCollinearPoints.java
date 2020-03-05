import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FastCollinearPoints {

    private final ArrayList<LineSegment> segments = new ArrayList<>();

    public FastCollinearPoints(final Point[] points) {
        validate(points);
        findCollinearPoints(points);
    }

    private void validate(Object p) {
        if (p == null) throw new IllegalArgumentException("Object cannot be null");
    }

    public static void main(final String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
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
        return segments.toArray(new LineSegment[segments.size()]);
    }

    private void sort(Point[] points, Point[] aux, int low, int hi) {
        if (hi <= low) return;
        int mid = low + (hi - low) / 2;
        sort(points, aux, low, mid);
        sort(points, aux, mid + 1, hi);
        merge(points, aux, low, hi, mid);
    }

    private void merge(Point[] a, Point[] aux, int low, int hi, int mid) {
        int i = low, j = mid + 1;
        System.arraycopy(a, low, aux, low, hi - low + 1);

        for (int k = low; k <= hi; k++) {
            if (i > mid) a[k] = aux[j++];
            else if (j > hi) a[k] = aux[i++];
            else if (aux[i] == null || aux[j] == null)
                throw new IllegalArgumentException("Null point");
            else if (aux[i].compareTo(aux[j]) == 0)
                throw new IllegalArgumentException("Duplicated point");
            else if (aux[i].compareTo(aux[j]) < 0) a[k] = aux[i++];
            else a[k] = aux[j++];
        }
    }

    private void findCollinearPoints(Point[] points) {
        Point[] aux = points.clone();
        for (int p = 0; p < aux.length - 3; p++) {
            final List<Point> sortedList = Arrays.asList(aux);
            System.out.println("Point p: " + aux[p]);
            System.out.println("points sorted according to slopes with p: ");
            Collections.sort(sortedList, aux[p].slopeOrder());
            System.out.println(sortedList);
            int finalP = p;
            sortedList.stream().forEach(point -> System.out.print(", " + aux[finalP].slopeTo(point)));
            System.out.println();
            checkSegments(aux[p], sortedList);
        }
    }

    private void checkSegments(Point p, final List<Point> otherPointsSortedBySlope) {
        int i = 1, match = 0;
        while (i < otherPointsSortedBySlope.size()) {
            if (p.compareTo(otherPointsSortedBySlope.get(i)) == 0) {
                match = 0;
                i++;
                continue;
            }
            if (p.slopeTo(otherPointsSortedBySlope.get(i)) == p
                    .slopeTo(otherPointsSortedBySlope.get(i - 1))) {
                match++;
            }
            else {
                if (match >= 3) {
                    segments.add(new LineSegment(p, otherPointsSortedBySlope.get(i)));
                    match = 0;
                    i++;
                    break;
                }
                match = 0;
            }
            i++;
        }
        if (match >= 3) {
            segments.add(new LineSegment(p, otherPointsSortedBySlope.get(i)));
        }
    }

    public int numberOfSegments() {
        return segments.size();
    }
}