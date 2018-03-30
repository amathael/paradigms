package expression.parser;

import expression.*;
import expression.exceptions.GrammarExpression;
import expression.exceptions.UnsupportedVariableNameException;
import expression.parser.grammar.*;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 22-Mar-18
 * @time 19:06
 */

@SuppressWarnings("WeakerAccess")
public class ExpressionParser implements Parser {

    private Tokenizer tokenizer;
    private ExpressionGrammar grammar;

    public ExpressionParser() {
        tokenizer = new Tokenizer();
        grammar = new ExpressionGrammar();

        tokenizer.addToken("(", new Single("LEFT_BRACKET"));
        tokenizer.addToken(")", new Single("RIGHT_BRACKET"));

        initToken("|", new Single("VSLASH", CheckedBitwiseOr.class, null), 0);
        initToken("^", new Single("CIRCUMFLEX", CheckedBitwiseXor.class, null), 1);
        initToken("&", new Single("AMPERSAND", CheckedBitwiseAnd.class, null), 2);

        initToken("-", new Single("MINUS", CheckedSubtract.class, CheckedNegate.class), 3);
        initToken("+", new Single("PLUS", CheckedAdd.class, null), 3);

        initToken("/", new Single("SLASH", CheckedDivide.class, null), 4);
        initToken("*", new Single("ASTERISK", CheckedMultiply.class, null), 4);

        initToken("**", new Single("POWER", CheckedPower.class, null), 5);
        initToken("//", new Single("LOG", CheckedLog.class, null), 5);

        tokenizer.addToken("~", new Single("TILDE", null, CheckedBitwiseNegate.class));
        tokenizer.addToken("count", new Single("COUNT", null, CheckedBitCount.class));
        tokenizer.addToken("log10", new Single("LOG10", null, CheckedLog10.class));
        tokenizer.addToken("pow10", new Single("POW10", null, CheckedPower10.class));
    }

    public void initToken(String value, Single token, int level) {
        tokenizer.addToken(value, token);
        grammar.addOnLevel(level, token);
    }

    @Override
    public CommonExpression parse(String expression) throws GrammarExpression {
        tokenizer.init(expression);
        return parseLevel(0);
    }

    private CommonExpression parseLevel(int level) throws GrammarExpression {
        if (level == grammar.size()) {
            return parseFactor();
        } else {
            CommonExpression result = parseLevel(level + 1);
            while (grammar.isOnLevel(level, tokenizer.getToken())) {
                Single operation = tokenizer.getToken();
                tokenizer.nextToken();
                result = operation.apply(result, parseLevel(level + 1));
            }
            return result;
        }
    }

    private CommonExpression parseFactor() throws GrammarExpression {
        if (tokenizer.getToken().isUnary()) {
            Single operation = tokenizer.getToken();
            tokenizer.nextToken();
            return operation.apply(parseFactor());
        } else {
            return parseAtom();
        }
    }

    private CommonExpression parseAtom() throws GrammarExpression {
        CommonExpression result;
        Single atom = tokenizer.getToken();
        tokenizer.nextToken();

        if (atom == Tokenizer.CONSTANT) {
            result = new Const(tokenizer.getConstValue());
        } else if (atom == Tokenizer.VARIABLE) {
            String name = tokenizer.getCustomWord();
            if (!"x".equals(name) && !"y".equals(name) && !"z".equals(name)) {
                throw new UnsupportedVariableNameException(name, tokenizer.getPosition());
            }
            result = new Variable(tokenizer.getCustomWord());
        } else if ("LEFT_BRACKET".equals(atom.getName())) {
            result = parseLevel(0);
            if (!"RIGHT_BRACKET".equals(tokenizer.getToken().getName())) {
                throw new GrammarExpression("Right bracket expected", tokenizer.getPosition());
            }
            tokenizer.nextToken();
        } else {
            throw new GrammarExpression("Unknown token", tokenizer.getPosition());
        }
        return result;
    }

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
