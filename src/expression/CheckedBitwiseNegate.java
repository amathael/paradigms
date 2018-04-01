package expression;

import expression.exceptions.DoubleUnsupportedException;
import expression.exceptions.EvaluationException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 23-Mar-18
 * @time 16:02
 */

public class CheckedBitwiseNegate extends AbstractUnaryOperation {

    public CheckedBitwiseNegate(CommonExpression argument) {
        super(argument);
    }

    @Override
    protected int eval(int value) {
        return ~value;
    }

    @Override
    protected double eval(double value) throws EvaluationException {
        throw new DoubleUnsupportedException();
    }

}
