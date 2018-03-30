package expression;

import expression.exceptions.DoubleUnsupportedException;
import expression.exceptions.IllegalArgumentException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 30-Mar-18
 * @time 10:35
 */

public class Checked extends AbstractBinaryOperation {

    public Checked(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected void check(int left, int right) throws IllegalArgumentException {
        if (left <= 0 || right <= 1) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    protected void check(double left, double right) throws DoubleUnsupportedException {
        throw new DoubleUnsupportedException();
    }

    @Override
    protected int eval(int left, int right) {
        int power = 1, p = 0;
        while (power * left <= right && power <= Integer.MAX_VALUE / left) {
            power *= left;
            p++;
        }
        return p;
    }

    @Override
    protected double eval(double left, double right) {
        return 0;
    }

}
