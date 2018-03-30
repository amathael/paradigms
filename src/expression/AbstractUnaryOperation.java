package expression;

import expression.exceptions.DoubleUnsupportedException;
import expression.exceptions.EvaluationException;
import expression.exceptions.OverflowException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 19-Mar-18
 * @time 12:50
 */

@SuppressWarnings("WeakerAccess")
public abstract class AbstractUnaryOperation implements CommonExpression {

    private final CommonExpression argument;

    public AbstractUnaryOperation(CommonExpression argument) {
        this.argument = argument;
    }

    protected abstract void check(int value) throws EvaluationException;

    protected abstract void check(double value) throws EvaluationException;

    protected abstract int eval(int value);

    protected abstract double eval(double value);

    @Override
    public int evaluate(int variable) throws EvaluationException {
        int value = argument.evaluate(variable);
        check(value);
        return eval(value);
    }

    @Override
    public double evaluate(double variable) throws EvaluationException {
        double value = argument.evaluate(variable);
        check(value);
        return eval(value);
    }

    @Override
    public int evaluate(int x, int y, int z) throws EvaluationException {
        int value = argument.evaluate(x, y, z);
        check(value);
        return eval(value);
    }

}
