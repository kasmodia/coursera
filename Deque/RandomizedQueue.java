import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] data;
    private Stack spots;
    private int size;
    private int last;

    // construct an empty randomized queue
    public RandomizedQueue() {
        data = (Item[]) new Object[10];
        spots = new Stack();
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
    public void enqueue(Item item) {
        expandIfNecessary();
        if (!spots.isEmpty()) {
            data[spots.pop()] = item;
            size++;
            return;
        }
        data[last++] = item;
        size++;
    }

    // remove and return a random item
    private void expandIfNecessary() {
        if (spots.isEmpty()) {
            if (last == data.length) {
                Item[] newData = (Item[]) new Object[data.length * 2];
                for (int i = 0; i < data.length; i++) {
                    newData[i] = data[i];
                }
                data = newData;
            }
        }
    }

    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();

        int next = StdRandom.uniform(data.length);
        Item result = null;
        while (next < data.length) {
            result = data[next];
            if (result != null) {
                break;
            }
            next = StdRandom.uniform(data.length);
        }      
        data[next] = null;
        spots.push(next);
        size--;
        return result;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        return data[StdRandom.uniform(size)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator<Item>();
    }

    private class RandomIterator<Item> implements Iterator<Item> {
        Item[] iData = (Item[]) new Object[data.length];
        int iNext;
        int found;
        public RandomIterator() {
            for (int i = 0; i < data.length; i++) {
                iData[i] = (Item) data[i];
            }
        }
        public boolean hasNext() {
            return found < size;
        }

        public Item next() {
            Item result = null;
            while (iNext < size) {
                if (iData[iNext] != null) {
                    result = iData[iNext++];
                    found++;
                    break;
                }
                iNext++;
            }
            return result;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        queue.enqueue("Ahmed");
        queue.enqueue("Rezk");
        queue.enqueue("Attia");
        queue.dequeue();
        queue.enqueue("Badr");

        Iterator<String> it = queue.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    private class Stack {
        int[] data;
        int last;

        public Stack() {
            data = new int[10];
        }

        void push(int item) {
            expandIfNecessary();
            data[last++] = item;
        }

        int pop() {
            if (isEmpty())
                throw new EmptyStackException();
            return data[--last];
        }

        boolean isEmpty() {
            return last == 0;
        }

        private void expandIfNecessary() {
            if (last == size) {
                int[] newArr = new int[data.length * 2];
                for (int i = 0; i < data.length; i++) {
                    newArr[i] = data[i];
                }
                data = newArr;
            }   
        }
    }
}