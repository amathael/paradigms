package expression;

import expression.exceptions.DoublePrecisionLossException;
import expression.exceptions.EvaluationException;
import expression.exceptions.OverflowException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 10-Mar-18
 * @time 11:27
 */

public class CheckedMultiply extends AbstractBinaryOperation {

    CheckedMultiply() {
        super();
    }

    public CheckedMultiply(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected int eval(int left, int right) throws EvaluationException {
        int limit = left > 0 == right > 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        if (right > 0 && limit / left > right || right < 0 && limit / left < right) {
            throw new OverflowException();
        }
        return left * right;
    }

    @Override
    protected double eval(double left, double right) throws EvaluationException {
        double absLeft = left >= 0 ? left : -left, absRight = right > 0 ? right : -right;
        if (absRight < 1 && Double.MIN_VALUE / absRight > absLeft) {
            throw new DoublePrecisionLossException();
        }
        if (absRight > 1 && Double.MAX_VALUE / absRight < absLeft) {
            throw new OverflowException();
        }
        return left * right;
    }

}
