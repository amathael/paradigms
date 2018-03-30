package expression;

import expression.exceptions.DoubleUnsupportedException;
import expression.exceptions.EvaluationException;
import expression.exceptions.IllegalArgumentException;
import expression.exceptions.OverflowException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 30-Mar-18
 * @time 16:42
 */

public class CheckedPower10 extends AbstractUnaryOperation {

    public CheckedPower10(CommonExpression argument) {
        super(argument);
    }

    @Override
    protected void check(int value) throws EvaluationException {
        if (value < 0) {
            throw new IllegalArgumentException();
        }
        int power = 1, p = 0;
        while (p < value) {
            if (power > Integer.MAX_VALUE / 10) {
                throw new OverflowException();
            }
            power *= 10;
            p++;
        }
    }

    @Override
    protected void check(double value) throws EvaluationException {
        throw new DoubleUnsupportedException();
    }

    @Override
    protected int eval(int value) {
        int power = 1;
        for (int i = 0; i < value; i++) {
            power *= 10;
        }
        return power;
    }

    @Override
    protected double eval(double value) {
        return 0;
    }

}
