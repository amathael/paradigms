package expression.elements;

import expression.calc.Calculator;
import expression.exceptions.OperationUnsupportedException;
import expression.exceptions.EvaluationException;
import expression.exceptions.IllegalArgumentException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 30-Mar-18
 * @time 10:35
 */

@SuppressWarnings("WeakerAccess")
public class CheckedPower<T> extends AbstractBinaryOperation<T> {

    public CheckedPower(TripleExpression<T> left, TripleExpression<T> right, Calculator<T> calc) {
        super(left, right, calc);
    }

    @Override
    protected T eval(T left, T right) throws EvaluationException {
        return calc.pow(left, right);
    }

}
