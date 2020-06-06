/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class SAP {

    private Digraph digraph;
    private List<Integer[]> lengthsAndAncestors;
    private Queue<Integer> vQ;
    private Queue<Integer> wQ;
    private int[] vDistTo;
    private int[] wDistTo;
    private boolean[] vMarked;
    private boolean[] wMarked;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException("Null Digraph");
        this.digraph = new Digraph(G);
        lengthsAndAncestors = new ArrayList<>();
        lengthsAndAncestors = new ArrayList<>();
        vQ = new Queue<>();
        wQ = new Queue<>();
        vDistTo = new int[digraph.V()];
        wDistTo = new int[digraph.V()];
        vMarked = new boolean[digraph.V()];
        wMarked = new boolean[digraph.V()];
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v >= digraph.V() || w >= digraph.V() || v < 0 || w < 0)
            throw new IllegalArgumentException("Vertices out of bounds.");

        if (v == w) return 0;
        return lengthsAndAncestor(v, w)[0];
    }

    public int ancestor(int v, int w) {
        if (v >= digraph.V() || w >= digraph.V() || v < 0 || w < 0)
            throw new IllegalArgumentException("Vertices out of bounds.");

        if (v == w) return v;
        return lengthsAndAncestor(v, w)[1];
    }

    private Integer[] lengthsAndAncestor(int v, int w) {
        List<Integer> vMarkedList = new ArrayList<>();
        List<Integer> wMarkedList = new ArrayList<>();
        lengthsAndAncestors = new ArrayList<>();
        vQ = new Queue<>();
        wQ = new Queue<>();
        vMarked[v] = true;
        wMarked[w] = true;
        vMarkedList.add(v);
        wMarkedList.add(w);
        vQ.enqueue(v);
        wQ.enqueue(w);

        // BFS in lockstep
        while (!vQ.isEmpty() || !wQ.isEmpty()) {
            if (!vQ.isEmpty()) {
                Integer current = vQ.dequeue();
                step(current, vMarked, wMarked, vDistTo, wDistTo, vMarkedList, vQ);
            }
            if (!wQ.isEmpty()) {
                Integer current = wQ.dequeue();
                step(current, wMarked, vMarked, wDistTo, vDistTo, wMarkedList, wQ);
            }
        }
        // clean arrays
        for (Integer mark : vMarkedList) {
            vMarked[mark] = false;
            vDistTo[mark] = 0;
        }
        for (Integer mark : wMarkedList) {
            wMarked[mark] = false;
            wDistTo[mark] = 0;
        }

        if (lengthsAndAncestors.isEmpty()) return new Integer[] { -1, -1 };
        else {
            Integer[] shortest = lengthsAndAncestors.get(0);
            for (Integer[] lengthAndAncestor : lengthsAndAncestors) {
                if (lengthAndAncestor[0] < shortest[0]) shortest = lengthAndAncestor;
            }
            return shortest;
        }
    }

    private void step(int current, boolean[] thisMarked, boolean[] otherMarked,
                      int[] thisDistTo, int[] thatDistTo,
                      List<Integer> markedList, Queue<Integer> q) {
        // touched the other point's path?
        if (otherMarked[current]) {
            lengthsAndAncestors
                    .add(new Integer[] { thisDistTo[current] + thatDistTo[current], current });
        }

        // enqueue adjacent vertices
        for (int adj : digraph.adj(current)) {
            if (!thisMarked[adj]) {
                thisMarked[adj] = true;
                thisDistTo[adj] = thisDistTo[current] + 1;
                markedList.add(adj);
                q.enqueue(adj);
            }
        }
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> vs, Iterable<Integer> ws) {
        if (vs == null || ws == null) throw new IllegalArgumentException("Null Digraph");

        int shortest = Integer.MAX_VALUE;
        for (Integer v : vs) {
            for (Integer w : ws) {
                if (v == null || w == null || v < 0 || w < 0)
                    throw new IllegalArgumentException("Invalid Vertex in Digraph");
                int length = length(v, w);
                if (length < shortest) {
                    shortest = length;
                }
            }
        }
        return shortest == Integer.MAX_VALUE ? -1 : shortest;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> vs, Iterable<Integer> ws) {
        if (vs == null || ws == null) throw new IllegalArgumentException("Null Digraph");

        Integer[] shortest = new Integer[] { Integer.MAX_VALUE, Integer.MAX_VALUE };
        for (Integer v : vs) {
            for (Integer w : ws) {
                if (v == null || w == null || v < 0 || w < 0)
                    throw new IllegalArgumentException("Invalid Vertex in Digraph");
                if (v.equals(w)) return v;
                Integer[] lengthAndAncestor = lengthsAndAncestor(v, w);
                if (lengthAndAncestor[0] < shortest[0]) {
                    shortest = lengthAndAncestor;
                }
            }
        }
        return shortest[1] == Integer.MAX_VALUE ? -1 : shortest[1];
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            String[] v = StdIn.readLine().split(",");
            String[] w = StdIn.readLine().split(",");

            List<Integer> vlist = new ArrayList<>();
            for (String s : v) {
                vlist.add(Integer.parseInt(s.trim()));
            }
            List<Integer> wlist = new ArrayList<>();
            for (String s : w) {
                wlist.add(Integer.parseInt(s.trim()));
            }
            int length = sap.length(vlist, wlist);
            int ancestor = sap.ancestor(vlist, wlist);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
