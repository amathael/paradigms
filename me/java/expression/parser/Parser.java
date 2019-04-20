package java.expression.parser;

import java.expression.elements.TripleExpression;
import java.expression.exceptions.EvaluationException;
import java.expression.exceptions.GrammarException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 22-Mar-18
 * @time 19:04
 */

public interface Parser {

    TripleExpression parse(String expression) throws GrammarException, EvaluationException;

}
