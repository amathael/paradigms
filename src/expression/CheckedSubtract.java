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
    protected int eval(int left, int right) throws EvaluationException {
        if (left >= 0 && left - Integer.MAX_VALUE > right || left < 0 && left - Integer.MIN_VALUE < right) {
            throw new OverflowException();
        }
        return left - right;
    }

    @Override
    protected double eval(double left, double right) throws EvaluationException {
        if (left > 0 && left - Double.MAX_VALUE > right || left < 0 && left + Double.MAX_VALUE < right) {
            throw new OverflowException();
        }
        return left - right;
    }

}
