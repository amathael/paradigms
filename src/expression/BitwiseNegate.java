package expression;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 23-Mar-18
 * @time 16:02
 */

public class BitwiseNegate extends UnaryOperation {

    public BitwiseNegate(CommonExpression argument) {
        super(argument);
    }

    @Override
    protected int eval(int value) {
        return ~value;
    }

    @Override
    protected double eval(double value) {
        throw new NumberFormatException("Unsupported operation for non-integer numbers");
    }

}
