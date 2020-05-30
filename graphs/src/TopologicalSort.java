import java.util.ArrayDeque;
import java.util.Deque;

public class TopologicalSort {

    public static void main(String[] args) {
        TopologicalSort topologicalSort = new TopologicalSort(Graph.createDAG());
        System.out.println(topologicalSort.getStack());

        topologicalSort = new TopologicalSort(Graph.createDirectedCyclicGraph());
        System.out.println(topologicalSort.getStack());
    }

    private boolean[] marked;
    private boolean[] history;
    private Deque<Integer> stack = new ArrayDeque<>();

    public TopologicalSort(Graph graph) {
        marked = new boolean[graph.vertices()];
        history = new boolean[graph.vertices()];
        for (int  i = 0; i < graph.vertices(); i++) {
            if (!marked[i])
                dfs(graph, i);
        }
    }

    private void dfs(Graph graph, int v) {
        marked[v] = true;
        history[v] = true;
        for (int adj: graph.adj(v)) {
            if (history[adj]) throw new IllegalArgumentException("Not a DAG");
            if (!marked[adj]) {
                marked[adj] = true;
                dfs(graph, adj);
            }
        }
        history[v] = false;
        stack.push(v);
    }

    public Deque<Integer> getStack() {
        return stack;
    }
}
