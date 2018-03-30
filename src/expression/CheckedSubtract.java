package expression;

import expression.exceptions.EvaluationException;
import expression.exceptions.OverflowException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 10-Mar-18
 * @time 11:25
 */

public class CheckedSubtract extends AbstractBinaryOperation {

    public CheckedSubtract(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected void check(int left, int right) throws EvaluationException {
        if (left >= 0 && right < 0 && left - Integer.MAX_VALUE > right ||
                left < 0 && right > 0 && Integer.MIN_VALUE - left > -right) {
            throw new OverflowException();
        }
    }

    @Override
    protected void check(double left, double right) throws EvaluationException {
        if (left >= 0 && right < 0 && left - Double.MAX_VALUE > right ||
                left < 0 && right > 0 && Double.MIN_VALUE - left > -right) {
            throw new OverflowException();
        }
    }

    @Override
    protected int eval(int left, int right) {
        return left - right;
    }

    @Override
    protected double eval(double left, double right) {
        return left - right;
    }

}
