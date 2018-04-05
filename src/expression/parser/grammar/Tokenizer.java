package expression.parser.grammar;

import expression.exceptions.InputNumberFormatException;

import java.util.HashMap;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 29-Mar-18
 * @time 21:31
 */

@SuppressWarnings("WeakerAccess")
public class Tokenizer<T> {

    private HashMap<String, Token<T>> tokens;
    public final Token<T>
            CONSTANT = new Token<>("CONST"),
            VARIABLE = new Token<>("VARIABLE"),
            EXPRESSION_END = new Token<>("EXPRESSION_END"),
            ERROR = new Token<>("ERROR");

    private static final boolean ALLOW_LEADING_ZEROES = true;

    private Trie trie;
    private String input;
    private int index;
    private Token<T> curToken;

    private String customWord;

    public Tokenizer() {
        tokens = new HashMap<>();
        trie = new Trie();
    }

    public void init(String input) throws InputNumberFormatException {
        this.input = input;
        index = 0;
        curToken = null;
        nextToken(false);
    }

    public void addToken(String value, Token<T> token) {
        tokens.put(value, token);
        trie.add(value);
    }

    public Token<T> getToken() {
        return curToken;
    }

    void skipSpaces() {
        while (index < input.length() && Character.isWhitespace(input.charAt(index))) {
            index++;
        }
    }

    private void readNumber(boolean negative) throws InputNumberFormatException {
        boolean point = false;
        StringBuilder sb = new StringBuilder();
        if (negative) {
            sb.append("-");
        }
        int start = index;
        boolean startsWithNull = input.charAt(index) == '0';
        while (index < input.length() && (Character.isDigit(input.charAt(index)) || input.charAt(index) == '.')) {
            if (input.charAt(index) == '.') {
                if (point) {
                    throw new InputNumberFormatException("Second dot got while parsing number", index);
                }
                point = true;
            }
            sb.append(input.charAt(index));
            index++;
        }
        //noinspection ConstantConditions
        if (startsWithNull && index > start + 1 && !Tokenizer.ALLOW_LEADING_ZEROES) {
            throw new InputNumberFormatException("Number with leading zero given", start);
        }
        if (point && sb.charAt(sb.length() - 1) == '.') {
            throw new InputNumberFormatException("Number ending with '.' given", start);
        }
        curToken = CONSTANT;
        customWord = sb.toString();
    }

    private void readWord() {
        StringBuilder sb = new StringBuilder();
        while (index < input.length() &&
                (Character.isAlphabetic(input.charAt(index)) || Character.isDigit(input.charAt(index)))) {
            sb.append(input.charAt(index));
            index++;
        }
        curToken = VARIABLE;
        customWord = sb.toString();
    }

    public void nextToken(boolean ignoreNegative) throws InputNumberFormatException {
        skipSpaces();
        if (index == input.length()) {
            curToken = EXPRESSION_END;
            return;
        }

        int from = index, to = trie.seek(input, index);
        if (to > -1) {
            String tokenValue = input.substring(from, to);
            curToken = tokens.get(tokenValue);
            index = to;
            if ("MINUS".equals(curToken.getName()) && Character.isDigit(input.charAt(to)) && !ignoreNegative) {
                readNumber(true);
            }
        } else {
            if (Character.isDigit(input.charAt(index))) {
                readNumber(false);
            } else if (Character.isAlphabetic(input.charAt(index))) {
                readWord();
            } else {
                curToken = ERROR;
                customWord = "" + input.charAt(index);
            }
        }
    }

    public int getPosition() {
        return index;
    }

    public String getCustomWord() {
        return customWord;
    }

    public String getDescription() {
        return curToken == ERROR ? customWord : curToken.getName();
    }

}
