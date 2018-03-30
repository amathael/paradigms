package expression.parser;
import expression.*;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 22-Mar-18
 * @time 19:06
 */

@Deprecated
public class SimpleParser implements Parser {

    public enum Token {
        ERROR,
        VARIABLE,
        CONSTANT,
        LEFT_BRACKET,
        RIGHT_BRACKET,
        MINUS,
        PLUS,
        AMPERSAND,
        VSLASH,
        CIRCUMFLEX,
        ASTERISK,
        SLASH,
        SHRINK_LEFT,
        SHRINK_RIGHT,
        TILDE,
        ABS,
        SQUARE,
        MOD,
        COUNT,
        EXPRESSION_END,
    }

    private final HashMap<String, Token> keywords;

    private final HashSet<Token> lowcost_operations;
    private final HashSet<Token> term_operations;
    private final HashSet<Token> factor_operations;
    private final HashSet<Token> unary_operations;

    public SimpleParser() {
        keywords = new HashMap<>();
        keywords.put("abs", Token.ABS);
        keywords.put("square", Token.SQUARE);
        keywords.put("mod", Token.MOD);
        keywords.put("count", Token.COUNT);

        lowcost_operations = new HashSet<>();
        lowcost_operations.add(Token.SHRINK_LEFT);
        lowcost_operations.add(Token.SHRINK_RIGHT);

        term_operations = new HashSet<>();
        term_operations.add(Token.MINUS);
        term_operations.add(Token.PLUS);

        factor_operations = new HashSet<>();
        factor_operations.add(Token.ASTERISK);
        factor_operations.add(Token.SLASH);
        factor_operations.add(Token.MOD);

        unary_operations = new HashSet<>();
        unary_operations.add(Token.MINUS);
        unary_operations.add(Token.ABS);
        unary_operations.add(Token.SQUARE);
        unary_operations.add(Token.TILDE);
        unary_operations.add(Token.COUNT);
    }

    @Override
    public CommonExpression parse(String expression) {
        assert expression != null : "Expression expected, null received";
        tokenizer = new Tokenizer(expression);
        return parseFullExpression();
    }

    @SuppressWarnings("ConstantConditions")
    private class Tokenizer {
        private static final boolean ALLOW_LEADING_ZEROES = true;

        private char[] expression;
        private int index;
        private Token curToken = null;

        private String varName = null;
        private int constValue = 0;

        Tokenizer(String expression) {
            this.expression = expression.toCharArray();
            index = 0;
            nextToken();
        }

        void skipSpaces() {
            while (index < expression.length && Character.isWhitespace(expression[index])) {
                index++;
            }
        }

        void nextToken() {
            skipSpaces();
            if (index == expression.length) {
                curToken = Token.EXPRESSION_END;
                return;
            }

            char cur = expression[index];
            switch (cur) {
                case '+':
                    curToken = Token.PLUS;
                    break;
                case '-':
                    curToken = Token.MINUS;
                    break;
                case '*':
                    curToken = Token.ASTERISK;
                    break;
                case '/':
                    curToken = Token.SLASH;
                    break;
                case '(':
                    curToken = Token.LEFT_BRACKET;
                    break;
                case ')':
                    curToken = Token.RIGHT_BRACKET;
                    break;
                case '&':
                    curToken = Token.AMPERSAND;
                    break;
                case '|':
                    curToken = Token.VSLASH;
                    break;
                case '~':
                    curToken = Token.TILDE;
                    break;
                case '^':
                    curToken = Token.CIRCUMFLEX;
                    break;
                case '<':
                    curToken = index + 1 < expression.length && expression[index + 1] == '<' ? Token.SHRINK_LEFT :
                            Token.ERROR;
                    index++;
                    break;
                case '>':
                    curToken = index + 1 < expression.length && expression[index + 1] == '>' ? Token.SHRINK_RIGHT :
                            Token.ERROR;
                    index++;
                    break;
                default:
                    if (Character.isDigit(cur)) {
                        constValue = 0;
                        int start = index;
                        boolean startsWithNull = cur == '0';
                        while (index < expression.length && Character.isDigit(expression[index])) {
                            constValue = constValue * 10 + (expression[index] - '0');
                            index++;
                        }
                        curToken = startsWithNull && cur > start + 1 && !Tokenizer.ALLOW_LEADING_ZEROES ? Token.ERROR :
                                Token.CONSTANT;
                    } else if (Character.isAlphabetic(cur)) {
                        StringBuilder sb = new StringBuilder();
                        while (index < expression.length &&
                                (Character.isAlphabetic(expression[index]) || Character.isDigit(expression[index]))) {
                            sb.append(expression[index]);
                            index++;
                        }

                        String token = sb.toString();
                        curToken = keywords.getOrDefault(token, Token.VARIABLE);
                        if (curToken == Token.VARIABLE) {
                            varName = token;
                        }
                    }
                    index--;
            }
            index++;
        }

    }

