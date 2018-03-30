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

public class Power extends AbstractBinaryOperation {

    public Power(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected void check(int left, int right) throws EvaluationException {
        if (right < 0) {
            throw new IllegalArgumentException();
        }
        int power = 1, p = 0;
        while (p < right) {
            if (power > 0 && left > 0 && power > Integer.MAX_VALUE / left ||
                    power > 0 && left < 0 && power > Integer.MIN_VALUE / left ||
                    power < 0 && left < 0 && power < Integer.MIN_VALUE / left) {
                throw new OverflowException();
            }
            p++;
            power *= left;
        }
    }

    @Override
    protected void check(double left, double right) throws EvaluationException {
        throw new DoubleUnsupportedException();
    }

    @Override
    protected int eval(int left, int right) {
        int power = 1;
        for (int i = 0; i < right; i++) {
            power *= left;
        }
        return power;
    }

    @Override
    protected double eval(double left, double right) {
        return 0;
    }

}
