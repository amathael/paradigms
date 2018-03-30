package expression;

import expression.exceptions.EvaluationException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 16-Mar-18
 * @time 14:59
 */

public interface DoubleExpression {

    public double evaluate(double variable) throws EvaluationException;

}
