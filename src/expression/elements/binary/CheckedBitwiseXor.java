package expression.elements.binary;

import expression.calc.Calculator;
import expression.elements.TripleExpression;
import expression.exceptions.EvaluationException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 23-Mar-18
 * @time 16:01
 */

public class CheckedBitwiseXor<T> extends AbstractBinaryOperation<T> {

    public CheckedBitwiseXor(TripleExpression<T> left, TripleExpression<T> right, Calculator<T> calc) {
        super(left, right, calc);
    }

    @Override
    protected T eval(T left, T right) throws EvaluationException {
        return calc.xor(left, right);
    }

}
