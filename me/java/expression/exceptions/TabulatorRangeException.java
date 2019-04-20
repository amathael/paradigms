package java.expression.exceptions;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 05-Apr-18
 * @time 13:05
 */

public class TabulatorRangeException extends Exception {

    public TabulatorRangeException(int from, int to) {
        super(String.format("Start value %d is greate then finish value %d", from, to));
    }

}
