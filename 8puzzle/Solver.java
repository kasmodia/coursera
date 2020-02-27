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

    private Board goal;
    private Board lastMin;
    private Stack<Board> solution = new Stack<>();
    private boolean solvable;
    private int moves;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Null argument");
        initGoal(initial);
        Board currentBoard = initial;
        Board tCurrentBoard = initial.twin();
        // Node<Board> currentNode = new Node<>(initial);
        // Node<Board> tCurrentNode = new Node<>(initial.twin());
        Comparator<Board> comparator = (Board o1, Board o2) ->
                Integer.compare(o1.manhattan() + o1.getMoves(), o2.manhattan() + o2.getMoves());
        MinPQ<Board> minPQ = new MinPQ<>(comparator);
        MinPQ<Board> tMinPQ = new MinPQ<>(comparator);
        while (!currentBoard.equals(goal) && !tCurrentBoard.equals(goal)) {
            moves++;
            currentBoard = step(currentBoard, minPQ);
            tCurrentBoard = step(tCurrentBoard, tMinPQ);
        }
        solvable = currentBoard.equals(goal);
        if (solvable) {
            while (currentBoard != null) {
                solution.push(currentBoard);
                currentBoard = currentBoard.getParent();
            }
        }
    }

    private Board step(Board currentNode, MinPQ<Board> pq) {
        for (Board neighbor : currentNode.neighbors()) {
            if (!neighbor.equals(currentNode.getParent())) {
                neighbor.setMoves(moves);
                neighbor.setParent(currentNode);
                pq.insert(neighbor);
            }
        }

        return pq.delMin();
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

    // test client (see below)
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

    private static class Node<T> {
        T board;
        Node<T> parent;

        public Node(T board) {
            this.board = board;
        }
    }
}
