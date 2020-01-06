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
    public void addFirst(final Item item) {
        final Node<Item> newNode = new Node<>(item);
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
    public void addLast(final Item item) {
        final Node<Item> newNode = new Node<>(item);
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
        final Node<Item> result = first;
        first = first.next;
        return result.item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException();
        size--;
        final Node<Item> result = last;
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
                final Item result = next.item;
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

        public Node(final Item item) {
            this.item = item;
        }
    }

    // unit testing (required)
    public static void main(final String[] args) {
        testSize();
        testIsEmpty();
        testIterator();
        testAddFirst();
        testAddLast();
        testRemoveFirst();
        testRemoveLast();
        testAddFirstAndRemoveLast();
    }

    private static void testRemoveLast() {
        final Deque<Integer> d = new Deque<>();
        
        Integer item = d.removeLast();
        if (item != null)
            throw new TestFailedException("EmptyStackException must be thrown");
        
        d.addFirst(1);
        d.removeLast();
        if (!d.isEmpty())
            throw new TestFailedException("Queue must be empty");
    }

    private static void testRemoveFirst() {
        final Deque<Integer> d = new Deque<>();
        Integer item = d.removeFirst();
        if (item != null)
            throw new TestFailedException("EmptyStackException must be thrown");
            
        d.addFirst(1);
        d.removeFirst();
        if (!d.isEmpty())
            throw new TestFailedException("Queue must be empty");
    }

    private static void testAddLast() {
        final Deque<Integer> d = new Deque<>();
        d.addLast(1);
        if (d.isEmpty())
            throw new TestFailedException("Queue must not be empty");
    }

    private static void testAddFirst() {
        final Deque<Integer> d = new Deque<>();
        d.addFirst(1);
        if (d.isEmpty())
            throw new TestFailedException("Queue must not be empty");
    }

    private static void testIterator() {
        final Deque<Integer> dequeue = new Deque<>();
        dequeue.addFirst(1);
        final Iterator<Integer> it = dequeue.iterator();
        if (!it.hasNext())
            throw new TestFailedException("Iterator must have next");

        dequeue.addLast(2);
        for (int i = 0; i < 2; i++) {
            if (!it.hasNext())
                throw new TestFailedException("Iterator must have next");
            it.next();
        }

        dequeue.removeFirst();
        dequeue.removeLast();

        if (it.hasNext())
            throw new TestFailedException("Iterator must not have next");

        dequeue.addLast(1);
        dequeue.addLast(2);
        dequeue.addLast(3);
        dequeue.addLast(4);
        dequeue.addLast(5);

        final Iterator<Integer> iterator1 = dequeue.iterator();
        for (int i = 0; i < dequeue.size(); i++) {
            final int item1 = iterator1.next();
            final Iterator<Integer> iterator2 = dequeue.iterator();
            for (int j = 0; j < dequeue.size(); j++) {
                if (item1 - i + j != iterator2.next())
                    throw new TestFailedException("Iterators should be independent");
            }
        }
    }

    private static void testIsEmpty() {
        final Deque<Integer> d = new Deque<>();
        if (!d.isEmpty())
            throw new TestFailedException("Queue must be empty");

        d.addFirst(1);
        if (d.isEmpty())
            throw new TestFailedException("Queue must not be empty");

        d.removeFirst();
        if (!d.isEmpty())
            throw new TestFailedException("Queue must be empty");

        d.addLast(1);
        if (d.isEmpty())
            throw new TestFailedException("Queue must not be empty");

        d.removeLast();
        if (!d.isEmpty())
            throw new TestFailedException("Queue must be empty");
    }

    private static void testSize() {
        final Deque<Integer> d = new Deque<>();
        if (d.size() != 0)
            throw new TestFailedException("Size must me 0");

        d.addFirst(1);
        if (d.size() != 1)
            throw new TestFailedException("Size must me 1");

        d.addLast(2);
        if (d.size() != 2)
            throw new TestFailedException("Size must me 2");

        d.removeFirst();
        if (d.size() != 1)
            throw new TestFailedException("Size must me 1");

        d.removeLast();
        if (d.size() != 0)
            throw new TestFailedException("Size must me 0");
    }

    private static void testAddFirstAndRemoveLast() {
        final Deque<Integer> d = new Deque<>();
        for (int i = 0; i < 10; i++) {
            d.addFirst(i);
        }

        for (int i = 0; i < 10; i++) {
            if (d.removeLast() != i)
                throw new TestFailedException("removeLast() must return " + i);
        }
    }
}