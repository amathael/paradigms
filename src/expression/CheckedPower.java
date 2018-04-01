package expression;

import expression.exceptions.DoubleUnsupportedException;
import expression.exceptions.EvaluationException;
import expression.exceptions.IllegalArgumentException;
import expression.exceptions.OverflowException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 30-Mar-18
 * @time 10:35
 */

public class CheckedPower extends AbstractBinaryOperation {

    CheckedPower() {
        super();
    }

    public CheckedPower(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected int eval(int left, int right) throws EvaluationException {
        if (left == 0 && right == 0) {
            throw new IllegalArgumentException();
        }
        if (right == 0) {
            return 1;
        } else if (right == 1) {
            return left;
        } else {
            if (right % 2 == 1) {
                return new CheckedMultiply().eval(eval(left, right - 1), left);
            } else {
                int half = eval(left, right / 2);
                return new CheckedMultiply().eval(half, half);
            }
        }
    }

    @Override
    protected double eval(double left, double right) throws EvaluationException {
        throw new DoubleUnsupportedException();
    }

}
