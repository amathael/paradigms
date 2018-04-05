package expression.exceptions;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 05-Apr-18
 * @time 12:15
 */

public class UnknownModeException extends Exception {

    public UnknownModeException(String mode) {
        super(String.format("Unknown mode '%s'", mode));
    }

}
