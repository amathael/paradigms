package expression.parser.grammar;

import expression.Expression;

import java.util.HashMap;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 24-Mar-18
 * @time 11:57
 */

public abstract class Token {

    protected static HashMap<String, Class<? extends Expression>> byKeyword;

}
