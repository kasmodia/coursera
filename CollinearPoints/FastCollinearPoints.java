import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class FastCollinearPoints {

    public static void main(String[] arvs) {
        Point[] points = new Point[]{
            new Point(1, 1),
            new Point(2, 2),
            new Point(3, 3),
            new Point(4, 4),
            new Point(5, 5),
            new Point(2, 3),
            new Point(5, 1),
            new Point(7, 4),
            new Point(0, 9),
            new Point(5, 5),
            new Point(-1, 1)
        };
        FastCollinearPoints fcp = new FastCollinearPoints(points);
        fcp.segments.forEach(seg -> System.out.println(seg.toString()));
    }

    final static int LINE_LENGTH = 4;
    ArrayList<LineSegment> segments = new ArrayList<>();
    ArrayList<Pair> pairs = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {
        for (int p = 0; p < points.length - 1; p++) {
            for (int q = p + 1; q < points.length; q++) {
                pairs.add(new Pair(points[p], points[q]));
            }
        }
        System.out.println("List before sorting>>>>>>>>>>> ");
        pairs.stream().forEach(item -> System.out.println(item.slope));
        Pair[] pairsArray = pairs.toArray(new Pair[0]);
        Arrays.sort(pairsArray);
        // sortPairs(new ArrayList<>(pairs), 0, pairs.size() - 1);
        System.out.println("List after sorting>>>>>>>>>>>> ");
        Stream.of(pairsArray).forEach(item -> System.out.println(item.slope));
        pairs = new ArrayList<Pair>(Arrays.asList(pairsArray));
        
        findCollinearPoints();
        System.out.println(segments);
    }

    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[segments.size()]);
    }

    private void sortPairs(ArrayList<Pair> aux, int low, int hi) {
        if (hi == low) return;
        int mid = (hi + low) / 2;
        sortPairs(aux, low, mid);
        sortPairs(aux, mid + 1, hi);
        merge(aux, low, mid, hi);
    }

    private void merge(ArrayList<Pair> aux, int low, int mid, int hi) {
        int left = low, right = mid + 1;
        for (int i = low; i <= hi; i++) {
            aux.set(i, pairs.get(i));
        }

        for (int k = low; k <= hi; k++) {
            if (left > mid)
                pairs.set(k, aux.get(right++));
            else if (right > hi) 
                pairs.set(k, aux.get(left++));
            else if (aux.get(right).compareTo(pairs.get(left)) <= 0) 
                pairs.set(k, aux.get(right++));
            else pairs.set(k, aux.get(left++));
        }
    }

    private void findCollinearPoints() {
        Pair starting = pairs.get(0);
        double lastSlope = 0;
        int counter = 0;
        for (int i = 1; i < pairs.size(); i++) {
            if (pairs.get(i).slope == lastSlope)
                ++counter;
            else {
                if (counter >= LINE_LENGTH) 
                    segments.add(new LineSegment(starting.p, pairs.get(i).q));
                
                starting = pairs.get(i);
                counter = 0;
                lastSlope = pairs.get(i).slope;
            }
        }
        if (counter >= LINE_LENGTH) 
            segments.add(new LineSegment(starting.p, pairs.get(pairs.size() - 1).q));
    }

    private static class Pair implements Comparable<Pair> {
        Point p;
        Point q;
        double slope;

        public Pair(Point p, Point q) {
            this.p = p;
            this.q = q;
            this.slope = p.slopeTo(q);
        }

        public int compareTo(Pair that) {
            return this.slope > that.slope ? 1 : this.slope < that.slope ? -1 : 0;
        }
    }
}