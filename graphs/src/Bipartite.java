import java.util.HashSet;
import java.util.Set;

public class Bipartite {

    private boolean[] marked;
    private Graph graph;
    private Set<Integer> oddSet = new HashSet<>();
    private Set<Integer> evenSet = new HashSet<>();

    public static void main(String[] args) {
        Bipartite bipartite = new Bipartite(Graph.createBipartiteConnectedGraph());
        System.out.println(bipartite.getOddSet());
        System.out.println(bipartite.getEvenSet());
    }

    public Bipartite(Graph graph) {
        this.graph = graph;
        marked = new boolean[graph.vertices()];
        dfs(0, true);
    }

    private void dfs(int v, boolean odd) {
        for (Integer a : graph.adj(v)) {
            if (!marked[a]) {
                if (odd) oddSet.add(a);
                else evenSet.add(a);
                marked[a] = true;
                dfs(a, !odd);
            }
        }
    }

    public Set<Integer> getOddSet() {
        return oddSet;
    }

    public Set<Integer> getEvenSet() {
        return evenSet;
    }
}
