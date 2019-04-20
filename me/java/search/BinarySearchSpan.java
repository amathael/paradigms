package java.search;

import java.util.Arrays;

/**
 * Created by isuca in 02-binsearch catalogue
 *
 * @date 28-Feb-18
 * @time 17:25
 */

@SuppressWarnings("WeakerAccess")
public class BinarySearchSpan {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Expected at least one key argument");
            return;
        }
        // x != NULL

        int x = Integer.parseInt(args[0]);
        int[] a = Arrays.stream(args).skip(1).mapToInt(Integer::parseInt).toArray();

        BinarySearchSpan processor = new BinarySearchSpan();
        int i1 = processor.recursive(x, a, true), i2 = processor.recursive(x, a, false);
        System.out.printf("%d %d\n", i1, i2 - i1 + 1);
    }

    /*
    assuming:
            values[-1] == +inf && values[values.length] == -inf
    pre:
            for.all i = 0..values.length : values[i - 1] >= values[i]
    post:
            -1 < #R <= values.length && values[#R] <= key < values[#R - 1] (if way) ||
            -1 <= #R < values.length && values[#R] < key <= values[#R - 1] (if not way)
    inv:
            for.all i = 0..values.length - 1 : values[i] == values'[i]
     */
    public int iterative(int key, int[] values, boolean way) {
        int left = -1, right = values.length;
        /*
        inv:
                -1 <= left < right <= values.length &&
                (
                    values[left] > key >= values[right] (if way) ||
                    values[left] >= key > values[right] (if not way)
                )
        h-inv:
                right - left < right' - left' (so the time is finite)
         */
        while (right - left > 1) {
            //  right - left >= 2
            int mid = (left + right) / 2;
            /*
            =>  right + left >= 2 * (left + 1) && right + left <= 2 * (right - 1) &&
            =>  left < mid < right
            */
            if (values[mid] > key || values[mid] == key && !way) {
                /*
                    values[mid] > key (when way is true) ||
                    values[mid] >= key (when way is false)
                */
                left = mid;
                /*
                =>  values[left] > key (when way is true) ||
                =>  values[left] >= key (when way is false)
                */
            } else {
                /*
                    values[mid] <= key (when key is true) ||
                    values[mid] < key (when way is false)
                */
                right = mid;
                /*
                =>  values[right] <= key (when key is true) ||
                =>  values[right] < key (when way is false)
                */
            }
            /*
                left' < mid < right'
            =>  right - left < right' - left'
            */
        }

        return way ? right : left;
    }

    /*
    assuming:
            values[-1] == +inf && values[values.length] == -inf
    pre:
            for.all i = 0..values.length : values[i - 1] >= values[i]
    post:
            -1 < #R <= values.length && values[#R] <= key < values[#R - 1] (if way) ||
            -1 <= #R < values.length && values[#R] < key <= values[#R - 1] (if not way)
    inv:
            for.all i = 0..values.length - 1 : values[i] == values'[i]
     */
    public int recursive(int key, int[] values, boolean way) {
        return recursive(key, values, -1, values.length, way);
    }

    /*
    assuming:
            values[-1] == +inf && values[values.length] == -inf
    pre:
            -1 <= left < right <= values.length &&
            for.all i = 0..values.length : values[i - 1] >= values[i] &&
            (
                values[left] > key >= values[right] (if way) ||
                values[left] >= key > values[right] (if not way)
            )
    post:
            -1 < #R <= values.length && values[#R] <= key < values[#R - 1]
    inv:
            for.all i = 0..values.length - 1 : values[i] == values'[i]
     */
    private int recursive(int key, int[] values, int left, int right, boolean way) {
        if (right - left == 1) {
            return way ? right : left;
        }
        //  right - left >= 2
        int mid = (left + right) / 2;
        /*
        =>  right + left >= 2 * (left + 1) && right + left <= 2 * (right - 1) &&
        =>  left < mid < right &&
        */
        if (values[mid] > key || values[mid] == key && !way) {
            //  =>  0 < right - mid < right - left && #pre:
            return recursive(key, values, mid, right, way);
            //  =>  #post:
        } else {
            //  =>  0 < mid - left < right - left && #pre:
            return recursive(key, values, left, mid, way);
            //  =>  #post:
        }
    }

}
