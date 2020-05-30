import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.IntStream;

public class ShortestCycleDiGraph {

    private boolean[] visited;
    private boolean[] tempMarked;
    private Graph graph;
    private int cycles;
    private Deque<Integer> shortestCycle;
    private List<Deque<Integer>> cyclesList;
    private int[] edgeTo;

    public static void main(String[] args) {
        ShortestCycleDiGraph shortestCycleDiGraph = new ShortestCycleDiGraph(Graph.createDAG());
        System.out.println(shortestCycleDiGraph.hasCycle());

        shortestCycleDiGraph = new ShortestCycleDiGraph(Graph.createDiGraphWith2Cycles());
        System.out.println(shortestCycleDiGraph.hasCycle());
        System.out.println(shortestCycleDiGraph.getShortestCycle());
        System.out.println(shortestCycleDiGraph.getCycles());
        System.out.println(shortestCycleDiGraph.getCyclesList());
    }

    private Deque<Integer> getShortestCycle() {
        return shortestCycle;
    }

    public ShortestCycleDiGraph(Graph graph) {
        this.graph = graph;
        visited = new boolean[graph.vertices()];
        tempMarked = new boolean[graph.vertices()];
        edgeTo = new int[graph.vertices()];
        shortestCycle = new ArrayDeque<>();
        cyclesList = new ArrayList<>();

        IntStream.range(0, graph.vertices()).forEach(this::dfs);
    }

    private void dfs(int v) {
        if (!visited[v]) {
            visited[v] = true;
            tempMarked[v] = true;
            for (int adj : graph.adj(v)) {
                edgeTo[adj] = v;
                if (tempMarked[adj]) {
                    cycles++;
                    checkShortestCycle(v, adj);
                }
                if (!visited[adj])
                    dfs(adj);
            }
        }
        tempMarked[v] = false;
    }

    private void checkShortestCycle(int parent, int adj) {
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = parent; i != adj; i = edgeTo[i]) {
            stack.push(i);
        }
        stack.push(adj);
        cyclesList.add(stack);
        if (shortestCycle.isEmpty() || stack.size() < shortestCycle.size())
            shortestCycle = stack;
    }

    public boolean hasCycle() {
        return shortestCycle.size() > 0;
    }

    public int getCycles() {
        return cycles;
    }

    public List<Deque<Integer>> getCyclesList() {
        return cyclesList;
    }
}
