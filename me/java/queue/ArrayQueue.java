package java.queue;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 09-Mar-18
 * @time 18:31
 */

@SuppressWarnings({"WeakerAccess", "Duplicates"})
public class ArrayQueue extends AbstractQueue {

    private ArrayQueue(int size) {
        super(size);
    }

    public ArrayQueue() {
        this(0);
    }

    private int head = 0, tail = 0;
    private Object[] elements = new Object[10];

    @Override
    protected void enqueueBody(Object element) {
        ensureCapacity(size() + 1);
        elements[tail] = element;
        tail = next(tail);
    }

    @Override
    protected Object dequeueBody() {
        Object first = elements[head];
        elements[head] = null;
        head = next(head);
        return first;
    }

    @Override
    protected Object elementBody() {
        return elements[head];
    }

    @Override
    protected void clearBody() {
        head = tail = 0;
        elements = new Object[10];
    }

    @Override
    public Queue copy() {
        ArrayQueue copy = new ArrayQueue(size());
        copy.elements = new Object[elements.length];
        System.arraycopy(elements, 0, copy.elements, 0, elements.length);
        copy.head = head;
        copy.tail = tail;

        return copy;
    }

    private int next(int x) {
        return (x + 1) % elements.length;
    }

    private void ensureCapacity(int capacity) {
        // size <= elements.length
        if (capacity <= elements.length) {
            return;
        }
        // size == elements.length

        Object[] newElements = new Object[capacity * 2];
        System.arraycopy(elements, head, newElements, 0, elements.length - head);
        System.arraycopy(elements, 0, newElements, elements.length - head, head);
        elements = newElements;
        head = 0;
        tail = size();
    }

}