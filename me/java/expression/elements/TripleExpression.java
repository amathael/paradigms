package java.expression.elements;

import java.expression.exceptions.EvaluationException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 16-Mar-18
 * @time 15:29
 */

public interface TripleExpression<T> {

    public T evaluate(T x, T y, T z) throws EvaluationException;

}
