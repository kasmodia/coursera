/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordNet {

    private Map<String, List<Integer>> synsetsMap;
    private Digraph wordNetGraph;
    private int root = -1;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException("Null file names");
        synsetsMap = new HashMap<>();

        initSynsetMap(synsets);
        initWordNetGraph(hypernyms);
    }

    private void initWordNetGraph(String hypernyms) {
        In in = new In(hypernyms);
        if (in.isEmpty()) throw new IllegalArgumentException("Empty hypernym file");
        String[] lines = in.readAllLines();
        wordNetGraph = new Digraph(lines.length);
        for (String line: lines) {
            String[] pair = line.split(",");
            if (pair.length == 1) continue;
            wordNetGraph.addEdge(Integer.parseInt(pair[0]), Integer.parseInt(pair[1]));
        }
        validateGraph();
    }

    private void validateGraph() {
        // cyclic? rooted?
        boolean[] marked = new boolean[wordNetGraph.V()];
        boolean[] visited = new boolean[wordNetGraph.V()];
        for (int i = 0; i < wordNetGraph.V(); i++) {
            if (!visited[i])
                dfs(i, marked, visited);
        }
    }

    private void dfs(int v, boolean[] currentPath, boolean[] visited) {
        visited[v] = true;
        currentPath[v] = true;
        for (int adj: wordNetGraph.adj(v)) {
            if (currentPath[adj])
                throw new IllegalArgumentException("Graph is not ascyclic");
            if (!visited[adj])
                dfs(adj, currentPath, visited);
        }
        currentPath[v] = false;

        // first time the root is found, should be the last.
        // if you reached a new root, which means the graph has more than one root
        if (root == -1)
            root = v;
        else if (wordNetGraph.outdegree(v) == 0)
            throw new IllegalArgumentException("Graph is not rooted");
    }

    private void initSynsetMap(String synsets) {
        In in = new In(synsets);
        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] splits = line.split(",");
            int id = Integer.parseInt(splits[0]);
            // split nounds in the same synset
            for (String noun: splits[1].split(" ")) {
                // construct a map with noun as key and ID as value
                // if a noun appears in more than one synset, add it to the list of IDs
                if (synsetsMap.containsKey(noun)) {
                    synsetsMap.get(noun).add(id);
                } else {
                    List<Integer> list = new ArrayList<>();
                    list.add(id);
                    synsetsMap.put(noun, list);
                }
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synsetsMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return synsetsMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        return 0;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        return null;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("synsets.txt", "hypernyms.txt");
    }
}
