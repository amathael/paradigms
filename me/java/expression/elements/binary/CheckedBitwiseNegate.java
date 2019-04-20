package java.expression.elements.binary;

import java.expression.calc.Calculator;
import java.expression.elements.TripleExpression;
import java.expression.elements.unary.AbstractUnaryOperation;
import java.expression.exceptions.EvaluationException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 23-Mar-18
 * @time 16:02
 */

public class CheckedBitwiseNegate<T> extends AbstractUnaryOperation<T> {

    public CheckedBitwiseNegate(TripleExpression<T> argument, Calculator<T> calc) {
        super(argument, calc);
    }

    @Override
    protected T eval(T value) throws EvaluationException {
        return calc.not(value);
    }

}
