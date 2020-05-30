import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class LongestPath {

    private final int[] edgeTo;
    private int longestPath;
    private int $;

    public static void main(String[] args) {
        LongestPath longestPath = new LongestPath(Graph.createConnectedGraphNoCycles());
        System.out.println(longestPath.getLongestPath());
    }

    // reach any leaf then start marking from there to get the longest path between two leaves
    public LongestPath(Graph graph) {
        boolean[] marked = new boolean[graph.vertices()];
        int[] level = new int[graph.vertices()];
        edgeTo = new int[graph.vertices()];
        Deque<Integer> queue = new ArrayDeque<>();

        for (Integer integer : graph.adj($)) {
            $ = integer;
        }

        queue.add($);
        int maxLevel = 0;
        while (!queue.isEmpty()) {
            int v = queue.removeFirst();
            if (!marked[v]) {
                marked[v] = true;
                if (level[v] > maxLevel) {
                    maxLevel = level[v];
                    longestPath = v;
                }
                for (Integer a : graph.adj(v)) {
                    if (!marked[a]) {
                        edgeTo[a] = v;
                        level[a] = level[v] + 1;
                         queue.add(a);
                    }
                }
            }
        }
    }

    public List<Integer> getLongestPath() {
        List<Integer> result = new ArrayList<>();
        for (int i = longestPath; i != $; i = edgeTo[i]) {
            result.add(i);
        }
        result.add($);
        return result;
    }
}
