package expression;

import expression.exceptions.DoubleUnsupportedException;

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
    protected void check(int value) {

    }

    @Override
    protected void check(double value) throws DoubleUnsupportedException {
        throw new DoubleUnsupportedException();
    }

    @Override
    protected int eval(int value) {
        return ~value;
    }

    @Override
    protected double eval(double value) {
        return 0;
    }

}
