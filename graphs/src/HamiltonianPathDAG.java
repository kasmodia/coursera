import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class HamiltonianPathDAG {

    private boolean[] marked;
    private boolean[] visited;
    private int[] edgeTo;
    private Graph graph;
    private Deque<Integer> hamiltonianPath;

    public static void main(String[] args) {
        HamiltonianPathDAG hamiltonianPathDAG = new HamiltonianPathDAG(Graph.createDAG());
        hamiltonianPathDAG.printTopologicalOrder();
        System.out.println(hamiltonianPathDAG.isHamiltonian());

        hamiltonianPathDAG = new HamiltonianPathDAG(Graph.createHamiltonianGraph());
        hamiltonianPathDAG.printTopologicalOrder();
        System.out.println(hamiltonianPathDAG.isHamiltonian());

        hamiltonianPathDAG = new HamiltonianPathDAG(Graph.createDirectedCyclicGraph());
        hamiltonianPathDAG.printTopologicalOrder();
        System.out.println(hamiltonianPathDAG.isHamiltonian());
    }

    public HamiltonianPathDAG(Graph graph) {
        this.graph = graph;
        this.marked = new boolean[graph.vertices()];
        this.edgeTo = new int[graph.vertices()];
        this.hamiltonianPath = new ArrayDeque<>();

        initHamiltonianPathWithTopologicalSort();
    }

    // To get a Hamiltonian path or cycle, It does a topological sort on a digraph.
    // If it's not a DAG it skips cycles. If it doesn't have Hamiltonian path, It
    // prints the topological sorted array
    private void initHamiltonianPathWithTopologicalSort() {
        hamiltonianPath.clear();
        marked = new boolean[graph.vertices()];
        visited = new boolean[graph.vertices()];

        for (int i = 0; i < graph.vertices(); i++) {
            if (!visited[i])
                dfs4TopologicalSort(i);
        }
    }

    private void dfs4TopologicalSort(int v) {
        marked[v] = true;
        visited[v] = true;
        for (int adj: graph.adj(v)) {
            if (marked[adj])
                continue;

            if (!visited[adj]) {
                edgeTo[adj] = v;
                dfs4TopologicalSort(adj);
            }
        }
        marked[v] = false;
        hamiltonianPath.add(v);
    }

    public boolean isHamiltonian() {
        Iterator<Integer> iterator = hamiltonianPath.descendingIterator();
        int prev = 0;
        if (iterator.hasNext())
            prev = iterator.next();

        while (iterator.hasNext()) {
            int current = iterator.next();
            if (edgeTo[current] != prev)
                return false;
            prev = current;
        }
        return true;
    }

    public void printTopologicalOrder() {
        Iterator<Integer> iterator = hamiltonianPath.descendingIterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();
    }
}
