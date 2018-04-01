package expression.parser;

import expression.*;
import expression.exceptions.GrammarException;
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

        tokenizer.addToken("(", new Token("LEFT_BRACKET"));
        tokenizer.addToken(")", new Token("RIGHT_BRACKET"));

        initToken("|", new Token("VSLASH", CheckedBitwiseOr.class, null), 0);
        initToken("^", new Token("CIRCUMFLEX", CheckedBitwiseXor.class, null), 1);
        initToken("&", new Token("AMPERSAND", CheckedBitwiseAnd.class, null), 2);

        initToken("-", new Token("MINUS", CheckedSubtract.class, CheckedNegate.class), 3);
        initToken("+", new Token("PLUS", CheckedAdd.class, null), 3);

        initToken("/", new Token("SLASH", CheckedDivide.class, null), 4);
        initToken("*", new Token("ASTERISK", CheckedMultiply.class, null), 4);

        initToken("**", new Token("POWER", CheckedPower.class, null), 5);
        initToken("//", new Token("LOG", CheckedLog.class, null), 5);

        tokenizer.addToken("~", new Token("TILDE", null, CheckedBitwiseNegate.class));
        tokenizer.addToken("count", new Token("COUNT", null, CheckedBitCount.class));
        tokenizer.addToken("log10", new Token("LOG10", null, CheckedLog10.class));
        tokenizer.addToken("pow10", new Token("POW10", null, CheckedPower10.class));
    }

    public void initToken(String value, Token token, int level) {
        tokenizer.addToken(value, token);
        grammar.addOnLevel(level, token);
    }

    @Override
    public CommonExpression parse(String expression) throws GrammarException {
        tokenizer.init(expression);
        return parseLevel(0, 0);
    }

    private CommonExpression parseLevel(int level, int brackets) throws GrammarException {
        if (level == grammar.size()) {
            return parseFactor(brackets);
        } else {
            CommonExpression result = parseLevel(level + 1, brackets);
            while (grammar.isOnLevel(level, tokenizer.getToken())) {
                Token operation = tokenizer.getToken();
                tokenizer.nextToken(true);
                result = operation.apply(result, parseLevel(level + 1, brackets));
            }
            if (tokenizer.getToken() == Tokenizer.ERROR) {
                throw new GrammarException("Unknown token", tokenizer.getPosition());
            }
            if (!tokenizer.getToken().isUnary() && !tokenizer.getToken().isBinary() &&
                    !(tokenizer.getToken() == Tokenizer.EXPRESSION_END) &&
                    !("RIGHT_BRACKET".equals(tokenizer.getToken().getName()) && brackets > 0)) {
                throw new GrammarException("Incorrect token, operation expected", tokenizer.getPosition());
            }
            return result;
        }
    }

    private CommonExpression parseFactor(int brackets) throws GrammarException {
        if (tokenizer.getToken().isUnary()) {
            Token operation = tokenizer.getToken();
            tokenizer.nextToken(false);
            return operation.apply(parseFactor(brackets));
        } else {
            return parseAtom(brackets);
        }
    }

    private CommonExpression parseAtom(int brackets) throws GrammarException {
        CommonExpression result;
        Token atom = tokenizer.getToken();
        tokenizer.nextToken(true);

        if (atom == Tokenizer.CONSTANT) {
            try {
                result = new Const(Integer.parseInt(tokenizer.getCustomWord()));
            } catch (NumberFormatException e) {
                throw new GrammarException("Too large constant ".concat(tokenizer.getCustomWord()), tokenizer.getPosition());
            }
        } else if (atom == Tokenizer.VARIABLE) {
            String name = tokenizer.getCustomWord();
            if (!"x".equals(name) && !"y".equals(name) && !"z".equals(name)) {
                throw new UnsupportedVariableNameException(name, tokenizer.getPosition());
            }
            result = new Variable(tokenizer.getCustomWord());
        } else if ("LEFT_BRACKET".equals(atom.getName())) {
            result = parseLevel(0, brackets + 1);
            if (!"RIGHT_BRACKET".equals(tokenizer.getToken().getName())) {
                throw new GrammarException("Right bracket expected", tokenizer.getPosition());
            }
            tokenizer.nextToken(true);
        } else {
            throw new GrammarException("Unknown token", tokenizer.getPosition());
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
