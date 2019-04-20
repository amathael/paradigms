package java.expression.exceptions;

import java.expression.exceptions.EvaluationException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 30-Mar-18
 * @time 15:44
 */

@SuppressWarnings("WeakerAccess")
public class IllegalArgumentException extends EvaluationException {

    public IllegalArgumentException() {
        super();
    }

    public IllegalArgumentException(String reason) {
        super(reason);
    }

}
