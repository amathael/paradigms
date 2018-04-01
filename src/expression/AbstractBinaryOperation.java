package expression;

import expression.exceptions.DoubleUnsupportedException;
import expression.exceptions.EvaluationException;
import expression.exceptions.IllegalArgumentException;

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

    protected AbstractBinaryOperation() {
        this.left = this.right = null;
    }

    protected abstract int eval(int left, int right) throws EvaluationException;

    protected abstract double eval(double left, double right) throws EvaluationException;

    @Override
    public int evaluate(int variable) throws EvaluationException {
        assert left != null && right != null : "Temporary object evaluation is not legal";
        return eval(left.evaluate(variable), right.evaluate(variable));
    }

    @Override
    public double evaluate(double variable) throws EvaluationException {
        assert left != null && right != null : "Temporary object evaluation is not legal";
        if (Double.isNaN(variable)) {
            throw new IllegalArgumentException();
        }
        return eval(left.evaluate(variable), right.evaluate(variable));
    }

    @Override
    public int evaluate(int x, int y, int z) throws EvaluationException {
        assert left != null && right != null : "Temporary object evaluation is not legal";
        return eval(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

}
