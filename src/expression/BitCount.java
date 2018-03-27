package expression;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 23-Mar-18
 * @time 16:15
 */

public class BitCount extends UnaryOperation {

    public BitCount(CommonExpression argument) {
        super(argument);
    }

    @Override
    protected int eval(int value) {
        return Integer.bitCount(value);
    }

    @Override
    protected double eval(double value) {
        throw new NumberFormatException("Unsupported operation for non-integer numbers");
    }

}
