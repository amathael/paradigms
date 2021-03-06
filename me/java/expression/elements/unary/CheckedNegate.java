package java.expression.elements.unary;

import java.expression.calc.Calculator;
import java.expression.elements.TripleExpression;
import java.expression.exceptions.EvaluationException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 22-Mar-18
 * @time 18:58
 */

public class CheckedNegate<T> extends AbstractUnaryOperation<T> {

    public CheckedNegate(TripleExpression<T> argument, Calculator<T> calc) {
        super(argument, calc);
    }

    @Override
    protected T eval(T value) throws EvaluationException {
        return calc.neg(value);
    }

}
