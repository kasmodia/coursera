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
    private int[] edgeToV;
    private int[] edgeToW;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException("Null Digraph");
        this.digraph = new Digraph(G);
        this.edgeToV = new int[digraph.V()];
        this.edgeToW = new int[digraph.V()];
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v >= digraph.V() || w >= digraph.V() || v < 0 || w < 0)
            throw new IllegalArgumentException("Vertices out of bounds.");

        return lengthsAndAncestor(v, w)[0];
    }

    public int ancestor(int v, int w) {
        if (v >= digraph.V() || w >= digraph.V() || v < 0 || w < 0)
            throw new IllegalArgumentException("Vertices out of bounds.");

        return lengthsAndAncestor(v, w)[1];
    }

    private Integer[] lengthsAndAncestor(int v, int w) {
        boolean[] vVisited = new boolean[digraph.V()];
        boolean[] wVisited = new boolean[digraph.V()];
        Queue<Integer> vQ = new Queue<>();
        vQ.enqueue(v);
        Queue<Integer> wQ = new Queue<>();
        wQ.enqueue(w);

        // BFS in lockstep
        List<Integer[]> lengthsAndAncestors = new ArrayList<>();
        while (!vQ.isEmpty() || !wQ.isEmpty()) {
            if (!vQ.isEmpty())
                step(v, w, vVisited, wVisited, vQ, edgeToV, lengthsAndAncestors);
            if (!wQ.isEmpty())
                step(v, w, wVisited, vVisited, wQ, edgeToW, lengthsAndAncestors);
        }

        // calculate shortest ancestor
        Integer[] shortest = new Integer[] {Integer.MAX_VALUE, Integer.MAX_VALUE};
        for (Integer[] lengthAndAncestor: lengthsAndAncestors) {
            if (lengthAndAncestor[0] < shortest[0]) shortest = lengthAndAncestor;
        }

        // if no ancestor return {-1, -1}
        if (lengthsAndAncestors.size() == 0) return new Integer[] { -1, -1 };
        return shortest;
    }

    private void step(int v, int w, boolean[] thisVisited, boolean[] otherVisited,
                         Queue<Integer> q, int[] edges,
                         List<Integer[]> lengthsAndAncestors) {
        Integer current = q.dequeue();
        // touched the other point's path
        if (otherVisited[current])
            lengthsAndAncestors.add(calcLength(v, w, current));

        thisVisited[current] = true;

        // enqueue adjacent vertices
        for (int adj : digraph.adj(current)) {
            if (!thisVisited[adj]) {
                edges[adj] = current;
                q.enqueue(adj);
            }
        }
    }

    private Integer[] calcLength(int v, int w, int ancestor) {
        int counter = 0;
        for (int i = ancestor; i != v; i = edgeToV[i]) {
            counter++;
        }
        for (int i = ancestor; i != w; i = edgeToW[i]) {
            counter++;
        }
        return new Integer[] {counter, ancestor};
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> vs, Iterable<Integer> ws) {
        if (vs == null || ws == null) throw new IllegalArgumentException("Null Digraph");

        Integer[] shortest = new Integer[] {Integer.MAX_VALUE, Integer.MAX_VALUE};
        for (Integer v : vs) {
            for (Integer w : ws) {
                Integer[] lengthAndAncestor = lengthsAndAncestor(v, w);
                if (lengthAndAncestor[0] < shortest[0]) {
                    shortest = lengthAndAncestor;
                }
            }
        }
        return shortest[0] == Integer.MAX_VALUE ? -1 : shortest[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> vs, Iterable<Integer> ws) {
        if (vs == null || ws == null) throw new IllegalArgumentException("Null Digraph");

        Integer[] shortest = new Integer[] {Integer.MAX_VALUE, Integer.MAX_VALUE};
        for (Integer v : vs) {
            for (Integer w : ws) {
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
            for (String s: v) {
                vlist.add(Integer.parseInt(s.trim()));
            }
            List<Integer> wlist = new ArrayList<>();
            for (String s: w) {
                wlist.add(Integer.parseInt(s.trim()));
            }
            int length = sap.length(vlist, wlist);
            int ancestor = sap.ancestor(vlist, wlist);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
