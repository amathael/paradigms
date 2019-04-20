package queue;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 01-Mar-18
 * @time 20:22
 */

@SuppressWarnings({"WeakerAccess", "Duplicates"})
public class ArrayQueueADT {
    /*
    inv:
        queue.size >= 0 && for.all i = 0..queue.size - 1 : queue.elements[i] != null
     */
    private int size = 0;
    private int head = 0, tail = 0;
    private Object[] elements = new Object[10];

    /*
    pre:
        queue != null && element != null
    post:
        queue.size' == queue.size + 1 && queue.elements'[queue.size' - 1] == element &&
        for.all i = 0..queue.size' - 2 : queue.elements'[i] == queue.elements[i]
     */
    public static void enqueue(ArrayQueueADT queue, Object element) {
        assert queue != null : "Queue is null";
        assert element != null : "Element is null";
        ensureCapacity(queue);

        queue.elements[queue.tail] = element;
        queue.tail = next(queue, queue.tail);
        queue.size++;
    }

    /*
    pre:
        queue != null && queue.size > 0
    post:
        #R == queue.elements[0] && queue.size' == queue.size - 1 &&
        for.all i = 0..queue.size' - 1 : queue.elements'[i] == queue.elements[i + 1]
     */
    public static Object dequeue(ArrayQueueADT queue) {
        assert queue != null : "Queue is null";
        assert queue.size > 0 : "Queue is empty";
        Object first = queue.elements[queue.head];
        queue.elements[queue.head] = null;
        queue.head = next(queue, queue.head);
        queue.size--;
        return first;
    }

    /*
    pre:
        queue != null && queue.size > 0
    post:
        #R == queue.elements[0] && queue.size' == queue.size
     */
    public static Object element(ArrayQueueADT queue) {
        assert queue != null : "Queue is null";
        assert queue.size > 0 : "Queue is empty";
        return queue.elements[queue.head];
    }

    /*
    pre:
        queue != null && element != null
    post:
        queue.size' == queue.size + 1 && queue.elements'[0] == element &&
        for.all i = 1..queue.size' - 1 : queue.elements'[i] == queue.elements[i - 1]
     */
    public static void push(ArrayQueueADT queue, Object element) {
        assert queue != null : "Queue is null";
        assert element != null : "Element is null";
        ensureCapacity(queue);

        queue.head = prev(queue, queue.head);
        queue.elements[queue.head] = element;
        queue.size++;
    }

    /*
    pre:
        queue != null && queue.size > 0
    post:
        queue.size' == queue.size - 1 && #R == queue.elements[size - 1] &&
        for.all i = 0..queue.size' - 1 : queue.elements'[i] == queue.elements[i]
     */
    public static Object remove(ArrayQueueADT queue) {
        assert queue != null : "Queue is null";
        assert queue.size > 0 : "Queue is empty";
        queue.tail = prev(queue, queue.tail);
        Object last = queue.elements[queue.tail];
        queue.elements[queue.tail] = null;
        queue.size--;
        return last;
    }

    /*
    pre:
        queue != null && queue.size > 0
    post:
        #R == queue.elements[size - 1] && queue.size' == queue.size
     */
    public static Object peek(ArrayQueueADT queue) {
        assert queue != null : "Queue is null";
        assert queue.size > 0 : "Queue is empty";
        return queue.elements[prev(queue, queue.tail)];
    }

    /*
    pre:
        queue != null
    post:
        #R == queue.size
     */
    public static int size(ArrayQueueADT queue) {
        assert queue != null : "Queue is empty";
        return queue.size;
    }

    /*
    pre:
        queue != null
    post:
        #R == (queue.size == 0)
     */
    public static boolean isEmpty(ArrayQueueADT queue) {
        assert queue != null : "Queue is null";
        return queue.size == 0;
    }

    /*
    pre:
        queue != null
    post:
        queue.size == 0
     */
    public static void clear(ArrayQueueADT queue) {
        assert queue != null : "Queue is null";
        queue.head = queue.tail = queue.size = 0;
        queue.elements = new Object[10];
    }

    private static int next(ArrayQueueADT queue, int x) {
        return (x + 1) % queue.elements.length;
    }

    private static int prev(ArrayQueueADT queue, int x) {
        return (x + queue.elements.length - 1) % queue.elements.length;
    }

    private static void ensureCapacity(ArrayQueueADT queue) {
        // size <= elements.length
        if (size(queue) < queue.elements.length) {
            return;
        }
        // size == elements.length

        Object[] newElements = new Object[queue.size * 2];
        System.arraycopy(queue.elements, queue.head, newElements, 0, queue.elements.length - queue.head);
        System.arraycopy(queue.elements, 0, newElements, queue.elements.length - queue.head, queue.head);
        queue.elements = newElements;
        queue.head = 0;
        queue.tail = queue.size;
    }
}