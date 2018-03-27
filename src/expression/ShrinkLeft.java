package expression;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 19-Mar-18
 * @time 13:08
 */

public class ShrinkLeft extends BinaryOperation {

    public ShrinkLeft(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected int eval(int left, int right) {
        return left << right;
    }

    @Override
    protected double eval(double left, double right) {
        throw new NumberFormatException("Unsupported operation for non-integer numbers");
    }

}
