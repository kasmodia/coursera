import java.util.ArrayDeque;
import java.util.Deque;

public class BFS {

    public static void main(String[] args) {
        BFS bfs = new BFS(Graph.createConnectedGraph(), 0);
        Iterable<Integer> path = bfs.pathTo(4);
        System.out.println(path);
    }

    private Graph graph;
    private int source;
    private int[] edgeTo;
    private int[] distanceTo;
    private boolean[] marked;

    public BFS(Graph graph, int source) {
        if (graph == null || source < 0) return;
        this.graph = graph;
        this.source = source;
        marked = new boolean[graph.vertices()];
        edgeTo = new int[graph.vertices()];
        distanceTo = new int[graph.vertices()];

        bfs(source);
    }

    public Iterable<Integer> pathTo(int v) {
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = v; i != source; i = edgeTo[i]) {
            stack.push(i);
        }
        stack.push(source);
        return stack;
    }

    private void bfs(int v) {
        Deque<Integer> queue = new ArrayDeque<>();
        queue.add(v);
        distanceTo[v] = 0;
        while (!queue.isEmpty()) {
            int currentV = queue.remove();
            if (!marked[currentV]) {
                marked[currentV] = true;
                for (int a : graph.adj(currentV)) {
                    if (!marked[a]) {
                        queue.add(a);
                        edgeTo[a] = currentV;
                        distanceTo[a] = distanceTo[currentV] + 1;
                    }
                }
            }
        }
    }
}
