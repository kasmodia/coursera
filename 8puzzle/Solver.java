/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private Board goal;
    private Stack<Board> solution = new Stack<>();
    private boolean solvable;
    private int moves;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Null argument");
        initGoal(initial);
        MinPQ<Node<Board>> minPQ = new MinPQ<>();
        minPQ.insert(new Node<>(initial));
        MinPQ<Node<Board>> tMinPQ = new MinPQ<>();
        tMinPQ.insert(new Node<>(initial.twin()));
        solve(minPQ, tMinPQ);
    }

    public static void main(String[] args) {
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

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }
    // min number of moves to solve initial board

    public int moves() {
        return solution.size() - 1;
    }
    // sequence of boards in a shortest solution

    public Iterable<Board> solution() {
        return solution;
    }

    private void solve(MinPQ<Node<Board>> minPQ, MinPQ<Node<Board>> tMinPQ) {
        Node<Board> min = minPQ.delMin();
        Node<Board> tmin = tMinPQ.delMin();
        int lastPriority = -1;
        while (!min.board.equals(goal) && !tmin.board.equals(goal)) {
            moves++;
            min = addChildren(min, minPQ);
            if (lastPriority > min.board.priority())
                throw new IllegalArgumentException("something is wrong here");
            lastPriority = min.board.priority();
            tmin = addChildren(tmin, tMinPQ);
        }
        solvable = min.board.equals(goal);
        if (solvable) {
            buildResultPath(min);
        }
    }

    private Node<Board> addChildren(Node<Board> currentMinNode, MinPQ<Node<Board>> pq) {
        for (Board neighbor : currentMinNode.board.neighbors()) {
            if (currentMinNode.parent == null || !neighbor.equals(currentMinNode.parent.board)) {
                neighbor.setMoves(moves);
                // set new neighbor node parent
                Node<Board> node = new Node<>(neighbor);
                node.parent = currentMinNode;
                // enqueue neighbor
                pq.insert(node);
            }
        }

        return pq.delMin();
    }

    private void buildResultPath(Node<Board> currentNode) {
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

    private void initGoal(Board initial) {
        int[][] ar = new int[initial.dimension()][initial.dimension()];
        for (int i = 0; i < initial.dimension(); i++) {
            for (int j = 0; j < initial.dimension(); j++) {
                ar[i][j] = (i * initial.dimension()) + j + 1;
            }
        }
        ar[initial.dimension() - 1][initial.dimension() - 1] = 0;
        goal = new Board(ar);
    }

    // test client (see below)
    private static class Node<T extends Comparable<T>> implements Comparable<Node<T>> {
        T board;
        Node<T> parent;

        public Node(T board) {
            this.board = board;
        }

        public int compareTo(Node<T> o) {
            return board.compareTo(o.board);
        }
    }
}
