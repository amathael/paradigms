package expression;

import expression.exceptions.DoubleUnsupportedException;
import expression.exceptions.EvaluationException;
import expression.exceptions.IllegalArgumentException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 30-Mar-18
 * @time 16:42
 */

public class CheckedPower10 extends AbstractUnaryOperation {

    public CheckedPower10(CommonExpression argument) {
        super(argument);
    }

    @Override
    protected int eval(int value) throws EvaluationException {
        if (value < 0) {
            throw new IllegalArgumentException();
        }
        return new CheckedPower().eval(10, value);
    }

    @Override
    protected double eval(double value) throws EvaluationException {
        throw new DoubleUnsupportedException();
    }

}
