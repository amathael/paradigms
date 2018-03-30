package expression;

import expression.exceptions.EvaluationException;
import expression.exceptions.OverflowException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 10-Mar-18
 * @time 11:27
 */

public class Multiply extends AbstractBinaryOperation {

    public Multiply(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected void check(int left, int right) throws EvaluationException {
        if (left > 0 && right > 0 && Integer.MAX_VALUE / left > right ||
                left > 0 && right < 0 && Integer.MIN_VALUE / right > left ||
                left < 0 && right > 0 && Integer.MIN_VALUE / left > right ||
                left < 0 && right < 0 && Integer.MAX_VALUE / left > right) {
            throw new OverflowException();
        }
    }

    @Override
    protected void check(double left, double right) throws EvaluationException {
        double l = left > 0 ? left : -left, r = right > 0 ? right : -right;
        if (left > Double.MAX_VALUE / right) {
            throw new OverflowException();
        }
    }

    @Override
    protected int eval(int left, int right) {
        return left * right;
    }

    @Override
    protected double eval(double left, double right) {
        return left * right;
    }

}
