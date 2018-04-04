package expression.elements;

import expression.calc.Calculator;
import expression.exceptions.EvaluationException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 23-Mar-18
 * @time 16:00
 */

public class CheckedBitwiseAnd<T> extends AbstractBinaryOperation<T> {

    public CheckedBitwiseAnd(TripleExpression<T> left, TripleExpression<T> right, Calculator<T> calc) {
        super(left, right, calc);
    }

    @Override
    protected T eval(T left, T right) throws EvaluationException {
        return calc.and(left, right);
    }

}
