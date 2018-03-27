package expression;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 10-Mar-18
 * @time 11:29
 */

public class Divide extends BinaryOperation {

    public Divide(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected int eval(int left, int right) {
        return left / right;
    }

    @Override
    protected double eval(double left, double right) {
        return left / right;
    }

}
