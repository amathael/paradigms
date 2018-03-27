package expression;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 23-Mar-18
 * @time 16:01
 */

public class BitwiseOr extends BinaryOperation {

    public BitwiseOr(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected int eval(int left, int right) {
        return left | right;
    }

    @Override
    protected double eval(double left, double right) {
        throw new NumberFormatException("Unsupported operation for non-integer numbers");
    }

}
