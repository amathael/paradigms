package expression;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 22-Mar-18
 * @time 18:58
 */

public class Negate extends UnaryOperation {

    public Negate(CommonExpression argument) {
        super(argument);
    }

    @Override
    protected int eval(int value) {
        return -value;
    }

    @Override
    protected double eval(double value) {
        return -value;
    }

}
