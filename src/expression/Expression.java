package expression;

import expression.exceptions.EvaluationException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 10-Mar-18
 * @time 10:15
 */

public interface Expression {

    public int evaluate(int variable) throws EvaluationException;

}
