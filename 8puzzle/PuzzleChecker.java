/******************************************************************************
 *  Compilation:  javac-algs4 PuzzleChecker.java
 *  Execution:    java-algs4 PuzzleChecker filename1.txt filename2.txt ...
 *  Dependencies: Board.java Solver.java
 *
 *  This program creates an initial board from each filename specified
 *  on the command line and finds the minimum number of moves to
 *  reach the goal state.
 *
 *  % java-algs4 PuzzleChecker puzzle*.txt
 *  puzzle00.txt: 0
 *  puzzle01.txt: 1
 *  puzzle02.txt: 2
 *  puzzle03.txt: 3
 *  puzzle04.txt: 4
 *  puzzle05.txt: 5
 *  puzzle06.txt: 6
 *  ...
 *  puzzle3x3-impossible: -1
 *  ...
 *  puzzle42.txt: 42
 *  puzzle43.txt: 43
 *  puzzle44.txt: 44
 *  puzzle45.txt: 45
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PuzzleChecker {

    public static void main(String[] args) throws IOException {

        List<String> list;
        try (Stream<Path> paths = Files.walk(Paths.get("/Users/arezk/Downloads/8puzzle"))) {
            list = paths
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(path -> path.toFile().getName())
                    .filter(name -> name.endsWith("txt"))
                    // .filter(name -> !name.contains("14"))
                    // .filter(name -> !name.contains("15"))
                    // .filter(name -> !name.contains("16"))
                    // .filter(name -> !name.contains("23"))
                    // .filter(name -> !name.contains("25"))
                    // .filter(name -> !name.contains("26"))
                    // .filter(name -> !name.contains("27"))
                    // .filter(name -> !name.contains("28"))
                    // .filter(name -> name.equals("puzzle14.txt"))
                    .sorted()
                    .collect(Collectors.toList());
        }

        // for each command-line argument
        for (String filename : list) {

            // read in the board specified in the filename
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }

            // solve the slider puzzle
            Board initial = new Board(tiles);
            Solver solver = new Solver(initial);
            StdOut.println(filename + ": " + solver.moves());
        }
    }
}

