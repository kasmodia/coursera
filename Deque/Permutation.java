import java.util.Iterator;

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(final String[] args) {
        if (args == null || args.length != 1)
            throw new IllegalArgumentException("Missing argument");

        final int k = Integer.parseInt(args[0]);
        final RandomizedQueue<String> queue = new RandomizedQueue<>();
        
        while (!StdIn.isEmpty()) {
            queue.enqueue(StdIn.readString());
        }

        final Iterator<String> it = queue.iterator();
        for (int i = 0; i < k; i++)
            System.out.println(it.next());
    }
 }