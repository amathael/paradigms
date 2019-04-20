package java.queue;
/**
 * Created by isuca in paradigms catalogue
 *
 * @date 09-Mar-18
 * @time 02:31
 */

import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("UnnecessaryInterfaceModifier")
public interface Queue {
    /*
    inv:
        size() >= 0 && for.all i = 0..size() - 1 : this[i] != null
     */

    /*
    pre:
        element != null
    post:
        size()' == size() + 1 && this'[size()' - 1] == element &&
        for.all i = 0..size()' - 2 : this'[i] == this[i]
     */
    public void enqueue(Object element);

    /*
    pre:
        size() > 0
    post:
        #R == this[0] && size()' == size() - 1 &&
        for.all i = 0..size()' - 1: this'[i] == this[i + 1]
     */
    public Object dequeue();

    /*
    pre:
        size() > 0
    post:
        #R == this[0] && size()' == size()
     */
    public Object element();

    /*
    pre:
        true
    post:
        #R == #number of elements in this
     */
    public int size();

    /*
    pre:
        true
    post:
        #R == (size() == 0)
     */
    public boolean isEmpty();

    /*
    pre:
        true
    post:
        size()' == 0
     */
    public void clear();

    /*
    pre:
        true
    post:
        size()' == size() && for.all i = 0..size() - 1 : this'[i] == this[i] &&
        #R instanceof Queue &&
        #R.size() == size() && for.all i = 0..size() - 1 : #R[i] == this[i]
     */
    public Queue copy();

    /*
    pre:
        predicate != null
    post:
        size()' == size() && for.all i = 0..size() - 1 : this'[i] == this[i] &&
        #R instanceof Queue &&
        exists idx[0..#R.size() - 1] : (
            for.all i in idx : 0 <= i < this.size() &&
            for.all i = 0..#R.size() - 1 : #R[i] == this[idx[i]] &&
            for.all i = 0..#R.size() - 2 : idx[i] < idx[i + 1] &&
            i in idx <=> predicate.test(this[idx[i]]) == true
        )
     */
    public Queue filter(Predicate<Object> predicate);

    /*
    pre:
        function != null
    post:
        size()' == size() && for.all i = 0..size() - 1 : this'[i] == this[i] &&
        #R instanceof Queue &&
        #R.size() == size() && for.all i = 0..size() - 1 : #R[i] == function.apply(this[i])
     */
    public Queue map(Function<Object, Object> function);

}
