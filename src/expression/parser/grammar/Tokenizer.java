package expression.parser.grammar;

import java.util.HashMap;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 29-Mar-18
 * @time 21:31
 */

@SuppressWarnings("WeakerAccess")
public class Tokenizer {

    private HashMap<String, Single> tokens;
    public static final Single
            CONSTANT = new Single("CONST"),
            VARIABLE = new Single("VARIABLE"),
            EXPRESSION_END = new Single("EXPRESSION_END"),
            ERROR = new Single("ERROR");

    private static final boolean ALLOW_LEADING_ZEROES = true;

    private Trie trie;
    private String input;
    private int index;
    private Single curToken;

    private int constValue;
    private String customWord;

    public Tokenizer() {
        tokens = new HashMap<>();
        trie = new Trie();
    }

    public void init(String input) {
        this.input = input;
        index = 0;
        curToken = null;
        nextToken();
    }

    public void addToken(String value, Single token) {
        tokens.put(value, token);
        trie.add(value);
    }

    public Single getToken() {
        return curToken;
    }

    void skipSpaces() {
        while (index < input.length() && Character.isWhitespace(input.charAt(index))) {
            index++;
        }
    }

    public void nextToken() {
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
        } else {
            char cur = input.charAt(index);
            if (Character.isDigit(cur)) {
                constValue = 0;
                int start = index;
                boolean startsWithNull = cur == '0';
                while (index < input.length() && Character.isDigit(input.charAt(index))) {
                    constValue = constValue * 10 + (input.charAt(index) - '0');
                    index++;
                }
                //noinspection ConstantConditions
                curToken = startsWithNull && cur > start + 1 && !Tokenizer.ALLOW_LEADING_ZEROES ? ERROR : CONSTANT;
            } else if (Character.isAlphabetic(cur)) {
                StringBuilder sb = new StringBuilder();
                while (index < input.length() &&
                        (Character.isAlphabetic(input.charAt(index)) || Character.isDigit(input.charAt(index)))) {
                    sb.append(input.charAt(index));
                    index++;
                }
                curToken = VARIABLE;
                customWord = sb.toString();
            } else {
                curToken = ERROR;
            }
        }
    }

    public int getPosition() {
        return index;
    }

    public int getConstValue() {
        return constValue;
    }

    public String getCustomWord() {
        return customWord;
    }

}
