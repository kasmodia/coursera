import java.util.EmptyStackException;

public class LinkedListStack {
    private Node head;

    public void push(final String value) {
        Node newNode = new Node();
        newNode.value = value;
        newNode.next = head;
        head = newNode;
    }

    public String pop() {
        if (isEmpty())
            throw new Error("Empty stack");
        String item = head.value;
        head = head.next;
        return item;
    }

    public boolean isEmpty() {
        return head == null;
    }

    private static class Node {
        String value;
        Node next;
    }

    public static void main(String[] args) {
        LinkedListStack s = new LinkedListStack();
        s.push("value1");
        s.push("value2");
        s.pop();
        s.push("value3");

        while(!s.isEmpty()) {
            System.out.println(s.pop());
        }
    }
}