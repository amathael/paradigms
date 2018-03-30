package expression;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.EvaluationException;
import expression.exceptions.OverflowException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 10-Mar-18
 * @time 11:29
 */

public class Divide extends AbstractBinaryOperation {

    public Divide(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected void check(int left, int right) throws EvaluationException {
        if (right == 0) {
            throw new DivisionByZeroException();
        }
        if (left == Integer.MIN_VALUE && right == -1) {
            throw new OverflowException();
        }
    }

    @Override
    protected void check(double left, double right) throws EvaluationException {
        if (right == 0) {
            throw new DivisionByZeroException();
        }
        if (right > -1 && right < 0 || right > 0 && right < -1) {
            double limit = Double.MIN_VALUE * right;
            if (left < 0 && left < limit || left > 0 && left > limit) {
                throw new OverflowException();
            }
        }
    }

    @Override
    protected int eval(int left, int right) {
        return left / right;
    }

    @Override
    protected double eval(double left, double right) {
        return left / right;
    }

}
