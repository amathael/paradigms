package expression.elements;

import expression.calc.Calculator;
import expression.exceptions.EvaluationException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 23-Mar-18
 * @time 16:15
 */

public class CheckedBitCount<T> extends AbstractUnaryOperation<T> {

    public CheckedBitCount(TripleExpression<T> argument, Calculator<T> calc) {
        super(argument, calc);
    }

    protected T eval(T value) throws EvaluationException {
        return calc.bitCount(value);
    }

}
