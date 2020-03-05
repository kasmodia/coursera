import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class BruteCollinearPoints {

    private final List<LineSegment> segments = new ArrayList<>();

    public BruteCollinearPoints(final Point[] points) {
        validate(points);
        Point[] aux = points.clone();
        sort(aux, new Point[aux.length], 0, aux.length - 1);
        findCollinearPoints(aux);
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
            if (i > mid)                           a[k] = aux[j++];
            else if (j > hi)                       a[k] = aux[i++];
            else if (aux[i] == null || aux[j] == null)  throw new IllegalArgumentException("Null point");
            else if (aux[i].compareTo(aux[j]) == 0)throw new IllegalArgumentException("Duplicated point");
            else if (aux[i].compareTo(aux[j]) < 0) a[k] = aux[i++];
            else                                   a[k] = aux[j++];
        }
    }

    private void findCollinearPoints(final Point[] aux) {
        final int size = aux.length;
        for (int p = 0; p < size - 3; p++) {
            validate(aux[p]);
            for (int q = p + 1; q < size - 2; q++) {
                validate(aux[q]);
                for (int r = q + 1; r < size - 1; r++) {
                    validate(aux[r]);
                    double slopePR = aux[p].slopeTo(aux[r]);
                    if (Double.compare(aux[p].slopeTo(aux[q]), slopePR) != 0) continue;
                    for (int s = r + 1; s < size; s++) {
                        validate(aux[s]);
                        if (Double.compare(slopePR, aux[p].slopeTo(aux[s])) == 0) {
                            segments.add(new LineSegment(aux[p], aux[s]));
                        }
                    }
                }
            }
        }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

    private void validate(final Object p) {
        if (p == null) throw new IllegalArgumentException("Object cannot be null");
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[segments.size()]);
    }
}