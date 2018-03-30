package expression;

import expression.exceptions.DoubleUnsupportedException;
import expression.exceptions.EvaluationException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 10-Mar-18
 * @time 11:13
 */

@SuppressWarnings("WeakerAccess")
public abstract class AbstractBinaryOperation implements CommonExpression {

    private final CommonExpression left, right;

    public AbstractBinaryOperation(CommonExpression left, CommonExpression right) {
        this.left = left;
        this.right = right;
    }

    protected abstract void check(int left, int right) throws EvaluationException;

    protected abstract void check(double left, double right) throws EvaluationException;

    protected abstract int eval(int left, int right);

    protected abstract double eval(double left, double right);

    @Override
    public int evaluate(int variable) throws EvaluationException {
        int l = left.evaluate(variable), r = right.evaluate(variable);
        check(l, r);
        return eval(l, r);
    }

    @Override
    public double evaluate(double variable) throws EvaluationException {
        double l = left.evaluate(variable), r = right.evaluate(variable);
        check(l, r);
        return eval(l, r);
    }

    @Override
    public int evaluate(int x, int y, int z) throws EvaluationException {
        int l = left.evaluate(x, y, z), r = right.evaluate(x, y, z);
        check(l, r);
        return eval(l, r);
    }

}
