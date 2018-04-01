package expression;

import expression.exceptions.DoubleUnsupportedException;
import expression.exceptions.EvaluationException;

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
    protected int eval(int value) throws EvaluationException {
        return new CheckedLog().eval(value, 10);
    }

    @Override
    protected double eval(double value) throws EvaluationException {
        return new CheckedLog().eval(value, 10);
    }

}
