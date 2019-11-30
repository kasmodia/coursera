import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node<Item> first;
    private Node<Item> last;
    private int size;

    // construct an empty deque
    public Deque() {

    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        Node<Item> newNode = new Node<>(item);
        if (isEmpty()) {
            first = newNode;
            last = newNode;
            size++;
            return;
        }
        newNode.next = first;
        first.prev = newNode;
        first = newNode;
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        Node<Item> newNode = new Node<>(item);
        if (isEmpty()) {
            first = newNode;
            last = newNode;
            size++;
            return;
        }
        last.next = newNode;
        newNode.prev = last;
        last = newNode;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException();
        size--;
        Node<Item> result = first;
        first = first.next;
        return result.item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException();
        size--;
        Node<Item> result = last;
        last = last.prev;
        return result.item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            Node<Item> next = first;
            @Override
            public Item next() {
                if (!hasNext())
                    return null;
                Item result = next.item;
                next = next.next;
                return result;
            }

            @Override
            public boolean hasNext() {
                return next != null;
            }
        };
    }

    private class Node<Item> {
        Node<Item> next;
        Node<Item> prev;
        Item item;

        public Node(Item item) {
            this.item = item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> d = new Deque<>();
        if (!d.isEmpty())
            d.addFirst("Will not be added");
    
        if (d.size() > 0)
            d.removeFirst();

        d.addFirst("Third Item");
        d.addFirst("Second Item");
        d.addFirst("First Item");

        d.removeFirst();

        d.addFirst("First Item");

        d.removeLast();
        
        d.addLast("Third Item");
        d.addLast("Fourth Item");
        d.addLast("Fifth Item");

        d.removeLast();

        d.addLast("Fifth Item");
        d.addLast("Sixth Item");
        Iterator<String> it = d.iterator();
        while (it.hasNext()) 
            System.out.println(it.next());
    }

}