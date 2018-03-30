package expression.exceptions;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 30-Mar-18
 * @time 14:48
 */

public class EvaluationException extends Exception {

    public EvaluationException() {
        super();
    }

    public EvaluationException(String reason) {
        super(reason);
    }

}
