import edu.princeton.cs.algs4.StdRandom;

public class Percolation {

    private final int[] grid;
    private final int size;
    private int numberOfOpenSites;

    public static void main(final String[] args) {
        final int size = 1000;
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
                return;
            }
        }
    }

    public Percolation(final int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Must contain at least 1 site");
        }
        size = n;
        grid = new int[n + 2];
        for (int i = 0; i < grid.length; i++) {
            grid[i] = -1;
        }
        // set virtual bottom site's root as it's own
        grid[0] = 0;
        grid[size + 1] = size + 1;
    }

    public void open(final int x, final int y) {
        validate(x, y);
        if (isOpen(x, y))
            return;

        convert(x, y);
        grid[x][y] = new int[] { x, y };
        ++numberOfOpenSites;
        exploreAndUnion(x, y);
    }

    private int convert(int x, int y) {
        
    }

    public boolean isOpen(final int x, final int y) {
        validate(x, y);
        return grid[x][y] != null;
    }

    public boolean isFull(final int x, final int y) {
        validate(x, y);
        // True if root is at the first row
        final int[] root = find(x, y);
        return root != null && root[1] == 0;
    }

    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // if the two virtual sites are connected then the system percolates
    public boolean percolates() {
        return connected(0, 0, 0, size);
    }

    private boolean connected(final int aX, final int aY, final int bX, final int bY) {
        final int[] aRoot = find(aX, aY);
        final int[] bRoot = find(bX, bY);
        return aRoot[0] == bRoot[0] && aRoot[1] == bRoot[1];
    }

    private void exploreAndUnion(final int x, final int y) {
        // union with top site if at the first row
        if (x == 1) {
            grid[x][y][0] = 0;
            grid[x][y][1] = 0;
        }
        // union with bottom site if at the bottom row
        if (x == size) {
            union(x, y, 0, size);
        }
        // right
        if (x < size && isOpen(x + 1, y))
            union(x, y, x + 1, y);
        // left
        if (x > 1 && isOpen(x - 1, y))
            union(x, y, x - 1, y);
        // up
        if (y > 1 && isOpen(x, y - 1))
            union(x, y, x, y - 1);
        // down
        if (y < size && isOpen(x, y + 1))
            union(x, y, x, y + 1);
    }

    private void union(final int aX, final int aY, final int bX, final int bY) {
        // Y of a is less than Y of b, set b's root to a's root
        final int[] rootA = find(aX, aY);
        final int[] rootB = find(bX, bY);
        if (rootA[1] < rootB[1]) {
            grid[rootB[0]][rootB[1]][0] = rootA[0];
            grid[rootB[0]][rootB[1]][1] = rootA[1];
        } else {
            grid[rootA[0]][rootA[1]][0] = rootB[0];
            grid[rootA[0]][rootA[1]][1] = rootB[1];
        }
    }

    private int[] find(final int x, final int y) {
        if (grid[x][y] == null)
            return null;
        final int[] site = {x, y};
        int[] parent = {grid[x][y][0], grid[x][y][1]};
        while (site[0] != parent[0] || site[1] != parent[1]) {
            grid[site[0]][site[1]] = grid[parent[0]][parent[1]];
            site[0] = parent[0];
            site[1] = parent[1];
            parent = grid[site[0]][site[1]];
        }
        return site;
    }

    private void validate(final int x, final int y) {
        if (x <= 0 || y <= 0) {
            throw new IllegalArgumentException("X and Y must be greater than 0");
        }
    }
}