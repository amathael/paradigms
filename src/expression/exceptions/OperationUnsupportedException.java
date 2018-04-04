package expression.exceptions;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 30-Mar-18
 * @time 15:24
 */

public class OperationUnsupportedException extends IllegalArgumentException {

    public OperationUnsupportedException(String reason) {
        super(reason);
    }

}
