import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double zValue = 1.96;
    private final double[] results;
    private final int size;

    // test client (see below)
    public static void main(final String[] args) {
        int n, t;
        try {
            if (args.length != 2)
                throw new NumberFormatException();
            n = Integer.parseInt(args[0]);
            t = Integer.parseInt(args[1]);
        } catch (final NumberFormatException ex) {
            throw new IllegalArgumentException("You must pass 2 integers, size and trials", ex);
        }

        final PercolationStats stats = new PercolationStats(n, t);
        System.out.println("mean\t\t\t\t= " + stats.mean());
        System.out.println("stddev\t\t\t\t= " + stats.stddev());
        System.out.println("95% confidence interval\t\t= [" + 
                            stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }

    // perform independent trials on an n-by-n grid
    public PercolationStats(final int n, final int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials must be greater than 0");
        }

        size = n;
        results = new double[trials];
        for (int x = 0; x < trials; x++) {
            results[x] = getResult() / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (zValue * (stddev() / Math.sqrt(size)));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (zValue * (stddev() / Math.sqrt(size)));
    }

    private double getResult() {
        final Percolation p = new Percolation(size);
        // create random sites
        final int[][] randoms = new int[size * size][2];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                randoms[(i * size) + j] = new int[] { i + 1, j + 1 };
            }
        }

        StdRandom.shuffle(randoms);

        for (int i = 0; i < randoms.length; i++) {
            p.open(randoms[i][0], randoms[i][1]);
            if (p.percolates()) {
                break;
            }
        }
        return p.numberOfOpenSites();
    }
}