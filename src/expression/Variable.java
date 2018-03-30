package expression;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 10-Mar-18
 * @time 11:01
 */

@SuppressWarnings("FieldCanBeLocal")
public class Variable implements CommonExpression {

    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public int evaluate(int variable) {
        return variable;
    }

    @Override
    public double evaluate(double variable) {
        return variable;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        if ("x".equals(name)) {
            return x;
        } else if ("y".equals(name)) {
            return y;
        } else {
            return z;
        }
    }
}
