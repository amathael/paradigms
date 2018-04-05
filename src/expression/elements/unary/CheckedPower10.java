package expression.elements.unary;

import expression.calc.Calculator;
import expression.elements.TripleExpression;
import expression.exceptions.EvaluationException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 30-Mar-18
 * @time 16:42
 */

public class CheckedPower10<T> extends AbstractUnaryOperation<T> {

    public CheckedPower10(TripleExpression<T> argument, Calculator<T> calc) {
        super(argument, calc);
    }

    @Override
    protected T eval(T value) throws EvaluationException {
        return calc.pow(calc.valueOf(10), value);
    }

}
