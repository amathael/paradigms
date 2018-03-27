package queue;
/**
 * Created by isuca in paradigms catalogue
 *
 * @date 09-Mar-18
 * @time 03:15
 */

@SuppressWarnings("WeakerAccess")
public class LinkedQueue extends AbstractQueue {

    private LinkedQueue(int size) {
        super(size);
    }

    public LinkedQueue() {
        this(0);
    }

    private class Node {
        Object value;
        Node next;

        private Node(Object value) {
            this.value = value;
            next = null;
        }
    }

    private Node head, tail;

    @Override
    protected void enqueueBody(Object element) {
        if (head == null) {
            head = tail = new Node(element);
        } else {
            tail.next = new Node(element);
            tail = tail.next;
        }
    }

    @Override
    protected Object dequeueBody() {
        Object first = head.value;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        return first;
    }

    @Override
    protected Object elementBody() {
        return head.value;
    }

    @Override
    protected void clearBody() {
        head = tail = null;
    }

    @Override
    public Queue copy() {
        LinkedQueue copy = new LinkedQueue();
        Node current = head;
        while (current != null) {
            copy.enqueue(current.value);
            current = current.next;
        }

        return copy;
    }

}
