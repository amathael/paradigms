package java.expression.elements.binary;

import java.expression.calc.Calculator;
import java.expression.elements.TripleExpression;
import java.expression.exceptions.EvaluationException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 10-Mar-18
 * @time 11:13
 */

public abstract class AbstractBinaryOperation<T> implements TripleExpression<T> {

    private final TripleExpression<T> left, right;
    protected final Calculator<T> calc;

    public AbstractBinaryOperation(TripleExpression<T> left, TripleExpression<T> right, Calculator<T> calc) {
        this.left = left;
        this.right = right;
        this.calc = calc;
    }

    protected abstract T eval(T left, T right) throws EvaluationException;

    @Override
    public T evaluate(T x, T y, T z) throws EvaluationException {
        return eval(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

}
