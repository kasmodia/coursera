import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

    private int[][] tiles;
    private int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(final int[][] tiles) {
        if (tiles == null) throw new IllegalArgumentException("Empty array");
        int[][] clone = tiles.clone();
        for (int i = 0; i < tiles.length; i++) {
            clone[i] = tiles[i].clone();
        }
        this.tiles = clone;
        n = tiles.length;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                final int goalValue = goalValue(i, j);
                if (goalValue == n * n) continue;
                if (tiles[i][j] != goalValue)
                    hamming++;
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int goalValue = goalValue(i, j);
                if (goalValue == n * n) continue;
                if (tiles[i][j] != goalValue) {
                    manhattan += searchForValue(i, j);
                }
            }
        }
        return manhattan;
    }

    private int searchForValue(int i, int j) {
        int goalValue = goalValue(i, j);
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if (tiles[x][y] == goalValue)
                    return Math.abs(x - i) + Math.abs(y - j);
            }
        }

        // not found, search backwards
        for (int x = 0; x >= 0; x--) {
            for (int y = 0; y >= 0; y--) {
                if (tiles[x][y] == goalValue)
                    return Math.abs(x - i) + Math.abs(y - j);
            }
        }
        return 0;
    }

    private int goalValue(int i, int j) {
        return (i * n) + j + 1;
    }

    // is this board the goal board?
    public boolean isGoal() {
        if (tiles[n -1][n - 1] != 0)
            return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                final int expectedValue = goalValue(i, j);
                if (expectedValue == n * n) continue;
                if (tiles[i][j] != expectedValue)
                    return false;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || !y.getClass().equals(this.getClass()))
            return false;

        Board that = (Board) y;

        if (that.dimension() != this.dimension())
            return false;

        return Arrays.deepEquals(this.tiles, that.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        // find empty tile
        return findNeighbors(findZero());
    }

    private int[] findZero() {
        int[] zero = new int[0];
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (tiles[i][j] == 0) {
                    zero = new int[] { i, j };
                    break;
                }
            }
        }
        return zero;
    }

    private List<Board> findNeighbors(final int[] zero) {
        final ArrayList<Board> neighbors = new ArrayList<>();
        final int[][] a1 = exchange(zero[0], zero[1], zero[0] - 1, zero[1]);
        if (a1 != null) neighbors.add(new Board(a1));
        final int[][] a2 = exchange(zero[0], zero[1], zero[0] + 1, zero[1]);
        if (a2 != null) neighbors.add(new Board(a2));
        final int[][] a3 = exchange(zero[0], zero[1], zero[0], zero[1] - 1);
        if (a3 != null) neighbors.add(new Board(a3));
        final int[][] a4 = exchange(zero[0], zero[1], zero[0], zero[1] + 1);
        if (a4 != null) neighbors.add(new Board(a4));
        return neighbors;
    }

    private int[][] exchange(int x, int y, int i, int j) {
        if (i < 0 || i >= n || j < 0 || j >= n)
            return null;
        final int[][] clone = tiles.clone();
        for (int k = 0; k < tiles.length; k++) {
            clone[k] = tiles[k].clone();
        }
        int t = clone[x][y];
        clone[x][y] = clone[i][j];
        clone[i][j] = t;
        return clone;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int x = 0;
        int y = 0;
        int i = 1;
        int j = 1;
        if (this.tiles[x][y] == 0) {
            x++;
        }

        if (this.tiles[i][j] == 0) {
            i--;
        }
        return new Board(exchange(x, y, i, j));
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        testEquals();
        testHamming();
        testManhattan();
        testNeighbors();
        testTwin();
    }

    private static void testTwin() {
        if (createRandomBoard().equals(createRandomBoard().twin()))
            throw new IllegalArgumentException("Twin test failed");
        System.out.println(createRandomBoard());
        System.out.println("Twin:");
        System.out.println(createRandomBoard().twin());
        System.out.println(createAnotherRandomBoard());
        System.out.println("Twin:");
        System.out.println(createAnotherRandomBoard().twin());
        final Board board = new Board(new int[][] { { 1, 0, 9 }, { 4, 3, 2 }, { 7, 6, 5 } });
        System.out.println(board);
        System.out.println(board.twin());
    }

    private static void testNeighbors() {
        final Board board = createRandomBoard();
        System.out.println(board);
        final Iterable<Board> neighbors = board.neighbors();
        if (neighbors == null) {
            throw new IllegalArgumentException("neighbors test failed");
        }
        int size = 0;
        for (Board neighbor : neighbors) {
            if (neighbor == null) {
                throw new IllegalArgumentException("neighbors test failed");
            }
            size++;
            System.out.println(neighbor);
        }
        if (size != 4)
            throw new IllegalArgumentException("neighbors test failed");
    }

    private static void testEquals() {
        Board a = createRandomBoard();
        Board b = createRandomBoard();
        Board c = new Board(new int[][] {
                { 0, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 }
        });
        if (!a.equals(b))
            throw new IllegalArgumentException("Equal test failed");
        if (a.equals(c))
            throw new IllegalArgumentException("Equal test failed");
        if (a.equals(null))
            throw new IllegalArgumentException("Equal test failed");
    }

    private static Board createRandomBoard() {
        return new Board(new int[][] {
                { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 }
        });
    }

    private static Board createAnotherRandomBoard() {
        return new Board(new int[][] {
                { 0, 1, 3 }, { 4, 8, 2 }, { 7, 6, 5 }
        });
    }

    private static void testManhattan() {
        Board b = createRandomBoard();
        if (b.manhattan() != 10)
            throw new IllegalArgumentException(
                    "Manhattan test failed. Expected 10, found " + b.manhattan());
    }

    private static void testHamming() {
        Board b = createRandomBoard();
        if (b.hamming() != 5)
            throw new IllegalArgumentException("Hamming test failed");
    }
}