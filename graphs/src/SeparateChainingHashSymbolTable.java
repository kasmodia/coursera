import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SeparateChainingHashSymbolTable<K, V> {

    private int size;
    private final int CAPACITY = 97;
    private final Node[] st = new Node[CAPACITY];

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeparateChainingHashSymbolTable<?, ?> that = (SeparateChainingHashSymbolTable<?, ?>) o;
        return size == that.size &&
                CAPACITY == that.CAPACITY &&
                Arrays.equals(st, that.st);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size, CAPACITY);
        result = 31 * result + Arrays.hashCode(st);
        return result;
    }

    public static void main(String[] args) {
        SeparateChainingHashSymbolTable<String, String> map = new SeparateChainingHashSymbolTable<>();
        map.add("a", "Ahmed");
        map.add("b", "Rezk");
        map.add("c", "Attia");
        map.add("a", "Badr");

        System.out.println(map.get("a"));
        System.out.println(map.get("b"));
        System.out.println(map.get("c"));
        System.out.println(map.get("a"));
        System.out.println(map.size());


        String a = "Aa";
        String b = "BB";
        System.out.println(a.hashCode());
        System.out.println(b.hashCode());
        System.out.println(31 * ('C' - 'D') == ('B' - 'a'));
        System.out.println(31 * ('B' - 'A') == ('a' - 'B'));
        System.out.println("common_prefixDB".hashCode());
        System.out.println("common_prefixCa".hashCode());

        Map<String, String> hashmap = new HashMap<>();
        hashmap.put("Aa", "Ahmed");
        hashmap.put("BB", "Rezk");
        System.out.println(hashmap.get("Aa"));
        System.out.println(hashmap.get("BB"));
    }

    public void add(K key, V item) {
        int hash = hash(key);
        for (Node<K, V> node = st[hash]; node != null; node = node.next) {
            if (node.value.equals(key)) {
                node.value = item;
                return;
            }
        }
        Node<K, V> node = new Node(key, item);
        node.next = st[hash];
        st[hash] = node;
        size++;
    }

    private int hash(K key) {
        return key.hashCode() & 0x7fffffff % CAPACITY;
    }

    public V get(K key) {
        if (key == null) return null;
        for (Node node = st[hash(key)]; node != null; node = node.next) {
            if (key.equals(node.key))
                return (V) node.value;
        }
        return null;
    }

    public int size() {
        return size;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
