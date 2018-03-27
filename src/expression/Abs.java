package expression;

import static java.lang.Math.abs;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 22-Mar-18
 * @time 19:52
 */

public class Abs extends UnaryOperation {

    public Abs(CommonExpression argument) {
        super(argument);
    }

    @Override
    protected int eval(int value) {
        return abs(value);
    }

    @Override
    protected double eval(double value) {
        return abs(value);
    }

}
