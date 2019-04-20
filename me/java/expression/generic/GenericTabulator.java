package java.expression.generic;

import java.expression.calc.*;
import java.expression.elements.TripleExpression;
import java.expression.exceptions.*;
import java.expression.parser.ExpressionParser;
import java.util.HashMap;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 05-Apr-18
 * @time 11:57
 */

public class GenericTabulator implements Tabulator {

    private HashMap<String, Calculator<?>> modes = new HashMap<String, Calculator<?>>() {{
        put("i", new IntegerCalculator());
        put("d", new DoubleCalculator());
        put("bi", new BigIntegerCalculator());
        put("u", new IntegerCalculator(true));
        put("l", new LongCalculator(true));
        put("s", new ShortCalculator(true));
    }};

    private Calculator<?> getCalculator(String mode) throws UnknownModeException {
        Calculator<?> calc = modes.getOrDefault(mode, null);
        if (calc != null) {
            return calc;
        } else {
            throw new UnknownModeException(mode);
        }
    }

    private void checkRange(int from, int to) throws TabulatorRangeException {
        if (from > to) {
            throw new TabulatorRangeException(from, to);
        }
    }

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws UnknownModeException, NumberParsingException, TabulatorRangeException {
        checkRange(x1, x2);
        checkRange(y1, y2);
        checkRange(z1, z2);

        Calculator<?> calc = getCalculator(mode);
        try {
            return buildTable(calc, expression, x1, x2, y1, y2, z1, z2);
        } catch (GrammarException | EvaluationException e) {
            return null;
        }
    }

    private <T> Object[][][] buildTable(Calculator<T> calc, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws EvaluationException, GrammarException {
        Object[][][] table = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        TripleExpression<T> parsedExpression = new ExpressionParser<>(calc).parse(expression);

        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                for (int k = z1; k <= z2; k++) {
                    try {
                        table[i - x1][j - y1][k - z1] = parsedExpression.evaluate(calc.valueOf(i), calc.valueOf(j), calc.valueOf(k));
                    } catch (EvaluationException e) {
                        table[i - x1][j - y1][k - z1] = null;
                    }
                }
            }
        }

        return table;
    }

}
