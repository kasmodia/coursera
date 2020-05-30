import java.util.stream.IntStream;

public class CyclesInGraph {

    private boolean[] marked;
    private Graph graph;
    private boolean cycle;

    public static void main(String[] args) {
        CyclesInGraph cyclesInGraph = new CyclesInGraph(Graph.createConnectedGraph());
        System.out.println(cyclesInGraph.hasCycle());

        CyclesInGraph noCyclesInGraph = new CyclesInGraph(Graph.createConnectedGraphNoCycles());
        System.out.println(noCyclesInGraph.hasCycle());

        CyclesInGraph noCyclesInDisconnectedGraph = new CyclesInGraph(Graph.createDisConnectedGraphNoCycleStartingFromZero());
        System.out.println(noCyclesInDisconnectedGraph.hasCycle());

        CyclesInGraph geeks4Geeks = new CyclesInGraph(Graph.createGeeks4GeeksGraph());
        System.out.println(geeks4Geeks.hasCycle());
    }

    public CyclesInGraph(Graph graph) {
        this.graph = graph;
        marked = new boolean[graph.vertices()];

        cycle = IntStream.range(0, graph.vertices())
                .anyMatch(v -> hasCycle(v, v));
    }

    private boolean hasCycle(int parent, int currentVertex) {
        for (Integer adjacent : graph.adj(currentVertex)) {
            if (adjacent == parent) continue;
            if (marked[adjacent])
                return true;
            marked[adjacent] = true;
            if (hasCycle(currentVertex, adjacent))
                return true;
        }
        marked[currentVertex] = false;
        return false;
    }

    public boolean hasCycle() {
        return cycle;
    }
}
