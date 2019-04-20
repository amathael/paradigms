package java.expression.elements.unary;

import java.expression.calc.Calculator;
import java.expression.elements.TripleExpression;
import java.expression.exceptions.EvaluationException;

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

    @Override
    protected T eval(T value) throws EvaluationException {
        return calc.bitCount(value);
    }

}
