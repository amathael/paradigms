package expression;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 22-Mar-18
 * @time 19:55
 */

public class Square extends UnaryOperation {

    public Square(CommonExpression argument) {
        super(argument);
    }

    @Override
    protected int eval(int value) {
        return value * value;
    }

    @Override
    protected double eval(double value) {
        return value * value;
    }

}
