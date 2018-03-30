package expression;

import expression.exceptions.DoubleUnsupportedException;
import expression.exceptions.EvaluationException;
import expression.exceptions.IllegalArgumentException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 30-Mar-18
 * @time 16:34
 */

public class CheckedLog10 extends AbstractUnaryOperation {

    public CheckedLog10(CommonExpression argument) {
        super(argument);
    }

    @Override
    protected void check(int value) throws EvaluationException {
        if (value <= 0) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    protected void check(double value) throws EvaluationException {
        throw new DoubleUnsupportedException();
    }

    @Override
    protected int eval(int value) {
        int power = 1, p = 0;
        while (power * 10 <= value && power <= Integer.MAX_VALUE / value) {
            power *= value;
            p++;
        }
        return p;
    }

    @Override
    protected double eval(double value) {
        return 0;
    }

}
