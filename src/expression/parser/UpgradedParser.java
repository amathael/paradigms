package expression.parser;

import expression.*;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 22-Mar-18
 * @time 19:06
 */

@SuppressWarnings("WeakerAccess")
public class UpgradedParser implements Parser {

    @Override
    public CommonExpression parse(String expression) {
        return null;
    }

    private abstract class Token {
//        static HashMap<String, Token> byKeyword = new HashMap<>();
    }

    // @formatter:off
    private void       deeo(int re) {
        int a =         1;
        int bbsdf =     2;
    }
    // @formatter:on

//    private abstract class

    /*
    <full>              <- <expression> (<bit sign> <expression>)*
    <expression>        <- <or expression> ("|" <or expression>)*
    <or expression>     <- <xor expression> ("^" <xor expression>)*
    <xor expression>    <- <and expression> ("&" <and expression>)*
    <and_expression>    <- <term> (<term sign> <term>)*
    <term>              <- <signed factor> (<factor sign> <signed factor>)*
    <signed factor>     <- <unary sign>*<factor>
    <factor>            <- <constant> | <variable> | "("<full>")"
    <constant>          <- ("-") "0" | ["1"-"9"]["0"-"9"]*
    <variable>          <- "x" | "y" | "z"

    <unary sign>        <- "-" | "abs" | "square"
    <term sign>         <- "+" | "-"
    <factor sign>       <- "*" | "/" | "mod"
    <bit sign>          <- "<<" | ">>"
    */

}
