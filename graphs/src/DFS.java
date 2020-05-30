import java.util.ArrayDeque;
import java.util.Deque;

public class DFS {

    private Graph g;
    private boolean[] marked;
    private int[] edgeTo;
    private int s;

    public DFS(Graph g, int s) {
        if (g == null || s < 0) return;

        this.g = g;
        this.s = s;
        marked = new boolean[g.vertices()];
        edgeTo = new int[g.vertices()];
        dfs(s);
    }

    public static void main(String[] args) {
        Graph g = Graph.createConnectedGraph();
        DFS dfs = new DFS(g, 0);
        Iterable<Integer> pathTo = dfs.pathTo(4);
        System.out.println(pathTo);
    }

    private Iterable<Integer> pathTo(int v) {
        if (!marked[v]) return null;

        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = v; i != s; i = edgeTo[i]) {
            stack.push(i);
        }
        stack.push(s);
        return stack;
    }

    private void dfs(int i) {
        for (int a: g.adj(i)) {
            if (!marked[a]) {

                System.out.println("visiting " + a);

                marked[a] = true;
                edgeTo[a] = i;
                dfs(a);
            }
        }
    }
}
