package expression.calc;

import expression.exceptions.*;
import expression.exceptions.IllegalArgumentException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 02-Apr-18
 * @time 13:56
 */

public class IntegerCalculator implements Calculator<Integer> {

    private final boolean EXCEPTION_IGNORE;

    public IntegerCalculator() {
        EXCEPTION_IGNORE = false;
    }

    public IntegerCalculator(boolean exceptionIgnore) {
        EXCEPTION_IGNORE = exceptionIgnore;
    }

    @Override
    public void throwEvaluationException(EvaluationException e) throws EvaluationException {
        if (!EXCEPTION_IGNORE) {
            throw e;
        }
    }

    @Override
    public Integer parseString(String string) throws EvaluationException {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            throwEvaluationException(new NumberParsingException(String.format("Can't parse Integer from %s", string)));
            return 0;
        }
    }

    @Override
    public Integer add(Integer left, Integer right) throws EvaluationException {
        if (left > 0 && Integer.MAX_VALUE - left < right || left < 0 && Integer.MIN_VALUE - left > right) {
            throwEvaluationException(new OverflowException());
        }
        return left + right;
    }

    @Override
    public Integer sub(Integer left, Integer right) throws EvaluationException {
        if (left >= 0 && left - Integer.MAX_VALUE > right || left < 0 && left - Integer.MIN_VALUE < right) {
            throwEvaluationException(new OverflowException());
        }
        return left - right;
    }

    @Override
    public Integer neg(Integer value) throws EvaluationException {
        if (value == Integer.MIN_VALUE) {
            throwEvaluationException(new OverflowException());
        }
        return -value;
    }

    @Override
    public Integer mul(Integer left, Integer right) throws EvaluationException {
        int limit = left > 0 == right > 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        if (left < 0 && right < 0 && limit / left > right ||
                left < 0 && right > 0 && limit / right > left ||
                left > 0 && right < 0 && limit / left > right ||
                left > 0 && right > 0 && limit / right < left) {
            throwEvaluationException(new OverflowException());
        }
        return left * right;
    }

    @Override
    public Integer div(Integer left, Integer right) throws EvaluationException {
        if (right == 0) {
            throwEvaluationException(new DivisionByZeroException());
        }
        if (left == Integer.MIN_VALUE && right == -1) {
            throwEvaluationException(new OverflowException());
        }
        return left / right;
    }

    @Override
    public Integer bitCount(Integer value) {
        return Integer.bitCount(value);
    }

    @Override
    public Integer not(Integer value) {
        return ~value;
    }

    @Override
    public Integer and(Integer left, Integer right) {
        return left & right;
    }

    @Override
    public Integer or(Integer left, Integer right) {
        return left | right;
    }

    @Override
    public Integer xor(Integer left, Integer right) {
        return left ^ right;
    }

    @Override
    public Integer log(Integer left, Integer right) throws EvaluationException {
        if (left <= 0) {
            throw new IllegalArgumentException(String.format("Log argument %d is non-positive", left));
        } else if (right <= 1) {
            throw new IllegalArgumentException(String.format("Log base %d is lesser than 2", right));
        }
        int l = 0, r = 32;
        while (r - l > 1) {
            int m = (l + r) / 2;
            boolean greater;
            try {
                greater = pow(right, m) > left;
            } catch (OverflowException e) {
                greater = true;
            }
            if (greater) {
                r = m;
            } else {
                l = m;
            }
        }
        return l;
    }

    @Override
    public Integer pow(Integer left, Integer right) throws EvaluationException {
        if (right < 0) {
            throw new IllegalArgumentException(String.format("Negative power base %d is invalid in integers", right));
        } else if (right == 0) {
            if (left == 0) {
                throw new IllegalArgumentException("Zero in zero degree is not a determined value");
            }
            return 1;
        }
        if (right == 1) {
            return left;
        } else {
            if (right % 2 == 1) {
                return mul(pow(left, right - 1), left);
            } else {
                int half = pow(left, right / 2);
                return mul(half, half);
            }
        }
    }

}
