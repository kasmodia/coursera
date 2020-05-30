import java.util.LinkedList;

public abstract class Graph {
    protected int v;
    protected LinkedList<Integer>[] adj;

    public Graph() {
    }

    public Graph(int v) {
        this.v = v;
        adj = (LinkedList<Integer>[]) new LinkedList[v];

        for (int i = 0; i < adj.length; i++) {
            adj[i] = new LinkedList<>();
        }
    }

    public abstract void addEdge(int v, int w);

    public static Graph createBipartiteConnectedGraph() {
        Graph g = new UndirectedGraph(7);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(0, 5);
        g.addEdge(0, 6);
        g.addEdge(1, 3);
        g.addEdge(2, 3);
        g.addEdge(2, 4);
        g.addEdge(4, 5);
        g.addEdge(4, 6);
        return g;
    }

    public static Graph createConnectedGraph() {
        Graph g = new UndirectedGraph(7);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(0, 5);
        g.addEdge(0, 6);
        g.addEdge(4, 5);
        g.addEdge(5, 3);
        g.addEdge(6, 4);
        return g;
    }

    public static Graph createConnectedGraphNoCycles() {
        Graph g = new UndirectedGraph(8);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(0, 5);
        g.addEdge(0, 6);
        g.addEdge(5, 3);
        g.addEdge(3, 4);
        g.addEdge(3, 7);
        return g;
    }

    public static Graph createDisConnectedGraph() {
        Graph g = new UndirectedGraph(13);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(0, 5);
        g.addEdge(0, 6);
        g.addEdge(5, 3);
        g.addEdge(5, 4);
        g.addEdge(6, 4);
        g.addEdge(3, 4);
        g.addEdge(7, 8);
        g.addEdge(9, 10);
        g.addEdge(9, 11);
        g.addEdge(9, 12);
        g.addEdge(11, 12);
        return g;
    }

    public static Graph createDisConnectedGraphNoCycleStartingFromZero() {
        Graph g = new UndirectedGraph(13);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(0, 5);
        g.addEdge(5, 3);
        g.addEdge(6, 4);
        g.addEdge(3, 4);
        g.addEdge(7, 8);
        g.addEdge(9, 10);
        g.addEdge(9, 11);
        g.addEdge(9, 12);
        g.addEdge(11, 12);
        return g;
    }

    public static Graph createGeeks4GeeksGraph() {
        Graph g = new UndirectedGraph(46);
        String in = "0 44 1 23 1 35 1 37 1 38 2 20 2 35 3 13 4 44 5 21 5 36 6 41 7 8 8 18 9 17 9 41 9 45 10 13 10 21 10 33 10 34 10 39 10 42 11 17 12 24 13 44 14 19 15 25 16 34 18 24 19 25 21 24 21 26 22 37 23 28 25 31 25 35 25 40 25 41 25 44 27 43 27 44 29 40 30 34 32 33";
        String[] vertexes = in.split(" ");
        String even = null;
        for (int i = 0; i < vertexes.length; i++) {
            if (i % 2 == 0)
                even = vertexes[i];
            else
                g.addEdge(Integer.parseInt(even), Integer.parseInt(vertexes[i]));
        }
        return g;
    }

    public static Graph createHamiltonianGraph() {
        Graph graph = new Digraph(6);
        graph.addEdge(0, 1);
        graph.addEdge(0, 3);
        graph.addEdge(0, 4);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 1);
        graph.addEdge(3, 4);
        graph.addEdge(4, 5);
        graph.addEdge(5, 0);
        return graph;
    }

    public static Graph createDAG() {
        Graph graph = new Digraph(7);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(0, 5);
        graph.addEdge(1, 4);
        graph.addEdge(6, 0);
        graph.addEdge(6, 4);
        graph.addEdge(5, 2);
        graph.addEdge(3, 2);
        graph.addEdge(3, 5);
        graph.addEdge(3, 6);
        return graph;
    }

    public static Graph createDiGraphWith2Cycles() {
        Graph graph = new Digraph(7);
        graph.addEdge(0, 1);
        graph.addEdge(1, 0);
        graph.addEdge(0, 2);
        graph.addEdge(2, 5);
        graph.addEdge(5, 6);
        graph.addEdge(6, 1);
        graph.addEdge(2, 4);
        graph.addEdge(4, 2);
        graph.addEdge(4, 3);
        graph.addEdge(3, 2);
        return graph;
    }

    public static Graph createDirectedCyclicGraph() {
        Graph graph = new Digraph(7);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(0, 5);
        graph.addEdge(1, 4);
        graph.addEdge(1, 3);
        graph.addEdge(3, 2);
        graph.addEdge(2, 1);
        graph.addEdge(6, 0);
        graph.addEdge(6, 4);
        graph.addEdge(5, 2);
        graph.addEdge(3, 5);
        graph.addEdge(3, 6);
        return graph;
    }

    public static Graph createEulerGraph() {
        Graph g = new UndirectedGraph(8);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 2);
        return g;
    }

    public Iterable<Integer> adj(int v) {
        return adj[v];
    }

    public int vertices() {
        return v;
    }
}
