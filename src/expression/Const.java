package expression;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 10-Mar-18
 * @time 11:00
 */

public class Const implements CommonExpression {

    private Number value;

    public Const(int intValue) {
        this.value = intValue;
    }

    public Const(double doubleValue) {
        this.value = doubleValue;
    }

    @Override
    public int evaluate(int variable) {
        return value.intValue();
    }

    @Override
    public double evaluate(double variable) {
        return value.doubleValue();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return value.intValue();
    }

}
