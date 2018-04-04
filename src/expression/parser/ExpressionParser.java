package expression.parser;

import expression.calc.Calculator;
import expression.calc.IntegerCalculator;
import expression.elements.*;
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
    Calculator calc = new IntegerCalculator();

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
    public TripleExpression parse(String expression) throws GrammarException {
        tokenizer.init(expression);
        return parseLevel(0, 0);
    }

    private TripleExpression parseLevel(int level, int brackets) throws GrammarException {
        if (level == grammar.size()) {
            return parseFactor(brackets);
        } else {
            TripleExpression result = parseLevel(level + 1, brackets);
            while (grammar.isOnLevel(level, tokenizer.getToken())) {
                Token operation = tokenizer.getToken();
                tokenizer.nextToken(true);
                result = operation.apply(result, parseLevel(level + 1, brackets));
            }
            if (tokenizer.getToken() == Tokenizer.ERROR) {
                throw new GrammarException("Unknown token '" + tokenizer.getDescription() + "'", tokenizer.getPosition());
            }
            if (!tokenizer.getToken().isUnary() && !tokenizer.getToken().isBinary() &&
                    !(tokenizer.getToken() == Tokenizer.EXPRESSION_END) &&
                    !("RIGHT_BRACKET".equals(tokenizer.getToken().getName()) && brackets > 0)) {
                throw new GrammarException("Invalid token '" + tokenizer.getDescription() + "' got, operation expected",
                        tokenizer.getPosition());
            }
            return result;
        }
    }

    private TripleExpression parseFactor(int brackets) throws GrammarException {
        if (tokenizer.getToken().isUnary()) {
            Token operation = tokenizer.getToken();
            tokenizer.nextToken(false);
            return operation.apply(parseFactor(brackets));
        } else {
            return parseAtom(brackets);
        }
    }

    private TripleExpression parseAtom(int brackets) throws GrammarException {
        TripleExpression result;
        Token atom = tokenizer.getToken();
        String tokenValue = tokenizer.getCustomWord();
        tokenizer.nextToken(true);

        if (atom == Tokenizer.CONSTANT) {
            try {
                result = new Const<>(Integer.parseInt(tokenValue));
            } catch (NumberFormatException e) {
                throw new GrammarException("Too large constant string '" + tokenizer.getCustomWord() + "'", tokenizer.getPosition());
            }
        } else if (atom == Tokenizer.VARIABLE) {
            if (!"x".equals(tokenValue) && !"y".equals(tokenValue) && !"z".equals(tokenValue)) {
                throw new UnsupportedVariableNameException(tokenValue, tokenizer.getPosition());
            }
            result = new Variable<>(tokenizer.getCustomWord());
        } else if ("LEFT_BRACKET".equals(atom.getName())) {
            if ("RIGHT_BRACKET".equals(tokenizer.getToken().getName())) {
                throw new GrammarException("Empty expression in parenthesis", tokenizer.getPosition());
            }
            result = parseLevel(0, brackets + 1);
            if (!"RIGHT_BRACKET".equals(tokenizer.getToken().getName())) {
                throw new GrammarException("Closing parenthesis expected", tokenizer.getPosition());
            }
            tokenizer.nextToken(true);
        } else {
            throw new GrammarException("Unexpected token '" + tokenizer.getDescription() + "'", tokenizer.getPosition());
        }
        return result;
    }

}
