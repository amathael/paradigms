package java.expression.elements.unary;

import java.expression.calc.Calculator;
import java.expression.elements.TripleExpression;
import java.expression.exceptions.EvaluationException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 19-Mar-18
 * @time 12:50
 */

public abstract class AbstractUnaryOperation<T> implements TripleExpression<T> {

    private final TripleExpression<T> argument;
    protected final Calculator<T> calc;

    public AbstractUnaryOperation(TripleExpression<T> argument, Calculator<T> calc) {
        this.argument = argument;
        this.calc = calc;
    }

    protected abstract T eval(T value) throws EvaluationException;

    @Override
    public T evaluate(T x, T y, T z) throws EvaluationException {
        return eval(argument.evaluate(x, y, z));
    }

}
