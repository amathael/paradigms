package expression;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 10-Mar-18
 * @time 11:13
 */

@SuppressWarnings("WeakerAccess")
public abstract class BinaryOperation implements CommonExpression {

    private final CommonExpression left, right;

    public BinaryOperation(CommonExpression left, CommonExpression right) {
        this.left = left;
        this.right = right;
    }

    protected abstract int eval(int left, int right);

    protected abstract double eval(double left, double right);

    @Override
    public int evaluate(int variable) {
        return eval(left.evaluate(variable), right.evaluate(variable));
    }

    @Override
    public double evaluate(double variable) {
        return eval(left.evaluate(variable), right.evaluate(variable));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return eval(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

}
