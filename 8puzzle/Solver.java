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
import java.util.Objects;

public class Solver {

    private static Comparator<Node> comparator = (Node o1, Node o2) -> {
        if (o1 == null) return 1;
        if (o2 == null) return -1;
        return Integer.compare(o1.board.manhattan() + o1.moves, o2.board.manhattan() + o2.moves);
    };
    private Board goal;
    private Stack<Board> solution = new Stack<>();
    private boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(final Board initial) {
        init(initial);
        MinPQ<Node> minPQ = new MinPQ<>(comparator);
        minPQ.insert(new Node(initial));
        MinPQ<Node> tMinPQ = new MinPQ<>(comparator);
        tMinPQ.insert(new Node(initial.twin()));
        solve(minPQ, tMinPQ);
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

    public int hashCode() {
        return Objects.hash(goal);
    }

    private void init(final Board initial) {
        if (initial == null) throw new IllegalArgumentException("Null argument");
        initGoal(initial);
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
        return solution.isEmpty() ? 0 : solution.size() - 1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return solution.isEmpty() ? null : solution;
    }

    private void solve(final MinPQ<Node> minPQ, final MinPQ<Node> tMinPQ) {
        Node min = minPQ.delMin();
        Node tmin = tMinPQ.delMin();
        while (!min.board.equals(goal) && !tmin.board.equals(goal)) {
            min = addNeighbors(min, minPQ);
            tmin = addNeighbors(tmin, tMinPQ);
        }
        solvable = min.board.equals(goal);
        if (solvable) {
            buildResultPath(min);
        }
    }

    private Node addNeighbors(final Node currentMinNode, final MinPQ<Node> pq) {
        for (Board neighbor : currentMinNode.board.neighbors()) {
            if (currentMinNode.parent == null || !neighbor.equals(currentMinNode.parent.board)) {
                // set new neighbor node parent
                Node node = new Node(neighbor);
                node.parent = currentMinNode;
                node.moves++;
                // enqueue neighbor
                pq.insert(node);
            }
        }

        return pq.delMin();
    }

    private void buildResultPath(Node currentNode) {
        // already solved
        if (currentNode.parent == null) {
            solution.push(currentNode.board);
            return;
        }
        while (currentNode != null) {
            solution.push(currentNode.board);
            currentNode = currentNode.parent;
        }
    }

    private void initGoal(final Board initial) {
        int[][] ar = new int[initial.dimension()][initial.dimension()];
        for (int i = 0; i < initial.dimension(); i++) {
            for (int j = 0; j < initial.dimension(); j++) {
                ar[i][j] = (i * initial.dimension()) + j + 1;
            }
        }
        ar[initial.dimension() - 1][initial.dimension() - 1] = 0;
        goal = new Board(ar);
    }

    private static class Node {
        private Board board;
        private Node parent;
        private int moves;

        public Node(Board board) {
            this.board = board;
        }
    }
}
