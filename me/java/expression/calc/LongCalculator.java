package java.expression.calc;

import java.lang.IllegalArgumentException;
import java.expression.exceptions.DivisionByZeroException;
import java.expression.exceptions.EvaluationException;
import java.expression.exceptions.NumberParsingException;
import java.expression.exceptions.OverflowException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 06-Apr-18
 * @time 15:11
 */

public class LongCalculator implements Calculator<Long> {

    private final boolean EXCEPTION_IGNORE;

    public LongCalculator() {
        EXCEPTION_IGNORE = false;
    }

    public LongCalculator(boolean exceptionIgnore) {
        EXCEPTION_IGNORE = exceptionIgnore;
    }

    @Override
    public void throwEvaluationException(EvaluationException e) throws EvaluationException {
        if (!EXCEPTION_IGNORE) {
            throw e;
        }
    }

    @Override
    public Long parseString(String string) throws NumberParsingException {
        try {
            return Long.parseLong(string);
        } catch (NumberFormatException e) {
            throw new NumberParsingException(String.format("Can't parse Long from %s", string));
        }
    }

    @Override
    public Long min(Long left, Long right) {
        return Math.min(left, right);
    }

    @Override
    public Long max(Long left, Long right) {
        return Math.max(left, right);
    }

    @Override
    public Long add(Long left, Long right) throws EvaluationException {
        if (left > 0 && Long.MAX_VALUE - left < right || left < 0 && Long.MIN_VALUE - left > right) {
            throwEvaluationException(new OverflowException());
        }
        return left + right;
    }

    @Override
    public Long sub(Long left, Long right) throws EvaluationException {
        if (left >= 0 && left - Long.MAX_VALUE > right || left < 0 && left - Long.MIN_VALUE < right) {
            throwEvaluationException(new OverflowException());
        }
        return left - right;
    }

    @Override
    public Long neg(Long value) throws EvaluationException {
        if (value == Long.MIN_VALUE) {
            throwEvaluationException(new OverflowException());
        }
        return -value;
    }

    @Override
    public Long mul(Long left, Long right) throws EvaluationException {
        long limit = left > 0 == right > 0 ? Long.MAX_VALUE : Long.MIN_VALUE;
        if (left < 0 && right < 0 && limit / left > right ||
                left < 0 && right > 0 && limit / right > left ||
                left > 0 && right < 0 && limit / left > right ||
                left > 0 && right > 0 && limit / right < left) {
            throwEvaluationException(new OverflowException());
        }
        return left * right;
    }

    @Override
    public Long div(Long left, Long right) throws EvaluationException {
        if (right == 0) {
            throw new DivisionByZeroException();
        }
        if (left == Long.MIN_VALUE && right == -1) {
            throwEvaluationException(new OverflowException());
        }
        return left / right;
    }

    @Override
    public Long bitCount(Long value) {
        return (long) Long.bitCount(value);
    }

    @Override
    public Long not(Long value) {
        return ~value;
    }

    @Override
    public Long and(Long left, Long right) {
        return left & right;
    }

    @Override
    public Long or(Long left, Long right) {
        return left | right;
    }

    @Override
    public Long xor(Long left, Long right) {
        return left ^ right;
    }

    @Override
    @Deprecated
    public Long log(Long left, Long right) throws EvaluationException {
        if (left <= 0) {
            throw new IllegalArgumentException(String.format("Log argument %d is non-positive", left));
        } else if (right <= 1) {
            throw new IllegalArgumentException(String.format("Log base %d is lesser than 2", right));
        }
        long l = 0, r = 64;
        while (r - l > 1) {
            long m = (l + r) / 2;
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
    @Deprecated
    public Long pow(Long left, Long right) throws EvaluationException {
        if (right < 0) {
            throw new IllegalArgumentException(String.format("Negative power base %d is invalid in integers", right));
        } else if (right == 0) {
            if (left == 0) {
                throw new IllegalArgumentException("Zero in zero degree is not a determined value");
            }
            return 1L;
        }
        if (right == 1) {
            return left;
        } else {
            if (right % 2 == 1) {
                return mul(pow(left, right - 1), left);
            } else {
                long half = pow(left, right / 2);
                return mul(half, half);
            }
        }
    }

}
