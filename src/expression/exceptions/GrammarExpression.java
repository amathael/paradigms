package expression.exceptions;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 30-Mar-18
 * @time 16:28
 */

@SuppressWarnings("WeakerAccess")
public class GrammarExpression extends Exception {

    public GrammarExpression(String msg, int position) {
        super(msg.concat(" before position ").concat(String.valueOf(position)));
    }

}
