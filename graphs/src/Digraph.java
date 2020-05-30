public class Digraph extends Graph {

    public Digraph(int v) {
        super(v);
    }

    @Override
    public void addEdge(int v, int w) {
        if (v >= this.v || w >= this.v) throw new IllegalArgumentException("One of the vertices exceeds capacity");
        adj[v].add(w);
    }
}
