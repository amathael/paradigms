package expression;

import expression.exceptions.DoubleUnsupportedException;
import expression.exceptions.EvaluationException;
import expression.exceptions.IllegalArgumentException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 30-Mar-18
 * @time 10:35
 */

@SuppressWarnings("WeakerAccess")
public class CheckedLog extends AbstractBinaryOperation {

    CheckedLog() {
        super();
    }

    public CheckedLog(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected int eval(int left, int right) throws EvaluationException {
        if (left <= 0 || right <= 1) {
            throw new IllegalArgumentException();
        }
        int power = 1, p = 0, limit = Integer.MAX_VALUE / right;
        while (power <= limit && power * right <= left) {
            power *= right;
            p++;
        }
        return p;
    }

    @Override
    protected double eval(double left, double right) throws EvaluationException {
        throw new DoubleUnsupportedException();
    }

}
