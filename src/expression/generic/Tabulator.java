package expression.generic;

import expression.exceptions.NumberParsingException;
import expression.exceptions.TabulatorRangeException;
import expression.exceptions.UnknownModeException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 05-Apr-18
 * @time 12:08
 */

public interface Tabulator {

    Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws UnknownModeException, NumberParsingException, TabulatorRangeException;

}
