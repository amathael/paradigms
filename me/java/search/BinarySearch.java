package java.search;

import java.util.Arrays;

/**
 * Created by isuca in 02-binsearch catalogue
 *
 * @date 27-Feb-18
 * @time 20:49
 */

@SuppressWarnings("WeakerAccess")
public class BinarySearch {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Expected at least one key argument");
            return;
        }
        // x != NULL

        int x = Integer.parseInt(args[0]);
        int[] a = Arrays.stream(args).skip(1).mapToInt(Integer::parseInt).toArray();

        BinarySearch processor = new BinarySearch();
//        System.out.println(processor.iterative(x, a));
        System.out.println(processor.recursive(x, a));
    }

    /*
    assuming:
            values[-1] == +inf && values[values.length] == -inf
    pre:
            for.all i = 0..values.length : values[i - 1] >= values[i]
    post:
            -1 < #R <= values.length && values[#R] <= key < values[#R - 1]
    inv:
            for.all i = 0..values.length - 1 : values[i] == values'[i]
     */
    public int iterative(int key, int[] values) {
        int left = -1, right = values.length;
        /*
        inv:
                -1 <= left < right <= values.length && values[left] > key >= values[right]
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
            if (values[mid] > key) {
                //      values[mid] > key
                left = mid;
                //  =>  values[left] > key
            } else {
                //      values[mid] <= key
                right = mid;
                //  =>  values[right] <= key
            }
            /*
                left' < mid < right'
            =>  right - left < right' - left'
            */
        }

        return right;
    }

    /*
    assuming:
            values[-1] == +inf && values[values.length] == -inf
    pre:
            for.all i = 0..values.length : values[i - 1] >= values[i]
    post:
            -1 < #R <= values.length && values[#R] <= key < values[#R - 1]
    inv:
            for.all i = 0..values.length - 1 : values[i] == values'[i]
     */
    public int recursive(int key, int[] values) {
        return recursive(key, values, -1, values.length);
    }

    /*
    assuming:
            values[-1] == +inf && values[values.length] == -inf
    pre:
            -1 <= left < right <= values.length &&
            for.all i = 0..values.length : values[i - 1] >= values[i] &&
            values[left] > key >= values[right]
    post:
            -1 < #R <= values.length && values[#R] <= key < values[#R - 1]
    inv:
            for.all i = 0..values.length - 1 : values[i] == values'[i]
     */
    private int recursive(int key, int[] values, int left, int right) {
        if (right - left == 1) {
            return right;
        }
        //  right - left >= 2
        int mid = (left + right) / 2;
        /*
        =>  right + left >= 2 * (left + 1) && right + left <= 2 * (right - 1) &&
        =>  left < mid < right &&
            for.all i = 0..values.length - 1 : values[i] == values'[i]
        */
        if (values[mid] > key) {
            //  =>  0 < right - mid < right - left && #pre:
            return recursive(key, values, mid, right);
            //  =>  post:
        } else {
            //  =>  0 < mid - left < right - left && #pre:
            return recursive(key, values, left, mid);
            //  =>  #post:
        }
    }

}
