package queue;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 01-Mar-18
 * @time 20:03
 */

@SuppressWarnings({"WeakerAccess", "Duplicates"})
public class ArrayQueueModule {
    /*
    inv:
        size >= 0 && for.all i = 0..size - 1 : elements[i] != null
     */
    private static int size = 0;
    private static int head = 0, tail = 0;
    private static Object[] elements = new Object[10];

    /*
    pre:
        element != null
    post:
        size' == size + 1 && elements'[size' - 1] == element &&
        for.all i = 0..size' - 2 : elements'[i] == elements[i]
     */
    public static void enqueue(Object element) {
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
    public static Object dequeue() {
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
    public static Object element() {
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
    public static void push(Object element) {
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
    public static Object remove() {
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
    public static Object peek() {
        assert size > 0 : "Queue is empty";
        return elements[prev(tail)];
    }

    /*
    pre:
        true
    post:
        #R == size
     */
    public static int size() {
        return size;
    }

    /*
    pre:
        true
    post:
        #R == (size == 0)
     */
    public static boolean isEmpty() {
        return size == 0;
    }

    /*
    pre:
        true
    post:
        size == 0
     */
    public static void clear() {
        head = tail = size = 0;
        elements = new Object[10];
    }

    private static int next(int x) {
        return (x + 1) % elements.length;
    }

    private static int prev(int x) {
        return (x + elements.length - 1) % elements.length;
    }

    private static void ensureCapacity() {
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