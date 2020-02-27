import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board implements Comparable<Board> {

    private int[][] tiles;
    private int n;
    private int moves;
    private Board parent;
    private int hamming = -1;
    private int manhattan = -1;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
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

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    // number of tiles out of place
    public int hamming() {
        if (hamming != -1)
            return hamming;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                final int expectedValue = expectedValue(i, j);
                if (expectedValue == n * n) continue;
                if (tiles[i][j] != expectedValue)
                    hamming++;
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (manhattan != -1)
            return manhattan;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int expectedValue = expectedValue(i, j);
                if (expectedValue == n * n) continue;
                if (tiles[i][j] != expectedValue) {
                    manhattan += searchForValue(i, j);
                }
            }
        }
        return manhattan;
    }

    private int searchForValue(int i, int j) {
        int expectedValue = expectedValue(i, j);
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if (tiles[x][y] == expectedValue)
                    return Math.abs(x - i) + Math.abs(y - j);
            }
        }

        // not found, search backwards
        for (int x = 0; x >= 0; x--) {
            for (int y = 0; y >= 0; y--) {
                if (tiles[x][y] == expectedValue)
                    return Math.abs(x - i) + Math.abs(y - j);
            }
        }
        return 0;
    }

    private int expectedValue(int i, int j) {
        return (i * n) + j + 1;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                final int expectedValue = expectedValue(i, j);
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
        int[] empty = new int[0];
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (tiles[i][j] == 0) {
                    empty = new int[] { i, j };
                    break;
                }
            }
        }

        List<Board> result = new ArrayList<>();
        final int[][] a1 = exchange(empty[0], empty[1], empty[0] - 1, empty[1]);
        if (a1 != null) setParentAndAddNeighbor(result, a1);
        final int[][] a2 = exchange(empty[0], empty[1], empty[0] + 1, empty[1]);
        if (a2 != null) setParentAndAddNeighbor(result, a2);
        final int[][] a3 = exchange(empty[0], empty[1], empty[0], empty[1] - 1);
        if (a3 != null) setParentAndAddNeighbor(result, a3);
        final int[][] a4 = exchange(empty[0], empty[1], empty[0], empty[1] + 1);
        if (a4 != null) setParentAndAddNeighbor(result, a4);
        return result;
    }

    private void setParentAndAddNeighbor(List<Board> result, int[][] ar) {
        final Board child = new Board(ar);
        child.setParent(this);
        result.add(child);
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
            i++;
        }

        if (this.tiles[i][j] == 0) {
            i++;
        }
        return new Board(exchange(x, y, i, j));
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        testInput();
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

    private static void testInput() {
        boolean expected = false;
        try {
            new Board(new int[][] {
                    { 0, 1, 3, 9 }, { 4, 0, 2 }, { 7, 6, 5 }
            });
        }
        catch (IllegalArgumentException ex) {
            expected = true;
        }

        if (!expected)
            throw new IllegalArgumentException("Input test failed");
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

    public int compareTo(Board that) {
        return Integer.compare(this.manhattan(), that.manhattan());
    }

    public Board getParent() {
        return parent;
    }

    public void setParent(Board parent) {
        this.parent = parent;
    }
}