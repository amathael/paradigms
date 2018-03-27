package expression;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 10-Mar-18
 * @time 11:04
 */

public class Add extends BinaryOperation {

    public Add(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected int eval(int left, int right) {
        return left + right;
    }

    @Override
    protected double eval(double left, double right) {
        return left + right;
    }

}
