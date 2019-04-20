package java.expression.parser;

import java.expression.calc.Calculator;
import java.expression.elements.Const;
import java.expression.elements.TripleExpression;
import java.expression.elements.Variable;
import java.expression.elements.binary.*;
import java.expression.elements.unary.CheckedBitCount;
import java.expression.elements.unary.CheckedLog10;
import java.expression.elements.unary.CheckedNegate;
import java.expression.elements.unary.CheckedPower10;
import java.expression.exceptions.GrammarException;
import java.expression.exceptions.NumberParsingException;
import java.expression.exceptions.UnsupportedVariableNameException;
import java.expression.parser.grammar.ExpressionGrammar;
import java.expression.parser.grammar.Token;
import java.expression.parser.grammar.Tokenizer;

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

        initToken("min", new Token<>("MIN",
                (left, right) -> new CheckedMin<>(left, right, calc), null), 0);
        initToken("max", new Token<>("MAX",
                (left, right) -> new CheckedMax<>(left, right, calc), null), 0);

        initToken("|", new Token<>("VSLASH",
                (left, right) -> new CheckedBitwiseOr<>(left, right, calc), null), 1);
        initToken("^", new Token<>("CIRCUMFLEX",
                (left, right) -> new CheckedBitwiseXor<>(left, right, calc), null), 2);
        initToken("&", new Token<>("AMPERSAND",
                (left, right) -> new CheckedBitwiseAnd<>(left, right, calc), null), 3);

        initToken("-", new Token<>("MINUS",
                (left, right) -> new CheckedSubtract<>(left, right, calc),
                arg -> new CheckedNegate<>(arg, calc)), 4);
        initToken("+", new Token<>("PLUS",
                (left, right) -> new CheckedAdd<>(left, right, calc), null), 4);

        initToken("/", new Token<>("SLASH",
                (left, right) -> new CheckedDivide<>(left, right, calc), null), 5);
        initToken("*", new Token<>("ASTERISK",
                (left, right) -> new CheckedMultiply<>(left, right, calc), null), 5);

        initToken("**", new Token<>("POWER",
                (left, right) -> new CheckedPower<>(left, right, calc), null), 6);
        initToken("//", new Token<>("LOG",
                (left, right) -> new CheckedLog<>(left, right, calc), null), 6);

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
    public TripleExpression<T> parse(String expression) throws GrammarException {
        tokenizer.init(expression);
        return parseLevel(0, 0);
    }

    private TripleExpression<T> parseLevel(int level, int brackets) throws GrammarException {
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
                throw new GrammarException(String.format("Unknown token '%s', expected level %d operation" +
                        (brackets > 0 ? " or closing parenthesis" : ""), tokenizer.getDescription(), level),
                        tokenizer.getPosition());
            }
            if (!tokenizer.getToken().isUnary() && !tokenizer.getToken().isBinary() &&
                    !(tokenizer.getToken() == tokenizer.EXPRESSION_END) &&
                    !("RIGHT_BRACKET".equals(tokenizer.getToken().getName()) && brackets > 0)) {
                throw new GrammarException(String.format("Invalid token '%s', expected level %d operation", tokenizer.getDescription(), level),
                        tokenizer.getPosition());
            }
            return result;
        }
    }

    private TripleExpression<T> parseFactor(int brackets) throws GrammarException {
        if (tokenizer.getToken().isUnary()) {
            Token<T> operation = tokenizer.getToken();
            tokenizer.nextToken(false);
            return operation.apply(parseFactor(brackets));
        } else {
            return parseAtom(brackets);
        }
    }

    private TripleExpression<T> parseAtom(int brackets) throws GrammarException {
        TripleExpression<T> result;
        Token atom = tokenizer.getToken();
        String tokenValue = tokenizer.getCustomWord(), description = tokenizer.getDescription();
        tokenizer.nextToken(true);

        if (atom == tokenizer.CONSTANT) {
            try {
                result = new Const<>(calc.parseString(tokenValue));
            } catch (NumberParsingException e) {
                throw new GrammarException("Can not parse number", tokenizer.getPosition());
            }
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
            throw new GrammarException(String.format("Unexpected token '%s', atom token expected", description), tokenizer.getPosition());
        }
        return result;
    }

}
