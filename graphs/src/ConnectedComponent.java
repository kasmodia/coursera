public class ConnectedComponent {

    private boolean[] marked;
    private int[] cc;
    private Graph graph;

    public static void main(String[] args) {
        ConnectedComponent cc = new ConnectedComponent(Graph.createDisConnectedGraph());
        System.out.println(cc.isConnected(1, 3));
        System.out.println(cc.isConnected(1, 10));
        System.out.println(cc.isConnected(5, 11));
        System.out.println(cc.isConnected(8, 11));
    }

    private boolean isConnected(int v, int w) {
        return cc[v] == cc[w];
    }

    public ConnectedComponent(Graph graph) {
        this.graph = graph;
        marked = new boolean[graph.vertices()];
        cc = new int[graph.vertices()];
        int groupCounter = 0;
        for (int i = 0; i < graph.vertices(); i++) {
            initGraph(i, groupCounter++);
        }
    }

    private void initGraph(int v, int groupCounter) {
        if (!marked[v]) {
            marked[v] = true;
            cc[v] = groupCounter;
            graph.adj(v).forEach(a -> initGraph(a, groupCounter));
        }
    }

}
