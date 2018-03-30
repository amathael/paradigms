package expression;

import expression.exceptions.DoubleUnsupportedException;
import expression.exceptions.EvaluationException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 23-Mar-18
 * @time 16:01
 */

public class CheckedBitwiseOr extends AbstractBinaryOperation {

    public CheckedBitwiseOr(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected void check(int left, int right) {

    }

    @Override
    protected void check(double left, double right) throws DoubleUnsupportedException {
        throw new DoubleUnsupportedException();
    }

    @Override
    protected int eval(int left, int right) {
        return left | right;
    }

    @Override
    protected double eval(double left, double right) {
        return 0;
    }

}
