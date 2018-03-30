package expression;

import expression.exceptions.EvaluationException;
import expression.exceptions.OverflowException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 22-Mar-18
 * @time 18:58
 */

public class CheckedNegate extends AbstractUnaryOperation {

    public CheckedNegate(CommonExpression argument) {
        super(argument);
    }

    @Override
    protected void check(int value) throws EvaluationException {
        if (value == Integer.MIN_VALUE) {
            throw new OverflowException();
        }
    }

    @Override
    protected void check(double value) {

    }

    @Override
    protected int eval(int value) {
        return -value;
    }

    @Override
    protected double eval(double value) {
        return -value;
    }

}
