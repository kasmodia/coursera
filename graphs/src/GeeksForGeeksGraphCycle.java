import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


class GeeksForGeeksGraphCycle
{
    private static boolean[] marked;
    private static ArrayList<ArrayList<Integer>> graph;

    public static void main(String[] args) {
        ArrayList lists = new ArrayList();
        lists.add(Stream.of(1,2,5,6).collect(Collectors.toList()));
        lists.add(Stream.of(0).collect(Collectors.toList()));
        lists.add(Stream.of(0).collect(Collectors.toList()));
        lists.add(Stream.of(4,5).collect(Collectors.toList()));
        lists.add(Stream.of(3, 5, 6).collect(Collectors.toList()));
        lists.add(Stream.of(0, 3, 4).collect(Collectors.toList()));
        lists.add(Stream.of(0, 4).collect(Collectors.toList()));
        GeeksForGeeksGraphCycle.isCyclic(lists, 7);
    }

    static boolean isCyclic(ArrayList<ArrayList<Integer>> list, int V) {
        // add your code here
        marked = new boolean[list.size()];
        graph = list;
        boolean cycle = hasCycle(0, new ArrayList<>());
        System.out.println(cycle);
        return cycle;
    }

    private static boolean hasCycle(int v, List<Integer> path) {
        if (graph.get(v).size() == 1) {
            path.remove(path.size() - 1);
            return false;
        }
        if (!marked[v]) {
            marked[v] = true;
            path.add(v);
        }
        for (Integer a : graph.get(v)) {
            if (path.contains(a) && path.indexOf(v) - path.indexOf(a) > 1)
                return true;

            if (!marked[a]) {
                marked[a] = true;
                path.add(a);
                if (hasCycle(a, path))
                    return true;
            }
        }
        return false;
    }

}

//
//class GeeksForGeeksGraphCycle
//{
//
//    private static boolean[] marked;
//    private static ArrayList<ArrayList<Integer>> graph;
//
//    public static void main(String[] args) {
//        ArrayList lists = new ArrayList();
//        lists.add(Stream.of(1,2,5,6).collect(Collectors.toList()));
//        lists.add(Stream.of(0).collect(Collectors.toList()));
//        lists.add(Stream.of(0).collect(Collectors.toList()));
//        lists.add(Stream.of(4,5).collect(Collectors.toList()));
//        lists.add(Stream.of(3, 5, 6).collect(Collectors.toList()));
//        lists.add(Stream.of(0, 3, 4).collect(Collectors.toList()));
//        lists.add(Stream.of(0, 4).collect(Collectors.toList()));
//        GeeksForGeeksGraphCycle.isCyclic(lists, 7);
//    }
//
//    static boolean isCyclic(ArrayList<ArrayList<Integer>> list, int V) {
//        // add your code here
//        marked = new boolean[list.size()];
//        graph = list;
//        boolean cycle = hasCycle(0, new ArrayList<>());
//        System.out.println(cycle);
//        return cycle;
//    }
//
//    private static boolean hasCycle(int v, List<Integer> path) {
//        if (graph.get(v).size() == 1) {
//            path.remove(path.size() - 1);
//            return false;
//        }
//        if (!marked[v]) {
//            marked[v] = true;
//            path.add(v);
//        }
//        for (Integer a : graph.get(v)) {
//            if (path.contains(a) && path.indexOf(v) - path.indexOf(a) > 1)
//                return true;
//
//            if (!marked[a]) {
//                marked[a] = true;
//                path.add(a);
//                if (hasCycle(a, path))
//                    return true;
//            }
//        }
//        return false;
//    }
//
//}
//
