package expression.parser;

import expression.elements.TripleExpression;
import expression.exceptions.GrammarException;
import expression.exceptions.NumberConversionException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 22-Mar-18
 * @time 19:04
 */

public interface Parser {

    TripleExpression parse(String expression) throws GrammarException, NumberConversionException;

}
