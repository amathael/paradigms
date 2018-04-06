package expression;

import expression.calc.IntegerCalculator;
import expression.elements.TripleExpression;
import expression.exceptions.DivisionByZeroException;
import expression.exceptions.EvaluationException;
import expression.exceptions.GrammarException;
import expression.exceptions.OverflowException;
import expression.parser.ExpressionParser;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 06-Apr-18
 * @time 15:01
 */

public class Main7 {

    public static void main(String[] args) {
        new Main7().test(args[0], 1, 10);
    }

    private void test(String expression, int from, int to) {
        TripleExpression<Integer> parsed = null;
        try {
            parsed = new ExpressionParser<>(new IntegerCalculator()).parse(expression);
        } catch (GrammarException e) {
            System.out.println("exception while parsing");
            System.exit(0);
        }

        System.out.println("x\tf");
        for (int i = from; i <= to; i++) {
            try {
                System.out.printf("%d\t\t%d\n", i, parsed.evaluate(i, 0, 0));
            } catch (DivisionByZeroException e) {
                System.out.printf("%d\t\tdivision by zero\n", i);
            } catch (OverflowException e) {
                System.out.printf("%d\t\toverflow\n", i);
            } catch (EvaluationException e) {
                System.out.printf("%d\t\tevaluation exception\n", i);
            }
        }
    }

}
