package queue;
/**
 * Created by isuca in paradigms catalogue
 *
 * @date 09-Mar-18
 * @time 02:42
 */

import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("WeakerAccess")
public abstract class AbstractQueue implements Queue {

    private int size;

    protected AbstractQueue(int size) {
        this.size = size;
    }

    protected abstract void enqueueBody(Object element);

    @Override
    public void enqueue(Object element) {
        assert element != null : "Element is null";
        enqueueBody(element);
        size++;
    }

    protected abstract Object dequeueBody();

    @Override
    public Object dequeue() {
        assert size > 0 : "Queue is empty";
        size--;
        return dequeueBody();
    }

    protected abstract Object elementBody();

    @Override
    public Object element() {
        assert size > 0 : "Queue is empty";
        return elementBody();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    protected abstract void clearBody();

    @Override
    public void clear() {
        clearBody();
        size = 0;
    }

    @Override
    public abstract Queue copy();

    @Override
    public Queue filter(Predicate<Object> predicate) {
        assert predicate != null : "Predicate is null";

        Queue filtered = copy();
        int size = filtered.size();
        for (int i = 0; i < size; i++) {
            Object first = filtered.dequeue();
            if (predicate.test(first)) {
                filtered.enqueue(first);
            }
        }

        return filtered;
    }

    @Override
    public Queue map(Function<Object, Object> function) {
        assert function != null : "Function is null";

        Queue mapped = copy();
        // inv: m.size()' == m.size()
        for (int i = 0; i < mapped.size(); i++) {
            mapped.enqueue(function.apply(mapped.dequeue()));
        }
        return mapped;
    }

}
