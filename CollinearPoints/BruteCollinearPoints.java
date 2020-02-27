import java.util.ArrayList;
import java.util.List;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {

    public static void main(String[] args) {

        // read the n points from a file
        
        int n = 4;
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = i;
            int y = i;
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

    List<LineSegment> segments = new ArrayList<>();
    public BruteCollinearPoints(Point[] points) {
        validate(points);
        int size = points.length;
        for (int p = 0; p < size - 3; p++) {
            validate(points[p]);
            for (int q = p + 1; q < size - 2; q++) {
                validate(points[q]);
                double slope1 = points[p].slopeTo(points[q]);
                for (int r = q + 1; r < size - 1; r++) {
                    validate(points[r]);
                    if (slope1 != points[p].slopeTo(points[r])) continue;
                    for (int s = r + 1; s < size; s++) {
                        validate(points[s]);
                        if (points[p].slopeTo(points[q]) == points[p].slopeTo(points[r])
                         && points[p].slopeTo(points[r]) == points[p].slopeTo(points[s]))
                        segments.add(new LineSegment(points[p], points[s]));
                    }
                }
            }
        }
    }

    private void validate(Object p) {
        if (p == null) throw new IllegalArgumentException("Object cannot be null");
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[segments.size()]);
    }
}