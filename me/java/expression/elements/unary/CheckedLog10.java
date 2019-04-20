package java.expression.elements.unary;

import java.expression.calc.Calculator;
import java.expression.elements.TripleExpression;
import java.expression.exceptions.EvaluationException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 30-Mar-18
 * @time 16:34
 */

public class CheckedLog10<T> extends AbstractUnaryOperation<T> {

    public CheckedLog10(TripleExpression<T> argument, Calculator<T> calc) {
        super(argument, calc);
    }

    @Override
    protected T eval(T value) throws EvaluationException {
        return calc.log(value, calc.valueOf(10));
    }

}
