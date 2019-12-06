import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] data;
    private Stack spots;
    private int size;

    // unit testing (required)
    public static void main(final String[] args) {
        testIterator();
        testEnqueue();
        testDequeue();
    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        final int capacity = 5;
        data = (Item[]) new Object[capacity];
        spots = new Stack(capacity);
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
        return data[spots.push()];
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

    private void expandSpots() {
        spots = new Stack(data.length);
    }

    private void expandIfNecessary() {
        if (size == data.length) {
            final Item[] newData = (Item[]) new Object[data.length * 2];
            for (int i = 0; i < data.length; i++) {
                newData[i] = data[i];
            }
            data = newData;
            expandSpots();
        }
    }

    private class Stack {
        int[] sData;
        int top;

        Stack(final int size) {
            sData = new int[size];
            for (int i = 0; i < size; i++) {
                sData[i] = i;
            }
            top = size;
            StdRandom.shuffle(sData);
        }

        int peek() {
            return sData[StdRandom.uniform(top)];
        }

        int push() {
            final int randomNext = StdRandom.uniform(top, sData.length);
            swap(top, randomNext);
            return sData[top++];
        }

        void swap(final int a, final int b) {
            final int temp = sData[a];
            sData[a] = sData[b];
            sData[b] = temp;
        }

        int pop() {
            if (isEmpty())
                throw new EmptyStackException();
            return sData[--top];
        }

        boolean isEmpty() {
            return top == 0;
        }

        int size() {
            return sData.length;
        }
    }

    private class RandomIterator<Item> implements Iterator<Item> {
        int[] indices;
        int index;

        public RandomIterator() {
            final int usedSpots = (spots.size()) - spots.top;
            indices = new int[usedSpots];
            for (int i = 0; i < usedSpots; i++) {
                indices[i] = spots.sData[spots.top + i];
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

    private static void testDequeue() {
        final RandomizedQueue<String> queue = new RandomizedQueue<>();
        queue.enqueue("Ahmed");
        queue.dequeue();
        assertEquals(0, queue.size());
    }

    private static void testEnqueue() {
        final RandomizedQueue<String> queue = new RandomizedQueue<>();
        assertEquals(0, queue.size());
        queue.enqueue("Ahmed");
        assertEquals(1, queue.size());
    }

    private static void testIterator() {
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        queue.enqueue("Ahmed");
        Iterator<String> it = queue.iterator();
        assertEquals("Ahmed", it.next());
        assertEquals(1, queue.size());

        queue.enqueue("Rezk");
        it = queue.iterator();
        assertTrue(it.hasNext());
        assertTrue(Arrays.asList("Ahmed", "Rezk").contains(it.next()));
        assertTrue(Arrays.asList("Ahmed", "Rezk").contains(it.next()));
        assertEquals(queue.size(), 2);
        assertFalse(it.hasNext());

        queue = new RandomizedQueue<>();
        queue.enqueue("Ahmed");
        queue.enqueue("Rezk");
        queue.enqueue("Attia");
        queue.enqueue("Mohamed");
        queue.enqueue("Zaki");
        queue.enqueue("Edra");
        queue.enqueue("Shanabo");
        queue.enqueue("Baher");
        queue.enqueue("Shalabi");
        final Iterator<String> it1 = queue.iterator();
        final Iterator<String> it2 = queue.iterator();
        assertNotEquals(it1.next(), it2.next());
    }
}