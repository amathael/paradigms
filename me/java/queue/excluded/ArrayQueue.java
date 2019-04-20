package queue;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 01-Mar-18
 * @time 18:31
 */

@SuppressWarnings({"WeakerAccess", "Duplicates"})
public class ArrayQueue {
    /*
    inv:
        size >= 0 && for.all i = 0..size - 1 : elements[i] != null
     */
    private int size = 0;
    private int head = 0, tail = 0;
    private Object[] elements = new Object[10];

    /*
    pre:
        element != null
    post:
        size' == size + 1 && elements'[size' - 1] == element &&
        for.all i = 0..size' - 2 : elements'[i] == elements[i]
     */
    public void enqueue(Object element) {
        assert element != null : "Element is null";
        ensureCapacity();

        elements[tail] = element;
        tail = next(tail);
        size++;
    }

    /*
    pre:
        size > 0
    post:
        #R == elements[0] && size' == size - 1 &&
        for.all i = 0..size' - 1 : elements'[i] == elements[i + 1]
     */
    public Object dequeue() {
        assert size > 0 : "Queue is empty";
        Object first = elements[head];
        elements[head] = null;
        head = next(head);
        size--;
        return first;
    }

    /*
    pre:
        size > 0
    post:
        #R == elements[0] && size' == size
     */
    public Object element() {
        assert size > 0 : "Queue is empty";
        return elements[head];
    }

    /*
    pre:
        element != null
    post:
        size' == size + 1 && elements'[0] == element &&
        for.all i = 1..size' - 1 : elements'[i] == elements[i - 1]
     */
    public void push(Object element) {
        assert element != null : "Element is null";
        ensureCapacity();

        head = prev(head);
        elements[head] = element;
        size++;
    }

    /*
    pre:
        size > 0
    post:
        size' == size - 1 && #R == elements[size - 1] &&
        for.all i = 0..size' - 1 : elements'[i] == elements[i]
     */
    public Object remove() {
        assert size > 0 : "Queue is empty";
        tail = prev(tail);
        Object last = elements[tail];
        elements[tail] = null;
        size--;
        return last;
    }

    /*
    pre:
        size > 0
    post:
        #R == elements[size - 1] && size' == size
     */
    public Object peek() {
        assert size > 0 : "Queue is empty";
        return elements[prev(tail)];
    }

    /*
    pre:
        true
    post:
        #R == size
     */
    public int size() {
        return size;
    }

    /*
    pre:
        true
    post:
        #R == (size == 0)
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /*
    pre:
        true
    post:
        size == 0
     */
    public void clear() {
        head = tail = size = 0;
        elements = new Object[10];
    }

    private int next(int x) {
        return (x + 1) % elements.length;
    }

    private int prev(int x) {
        return (x + elements.length - 1) % elements.length;
    }

    private void ensureCapacity() {
        // size <= elements.length
        if (size < elements.length) {
            return;
        }
        // size == elements.length

        Object[] newElements = new Object[size * 2];
        System.arraycopy(elements, head, newElements, 0, elements.length - head);
        System.arraycopy(elements, 0, newElements, elements.length - head, head);
        elements = newElements;
        head = 0;
        tail = size;
    }
}