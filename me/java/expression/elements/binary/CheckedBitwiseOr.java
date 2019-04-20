package java.expression.elements.binary;

import java.expression.calc.Calculator;
import java.expression.elements.TripleExpression;
import java.expression.exceptions.EvaluationException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 23-Mar-18
 * @time 16:01
 */

public class CheckedBitwiseOr<T> extends AbstractBinaryOperation<T> {

    public CheckedBitwiseOr(TripleExpression<T> left, TripleExpression<T> right, Calculator<T> calc) {
        super(left, right, calc);
    }

    @Override
    protected T eval(T left, T right) throws EvaluationException {
        return calc.or(left, right);
    }

}
