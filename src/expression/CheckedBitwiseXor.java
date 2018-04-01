package expression;

import expression.exceptions.DoubleUnsupportedException;
import expression.exceptions.EvaluationException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 23-Mar-18
 * @time 16:02
 */

public class CheckedBitwiseXor extends AbstractBinaryOperation {

    public CheckedBitwiseXor(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected int eval(int left, int right) {
        return left ^ right;
    }

    @Override
    protected double eval(double left, double right) throws EvaluationException {
        throw new DoubleUnsupportedException();
    }

}
