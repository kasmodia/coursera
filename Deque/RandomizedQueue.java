import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] data;
    private final UsageStack spots;
    private int size;

    // unit testing (required)
    public static void main(final String[] args) {
        testIterator();
        testSimultaneousIterators();
        testEnqueue();
        testDequeue();
        testSize();
        testIsEmpty();
        testSample();
    }
    
    // construct an empty randomized queue
    public RandomizedQueue() {
        final int capacity = 5;
        data = (Item[]) new Object[capacity];
        spots = new UsageStack(capacity);
    }

    private static void testSimultaneousIterators() {
        final int n = 5;
        final RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        for (int i = 0; i < n; i++)
            queue.enqueue(i);
        for (final int a : queue) {
            for (final int b : queue)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }
    }

    private static void testSize() {
        final RandomizedQueue<String> queue = new RandomizedQueue<>();
        if (queue.size() != 0) throw new TestFailedException("Size must be 0");

        queue.enqueue("item");
        if (queue.size() != 1) throw new TestFailedException("Size must be 1");

        queue.dequeue();
        if (queue.size() != 0) throw new TestFailedException("Size must be 0");
    }

    private static void testSample() {
        final RandomizedQueue<String> queue = new RandomizedQueue<>();
        queue.enqueue("item");
        if (!queue.sample().equals("item")) throw new TestFailedException("Returned value must be 'item'");
    }

    private static void testIsEmpty() {
        final RandomizedQueue<String> queue = new RandomizedQueue<>();
        if (!queue.isEmpty()) throw new TestFailedException("Queue must be empty");

        queue.enqueue("item");
        if (queue.isEmpty()) throw new TestFailedException("Queue must not be empty");

        queue.dequeue();
        if (!queue.isEmpty()) throw new TestFailedException("Queue must be empty");
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(final Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        expandIfNecessary();
        data[spots.pop()] = item;
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();
        size--;
        final int freeSpot = spots.push();
        final Item item = data[freeSpot];
        data[freeSpot] = null;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException();
        return data[spots.peek()];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator<Item>();
    }

    private void expandIfNecessary() {
        if (size == data.length) {
            final Item[] newData = (Item[]) new Object[data.length * 2];
            for (int i = 0; i < data.length; i++) {
                newData[i] = data[i];
            }
            data = newData;
            spots.expandSpots();
        }
    }

    private static void testDequeue() {
        final RandomizedQueue<String> queue = new RandomizedQueue<>();
        queue.enqueue("Ahmed");
        final String item = queue.dequeue();
        if (!queue.isEmpty()) throw new TestFailedException("Queue must be empty");
        if (!item.equals("Ahmed")) throw new TestFailedException("Returned value must be 'Ahmed'");
    }

    private static void testEnqueue() {
        final RandomizedQueue<String> queue = new RandomizedQueue<>();
        if (!queue.isEmpty()) throw new TestFailedException("Queue must be empty");
        queue.enqueue("Ahmed");
        if (queue.size() != 1) throw new TestFailedException("Queue must have size 1");
    }

    private static void testIterator() {
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        queue.enqueue("Ahmed");
        Iterator<String> it = queue.iterator();
        if (!it.next().equals("Ahmed")) throw new TestFailedException("Value must be 'Ahmed'");
        if (queue.size() != 1) throw new TestFailedException("Queue must have size 1");

        queue.enqueue("Rezk");
        it = queue.iterator();
        if (!it.hasNext()) throw new TestFailedException("Queue must have next");        
        if (queue.size() != 2) throw new TestFailedException("Queue must have size 2");
        it.next();
        it.next();
        if (it.hasNext()) throw new TestFailedException("Queue must not have next");

        queue = new RandomizedQueue<>();
        queue.enqueue("Shanabo");
        queue.enqueue("Baher");
        queue.enqueue("Shalabi");
        queue.enqueue("Jackson");
        queue.enqueue("Ali");
        queue.enqueue("Shanabo");
        queue.enqueue("Taleb");
        queue.enqueue("Basha");
        queue.enqueue("Shleta");
        queue.enqueue("Kal");
        final Iterator<String> it1 = queue.iterator();
        final Iterator<String> it2 = queue.iterator();
        boolean notEquals = false;
        for (int i = 0; i < 10; i++) {
            if (!it1.next().equals(it2.next())) notEquals = true;
        }
        if (!notEquals) throw new TestFailedException("Values must not be equal");
    }

    private class UsageStack {
        int[] spotsData;
        int top;

        UsageStack(final int size) {
            spotsData = new int[size];
            for (int i = 0; i < size; i++) {
                spotsData[i] = i;
            }
            top = size;
            StdRandom.shuffle(spotsData);
        }

        public void expandSpots() {
            final int[] newSData = new int[size * 2];
            for (int i = 0; i < size; i++) {
                newSData[i] = i + size;
            }
            for (int i = size; i < size * 2; i++) {
                newSData[i] = spotsData[i - size];
            }
            spotsData = newSData;
            StdRandom.shuffle(spotsData, 0, size);
            top = size;
        }

        int peek() {
            return spotsData[StdRandom.uniform(top, size())];
        }

        int push() {
            final int randomNext = StdRandom.uniform(top, spotsData.length);
            swap(top, randomNext);
            return spotsData[top++];
        }

        void swap(final int a, final int b) {
            final int temp = spotsData[a];
            spotsData[a] = spotsData[b];
            spotsData[b] = temp;
        }

        int pop() {
            if (isEmpty())
                throw new EmptyStackException();
            return spotsData[--top];
        }

        boolean isEmpty() {
            return top == 0;
        }

        int size() {
            return spotsData.length;
        }
    }

    private class RandomIterator<Item> implements Iterator<Item> {
        int[] indices;
        int index;
        
        RandomIterator() {
            final int usedSpots = (spots.size()) - spots.top;
            indices = new int[usedSpots];
            for (int i = 0; i < usedSpots; i++) {
                indices[i] = spots.spotsData[spots.top + i];
            }
            StdRandom.shuffle(indices);
        }

        public boolean hasNext() {
            return index < indices.length;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return (Item) data[indices[index++]];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}