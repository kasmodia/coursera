public class ArrayStack {
    private String[] array;
    private int first = -1;

    public ArrayStack() {
        array = new String[2];
    }

    public void push(String element) {
        if (first == array.length - 1)
            resize();
        array[++first] = element;
    }

    public String pop() {
        if (isEmpty())
            throw new Error("Empty stack");
        return array[first--];
    }

    public boolean isEmpty() {
        return first == -1;
    }

    private void resize() {
        System.out.println("resizing ..");
        String[] newArray = new String[array.length * 2];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        array = newArray;
    }

    public static void main(String[] args) {
        ArrayStack s = new ArrayStack();
        s.push("value1");
        s.push("value2");
        s.push("value3");
        s.push("value4");
        s.push("value5");
        s.push("value6");
        s.push("value7");
        s.push("value8");
        s.push("value9");


        while(!s.isEmpty()) {
            System.out.println(s.pop());
        }
    }
}