package expression;

import expression.exceptions.NumberParsingException;
import expression.exceptions.TabulatorRangeException;
import expression.exceptions.UnknownModeException;
import expression.generic.GenericTabulator;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 06-Apr-18
 * @time 15:02
 */

public class Main8 {

    public static void main(String[] args) {
        new Main8().test(args[0], args[1], 1, 10, 1, 10, 1, 10);
    }

    private void test(String expression, String mode, int x1, int x2, int y1, int y2, int z1, int z2) {
        Object[][][] table = null;
        try {
            table = new GenericTabulator().tabulate(mode, expression, x1, x2, y1, y2, z1, z2);
        } catch (UnknownModeException | NumberParsingException | TabulatorRangeException e) {
            System.out.println("exception while building");
            System.exit(0);
        }

        System.out.println("Table:");
        for (int x = x1; x <= x2; x++) {
            System.out.printf("\nWhere x == %d...\ny\\z\t\t\t\t", x);
            for (int z = z1; z <= z2; z++) {
                System.out.printf("%d\t\t\t\t", z);
            }
            System.out.println();
            for (int y = y1; y <= y2; y++) {
                System.out.printf("%d\t\t\t\t", y);
                for (int z = z1; z <= z2; z++) {
                    System.out.print(table[x - x1][y - y1][x - z1]);
                    System.out.print("\t\t");
                }
                System.out.println();
            }
        }
    }

}
