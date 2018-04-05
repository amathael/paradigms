package expression.elements.binary;

import expression.calc.Calculator;
import expression.elements.TripleExpression;
import expression.exceptions.EvaluationException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 30-Mar-18
 * @time 10:35
 */

public class CheckedPower<T> extends AbstractBinaryOperation<T> {

    public CheckedPower(TripleExpression<T> left, TripleExpression<T> right, Calculator<T> calc) {
        super(left, right, calc);
    }

    @Override
    protected T eval(T left, T right) throws EvaluationException {
        return calc.pow(left, right);
    }

}
