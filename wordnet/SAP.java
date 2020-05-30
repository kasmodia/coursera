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

public class SAP {

    private Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException("Null Digraph");
        this.digraph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return 0;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        boolean[] vVisited = new boolean[digraph.V()];
        boolean[] wVisited = new boolean[digraph.V()];
        int[] edgeToV = new int[digraph.V()];
        int[] edgeToW = new int[digraph.V()];
        Queue<Integer> vQ = new Queue<>();
        vQ.enqueue(v);
        Queue<Integer> wQ = new Queue<>();
        wQ.enqueue(w);

        while (!vQ.isEmpty() || !wQ.isEmpty()) {
            if (!vQ.isEmpty()) {
                Integer currentV = vQ.dequeue();
                // touched the other point's path
                if (wVisited[currentV])
                    return currentV;
                // skip on cycles
                if (vVisited[currentV])
                    continue;
                vVisited[currentV] = true;
                // enqueue adjacent vertices
                for (int adj : digraph.adj(currentV)) {
                    if (!vVisited[adj]) {
                        edgeToV[adj] = currentV;
                        vQ.enqueue(adj);
                    }
                }
            }
            if (!wQ.isEmpty()) {
                Integer currentW = wQ.dequeue();
                // touched the other point's path
                if (vVisited[currentW])
                    return currentW;
                // skip on cycles
                if (wVisited[currentW])
                    continue;
                wVisited[currentW] = true;
                // enqueue adjacent vertices
                for (int adj : digraph.adj(currentW)) {
                    if (!wVisited[adj]) {
                        edgeToW[adj] = currentW;
                        wQ.enqueue(adj);
                    }
                }
            }
        }
        // no ancestor
        return -1;
    }

    private int calcDistance(Integer v, Integer w, Integer intersection, boolean[] vVisited, boolean[] wVisited, int[] edgeToV, int[] edgeToW) {
        int counter = 0;
        for (int i = intersection; i != v; i = edgeToV[i]) {
            counter++;
        }
        counter++;
        for (int i = intersection; i != w; i = edgeToW[i]) {
            counter++;
        }
        return counter;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return 0;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return 0;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
