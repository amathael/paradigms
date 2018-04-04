package expression.elements;

import expression.calc.Calculator;
import expression.exceptions.OperationUnsupportedException;
import expression.exceptions.EvaluationException;
import expression.exceptions.IllegalArgumentException;

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
