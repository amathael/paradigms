package expression.parser;

import expression.CommonExpression;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 22-Mar-18
 * @time 19:04
 */

public interface Parser {

    public CommonExpression parse(String expression);

}
