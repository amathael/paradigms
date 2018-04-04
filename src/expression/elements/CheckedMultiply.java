package expression.elements;

import expression.calc.Calculator;
import expression.exceptions.PrecisionLossException;
import expression.exceptions.EvaluationException;
import expression.exceptions.OverflowException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 10-Mar-18
 * @time 11:27
 */

public class CheckedMultiply<T> extends AbstractBinaryOperation<T> {

    public CheckedMultiply(TripleExpression<T> left, TripleExpression<T> right, Calculator<T> calc) {
        super(left, right, calc);
    }

    @Override
    protected T eval(T left, T right) throws EvaluationException {
        return calc.mul(left, right);
    }

}
