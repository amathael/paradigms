package expression;

import expression.exceptions.DoubleUnsupportedException;
import expression.exceptions.EvaluationException;
import expression.exceptions.IllegalArgumentException;
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

    protected abstract int eval(int value) throws EvaluationException;

    protected abstract double eval(double value) throws EvaluationException;

    @Override
    public int evaluate(int variable) throws EvaluationException {
        return eval(argument.evaluate(variable));
    }

    @Override
    public double evaluate(double variable) throws EvaluationException {
        if (Double.isNaN(variable)) {
            throw new IllegalArgumentException();
        }
        return eval(argument.evaluate(variable));
    }

    @Override
    public int evaluate(int x, int y, int z) throws EvaluationException {
        return eval(argument.evaluate(x, y, z));
    }

}
