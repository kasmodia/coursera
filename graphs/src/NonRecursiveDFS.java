import java.util.ArrayDeque;
import java.util.Deque;

public class NonRecursiveDFS {

    private boolean[] marked;
    private int[] edgeTo;
    private Graph graph;

    public static void main(String[] args) {
        NonRecursiveDFS nonRecursiveDFS = new NonRecursiveDFS(Graph.createConnectedGraph());
        Iterable<Integer> path = nonRecursiveDFS.pathFromTo(0, 3);
        System.out.println(path);
    }

    public NonRecursiveDFS(Graph graph) {
        marked = new boolean[graph.vertices()];
        edgeTo = new int[graph.vertices()];
        this.graph = graph;
    }

    public Iterable<Integer> pathFromTo(int from, int to) {
        bfs(from);
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = to; i != from; i = edgeTo[i]) {
            stack.push(i);
        }
        stack.push(from);
        return stack;
    }

    private void bfs(int from) {
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(from);

        while(!stack.isEmpty()) {
            int v = stack.pop();
            if (!marked[v]) {
                marked[v] = true;
                for (Integer a : graph.adj(v)) {
                    if (!marked[a]) {
                        stack.push(a);
                        edgeTo[a] = v;
                    }
                }
            }
        }
    }
}
