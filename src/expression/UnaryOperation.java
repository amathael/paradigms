package expression;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 19-Mar-18
 * @time 12:50
 */

public abstract class UnaryOperation implements CommonExpression {

    private final CommonExpression argument;

    public UnaryOperation(CommonExpression argument) {
        this.argument = argument;
    }

    protected abstract int eval(int value);

    protected abstract double eval(double value);

    @Override
    public double evaluate(double variable) {
        return eval(argument.evaluate(variable));
    }

    @Override
    public int evaluate(int variable) {
        return eval(argument.evaluate(variable));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return eval(argument.evaluate(x, y, z));
    }

}
