package java.expression.exceptions;

import java.expression.exceptions.GrammarException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 04-Apr-18
 * @time 21:52
 */

public class InputNumberFormatException extends GrammarException {

    public InputNumberFormatException(String msg, int position) {
        super(msg, position);
    }

}
