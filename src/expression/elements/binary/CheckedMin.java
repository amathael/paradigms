package expression.elements.binary;

import expression.calc.Calculator;
import expression.elements.TripleExpression;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 06-Apr-18
 * @time 15:34
 */

public class CheckedMin<T> extends AbstractBinaryOperation<T> {

    public CheckedMin(TripleExpression<T> left, TripleExpression<T> right, Calculator<T> calc) {
        super(left, right, calc);
    }

    @Override
    protected T eval(T left, T right) {
        return calc.min(left, right);
    }

}
