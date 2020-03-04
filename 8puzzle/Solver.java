/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private static Comparator<Node> comparator = (o1, o2) -> {
        if (o1 == null) return 1;
        if (o2 == null) return -1;
        if (o1.getPriority() == o2.getPriority())
            return Integer.compare(o1.hamming, o2.hamming);
        else
            return Integer.compare(o1.getPriority(), o2.getPriority());
    };
    private boolean solvable;
    private Node lastMin;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(final Board initial) {
        if (initial == null) throw new IllegalArgumentException("Null argument");
        MinPQ<Node> minPQ = new MinPQ<>(comparator);
        minPQ.insert(new Node(initial));
        MinPQ<Node> twinMinPQ = new MinPQ<>(comparator);
        twinMinPQ.insert(new Node(initial.twin()));
        solve(minPQ, twinMinPQ);
    }

    public static void main(String[] args) {
        testComparator();

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    private static void testComparator() {
        Board a = new Board(new int[][] {
                { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 }
        });
        Board b = new Board(new int[][] {
                { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 }
        });
        Board c = new Board(new int[][] {
                { 0, 1, 3 }, { 2, 0, 4 }, { 5, 6, 7 }
        });

        if (comparator.compare(new Node(a), new Node(b)) != 0)
            throw new IllegalArgumentException("Equal test failed");
        if (comparator.compare(new Node(a), new Node(c)) != -1)
            throw new IllegalArgumentException("Equal test failed");
        if (comparator.compare(new Node(a), null) == 0)
            throw new IllegalArgumentException("Equal test failed");
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return solvable ? buildResultPath(lastMin).size() - 1 : -1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return solvable ? buildResultPath(lastMin) : null;
    }

    private void solve(final MinPQ<Node> minPQ, final MinPQ<Node> tMinPQ) {
        Node min = minPQ.delMin();
        Node tmin = tMinPQ.delMin();
        while (!min.board.isGoal() && !tmin.board.isGoal()) {
            min = addNeighbors(min, minPQ);
            tmin = addNeighbors(tmin, tMinPQ);
        }
        solvable = min.board.isGoal();
        if (solvable) lastMin = min;
    }

    private Node addNeighbors(final Node currentMinNode, final MinPQ<Node> pq) {
        for (Board neighbor : currentMinNode.board.neighbors()) {
            if (currentMinNode.parent == null || !neighbor.equals(currentMinNode.parent.board)) {
                Node neighborNode = new Node(neighbor);
                neighborNode.parent = currentMinNode;
                neighborNode.moves = currentMinNode.moves + 1;
                pq.insert(neighborNode);
            }
        }
        return pq.delMin();
    }

    private Stack<Board> buildResultPath(Node currentNode) {
        Stack<Board> solution = new Stack<>();
        solution.push(currentNode.board);
        currentNode = currentNode.parent;
        while (currentNode != null) {
            solution.push(currentNode.board);
            currentNode = currentNode.parent;
        }
        return solution;
    }

    private static class Node {
        private Board board;
        private Node parent;
        private int moves;
        private int manhattan;
        private int hamming;

        public Node(Board board) {
            this.board = board;
            this.manhattan = board.manhattan();
            this.hamming = board.hamming();
        }

        public int getPriority() {
            return manhattan + moves;
        }
    }
}
