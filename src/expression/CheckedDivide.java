package expression;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.DoublePrecisionLossException;
import expression.exceptions.EvaluationException;
import expression.exceptions.OverflowException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 10-Mar-18
 * @time 11:29
 */

public class CheckedDivide extends AbstractBinaryOperation {

    public CheckedDivide(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected int eval(int left, int right) throws EvaluationException {
        if (right == 0) {
            throw new DivisionByZeroException();
        }
        if (left == Integer.MIN_VALUE && right == -1) {
            throw new OverflowException();
        }
        return left / right;
    }

    @Override
    protected double eval(double left, double right) throws EvaluationException {
        if (right == 0) {
            throw new DivisionByZeroException();
        }
        double absLeft = left >= 0 ? left : -left, absRight = right > 0 ? right : -right;
        if (absRight < 1 && Double.MAX_VALUE * absRight < absLeft) {
            throw new OverflowException();
        }
        if (absRight > 1 && Double.MIN_VALUE * absRight > absLeft) {
            throw new DoublePrecisionLossException();
        }
        return left / right;
    }

}
