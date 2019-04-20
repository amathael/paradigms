package java.expression.elements;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 10-Mar-18
 * @time 11:01
 */

public class Variable<T> implements TripleExpression<T> {

    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        if ("x".equals(name)) {
            return x;
        } else if ("y".equals(name)) {
            return y;
        } else {
            return z;
        }
    }

}
