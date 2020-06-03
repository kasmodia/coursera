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
    private List<String> synsets;
    private Digraph wordNetGraph;
    private int root = -1;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("Null file names");
        this.synsetsMap = new HashMap<>();
        this.synsets = new ArrayList<>();

        initSynsetMap(synsets);
        initWordNetGraph(hypernyms);
    }

    private void initWordNetGraph(String hypernyms) {
        In in = new In(hypernyms);
        if (in.isEmpty()) throw new IllegalArgumentException("Empty hypernym file");
        String[] lines = in.readAllLines();
        wordNetGraph = new Digraph(lines.length);
        for (String line : lines) {
            String[] pair = line.split(",");
            if (pair.length == 1) continue;
            for (int i = 1; i < pair.length; i++) {
                wordNetGraph.addEdge(Integer.parseInt(pair[0]), Integer.parseInt(pair[i]));
            }
        }
        validateGraph();
        sap = new SAP(wordNetGraph);
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
        for (int adj : wordNetGraph.adj(v)) {
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
            this.synsets.add(splits[1]);
            // split nounds in the same synset
            for (String noun : splits[1].split(" ")) {
                // construct a map with noun as key and ID as value
                // if a noun appears in more than one synset, add it to the list of IDs
                if (synsetsMap.containsKey(noun)) {
                    synsetsMap.get(noun).add(id);
                }
                else {
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
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("Nouns must exist in WordNet");
        return sap.length(synsetsMap.get(nounA), synsetsMap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("Nouns must exist in WordNet");
        int ancestor = sap.ancestor(synsetsMap.get(nounA), synsetsMap.get(nounB));
        return synsets.get(ancestor);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("synsets.txt", "hypernyms.txt");
        System.out.println("worm is a noun: " + wordNet.isNoun("worm"));
        System.out.println("kasmodia is a noun: " + wordNet.isNoun("kasmodia"));
        System.out.println("'null' is a noun: " + wordNet.isNoun(null));

        System.out.println("distance 'white_marlin' and 'mileage': "
                                   + wordNet.distance("white_marlin", "mileage"));

        System.out.println("distance 'Black_Plague' and 'black_marlin': "
                                   + wordNet.distance("Black_Plague", "black_marlin"));

        System.out.println("distance 'American_water_spaniel' and 'histology': "
                                   + wordNet.distance("American_water_spaniel", "histology"));

        System.out.println("distance 'Brown_Swiss' and 'barrel_roll': "
                                   + wordNet.distance("Brown_Swiss", "barrel_roll"));

        System.out.println("SCA between 'individual' and 'edible_fruit': "
                                   + wordNet.sap("individual", "edible_fruit"));
        System.out.println("distance between 'individual' and 'edible_fruit': "
                                   + wordNet.distance("individual", "edible_fruit"));
    }
}
