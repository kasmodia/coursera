import java.util.stream.IntStream;

public class EulerCycle {

    private boolean isEuler;

    public static void main(String[] args) {
        EulerCycle eulerCycle =  new EulerCycle(Graph.createConnectedGraph());
        System.out.println(eulerCycle.isEuler());

        eulerCycle = new EulerCycle(Graph.createEulerGraph());
        System.out.println(eulerCycle.isEuler());
    }

    public EulerCycle(Graph graph) {
        isEuler = IntStream.range(0, graph.vertices()).allMatch(v -> graph.adj(v).spliterator().getExactSizeIfKnown() % 2 == 0);
    }

    public boolean isEuler() {
        return isEuler;
    }
}