    private Tokenizer tokenizer;

    private CommonExpression parseFullExpression() {
        CommonExpression result = parseOrExpression();
        while (tokenizer.curToken != Token.EXPRESSION_END && tokenizer.curToken != Token.RIGHT_BRACKET
                && lowcost_operations.contains(tokenizer.curToken)) {
            Token op = tokenizer.curToken;
            tokenizer.nextToken();
            switch (op) {
                case SHRINK_LEFT:
                    result = new ShrinkLeft(result, parseOrExpression());
                    break;
                case SHRINK_RIGHT:
                    result = new ShrinkRight(result, parseOrExpression());
                    break;
                default:
                    result = null;
            }
        }
        return result;
    }

    private CommonExpression parseOrExpression() {
        CommonExpression result = parseXorExpression();
        while (tokenizer.curToken == Token.VSLASH) {
            tokenizer.nextToken();
            result = new BitwiseOr(result, parseXorExpression());
        }
        return result;
    }

    private CommonExpression parseXorExpression() {
        CommonExpression result = parseAndExpression();
        while (tokenizer.curToken == Token.CIRCUMFLEX) {
            tokenizer.nextToken();
            result = new BitwiseXor(result, parseAndExpression());
        }
        return result;
    }

    private CommonExpression parseAndExpression() {
        CommonExpression result = parseExpression();
        while (tokenizer.curToken == Token.AMPERSAND) {
            tokenizer.nextToken();
            result = new BitwiseAnd(result, parseExpression());
        }
        return result;
    }

    private CommonExpression parseExpression() {
        CommonExpression result = parseTerm();
        while (tokenizer.curToken != Token.EXPRESSION_END && tokenizer.curToken != Token.RIGHT_BRACKET
                && term_operations.contains(tokenizer.curToken)) {
            Token op = tokenizer.curToken;
            tokenizer.nextToken();
            switch (op) {
                case PLUS:
                    result = new Add(result, parseTerm());
                    break;
                case MINUS:
                    result = new Subtract(result, parseTerm());
                    break;
                default:
                    result = null;
            }
        }
        return result;
    }

    private CommonExpression parseTerm() {
        CommonExpression result = parseSignedFactor();
        while (tokenizer.curToken != Token.EXPRESSION_END && tokenizer.curToken != Token.RIGHT_BRACKET
                && factor_operations.contains(tokenizer.curToken)) {
            Token op = tokenizer.curToken;
            tokenizer.nextToken();
            switch (op) {
                case ASTERISK:
                    result = new Multiply(result, parseSignedFactor());
                    break;
                case SLASH:
                    result = new Divide(result, parseSignedFactor());
                    break;
                case MOD:
                    result = new Mod(result, parseSignedFactor());
                    break;
                default:
                    result = null;
            }
        }
        return result;
    }

    private CommonExpression parseSignedFactor() {
        if (unary_operations.contains(tokenizer.curToken)) {
            Token op = tokenizer.curToken;
            tokenizer.nextToken();
            switch (op) {
                case MINUS:
                    return new Negate(parseSignedFactor());
                case ABS:
                    return new Abs(parseSignedFactor());
                case SQUARE:
                    return new Square(parseSignedFactor());
                case COUNT:
                    return new BitCount(parseSignedFactor());
                case TILDE:
                    return new BitwiseNegate(parseSignedFactor());
                default:
                    return null;
            }
        } else {
            return parseFactor();
        }
    }

    private CommonExpression parseFactor() {
        CommonExpression result;
        Token op = tokenizer.curToken;
        tokenizer.nextToken();
        switch (op) {
            case CONSTANT:
                result = new Const(tokenizer.constValue);
                break;
            case VARIABLE:
                result = new Variable(tokenizer.varName);
                break;
            case LEFT_BRACKET:
                result = parseFullExpression();
                assert tokenizer.curToken == Token.RIGHT_BRACKET : "Incorrect syntax, \")\" missing";
                tokenizer.nextToken();
                break;
            default:
                result = null;
        }
        return result;
    }

    /*
    <full>          <- <expression> (<bit sign> <expression>)*
    <expression>    <- <or term> ("|" <or term>)*
    <or term>       <- <xor term> ("^" <xor term>)*
    <xor term>      <- <and_term> ("&" <and term>)*
    <and_term>      <- <term> (<term sign> <term>)*
    <term>          <- <signed factor> (<factor sign> <signed factor>)*
    <signed factor> <- <unary sign>*<factor>
    <factor>        <- <constant> | <variable> | "("<full>")"
    <constant>      <- ("-") "0" | ["1"-"9"]["0"-"9"]*
    <variable>      <- "x" | "y" | "z"

    <unary sign>    <- "-" | "abs" | "square"
    <term sign>     <- "+" | "-"
    <factor sign>   <- "*" | "/" | "mod"
    <bit sign>      <- "<<" | ">>"
    */

}
