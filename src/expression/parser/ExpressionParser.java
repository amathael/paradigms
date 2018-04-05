package expression.parser;

import expression.calc.Calculator;
import expression.elements.*;
import expression.elements.binary.*;
import expression.elements.unary.CheckedBitCount;
import expression.elements.unary.CheckedLog10;
import expression.elements.unary.CheckedNegate;
import expression.elements.unary.CheckedPower10;
import expression.exceptions.GrammarException;
import expression.exceptions.NumberParsingException;
import expression.exceptions.UnsupportedVariableNameException;
import expression.parser.grammar.*;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 22-Mar-18
 * @time 19:06
 */

@SuppressWarnings("WeakerAccess")
public class ExpressionParser<T> implements Parser {

    private Tokenizer<T> tokenizer;
    private ExpressionGrammar grammar;
    private Calculator<T> calc;

    public ExpressionParser(Calculator<T> calc) {
        tokenizer = new Tokenizer<>();
        grammar = new ExpressionGrammar();
        this.calc = calc;

        tokenizer.addToken("(", new Token<>("LEFT_BRACKET"));
        tokenizer.addToken(")", new Token<>("RIGHT_BRACKET"));

        initToken("|", new Token<>("VSLASH",
                (left, right) -> new CheckedBitwiseOr<>(left, right, calc), null), 0);
        initToken("^", new Token<>("CIRCUMFLEX",
                (left, right) -> new CheckedBitwiseXor<>(left, right, calc), null), 1);
        initToken("&", new Token<>("AMPERSAND",
                (left, right) -> new CheckedBitwiseAnd<>(left, right, calc), null), 2);

        initToken("-", new Token<>("MINUS",
                (left, right) -> new CheckedSubtract<>(left, right, calc),
                arg -> new CheckedNegate<>(arg, calc)), 3);
        initToken("+", new Token<>("PLUS",
                (left, right) -> new CheckedAdd<>(left, right, calc), null), 3);

        initToken("/", new Token<>("SLASH",
                (left, right) -> new CheckedDivide<>(left, right, calc), null), 4);
        initToken("*", new Token<>("ASTERISK",
                (left, right) -> new CheckedMultiply<>(left, right, calc), null), 4);

        initToken("**", new Token<>("POWER",
                (left, right) -> new CheckedPower<>(left, right, calc), null), 5);
        initToken("//", new Token<>("LOG",
                (left, right) -> new CheckedLog<>(left, right, calc), null), 5);

        tokenizer.addToken("~", new Token<>("TILDE",
                null, arg -> new CheckedBitwiseNegate<>(arg, calc)));
        tokenizer.addToken("count", new Token<>("COUNT",
                null, arg -> new CheckedBitCount<>(arg, calc)));
        tokenizer.addToken("log10", new Token<>("LOG10",
                null, arg -> new CheckedLog10<>(arg, calc)));
        tokenizer.addToken("pow10", new Token<>("POW10",
                null, arg -> new CheckedPower10<>(arg, calc)));
    }

    public void initToken(String value, Token<T> token, int level) {
        tokenizer.addToken(value, token);
        grammar.addOnLevel(level, token);
    }

    @Override
    public TripleExpression<T> parse(String expression) throws GrammarException, NumberParsingException {
        tokenizer.init(expression);
        return parseLevel(0, 0);
    }

    private TripleExpression<T> parseLevel(int level, int brackets) throws GrammarException, NumberParsingException {
        if (level == grammar.size()) {
            return parseFactor(brackets);
        } else {
            TripleExpression<T> result = parseLevel(level + 1, brackets);
            while (grammar.isOnLevel(level, tokenizer.getToken())) {
                Token<T> operation = tokenizer.getToken();
                tokenizer.nextToken(true);
                result = operation.apply(result, parseLevel(level + 1, brackets));
            }
            if (tokenizer.getToken() == tokenizer.ERROR) {
                throw new GrammarException("Unknown token '" + tokenizer.getDescription() + "'", tokenizer.getPosition());
            }
            if (!tokenizer.getToken().isUnary() && !tokenizer.getToken().isBinary() &&
                    !(tokenizer.getToken() == tokenizer.EXPRESSION_END) &&
                    !("RIGHT_BRACKET".equals(tokenizer.getToken().getName()) && brackets > 0)) {
                throw new GrammarException("Invalid token '" + tokenizer.getDescription() + "' got, operation expected",
                        tokenizer.getPosition());
            }
            return result;
        }
    }

    private TripleExpression<T> parseFactor(int brackets) throws GrammarException, NumberParsingException {
        if (tokenizer.getToken().isUnary()) {
            Token<T> operation = tokenizer.getToken();
            tokenizer.nextToken(false);
            return operation.apply(parseFactor(brackets));
        } else {
            return parseAtom(brackets);
        }
    }

    private TripleExpression<T> parseAtom(int brackets) throws GrammarException, NumberParsingException {
        TripleExpression<T> result;
        Token atom = tokenizer.getToken();
        String tokenValue = tokenizer.getCustomWord();
        tokenizer.nextToken(true);

        if (atom == tokenizer.CONSTANT) {
            result = new Const<>(calc.parseString(tokenValue));
        } else if (atom == tokenizer.VARIABLE) {
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
