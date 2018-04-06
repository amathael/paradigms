package expression.calc;

import expression.exceptions.*;
import expression.exceptions.IllegalArgumentException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 06-Apr-18
 * @time 15:13
 */

public class ShortCalculator implements Calculator<Short> {

    private final boolean EXCEPTION_IGNORE;

    public ShortCalculator() {
        EXCEPTION_IGNORE = false;
    }

    public ShortCalculator(boolean exceptionIgnore) {
        EXCEPTION_IGNORE = exceptionIgnore;
    }

    @Override
    public void throwEvaluationException(EvaluationException e) throws EvaluationException {
        if (!EXCEPTION_IGNORE) {
            throw e;
        }
    }

    @Override
    public Short parseString(String string) throws NumberParsingException {
        try {
            return Short.parseShort(string);
        } catch (NumberFormatException e) {
            try {
                return (short) Integer.parseInt(string);
            } catch (NumberFormatException e2) {
                throw new NumberParsingException(String.format("Can't parse Short or Integer from %s", string));
            }
        }
    }

    @Override
    public Short min(Short left, Short right) {
        return (short) Math.min(left, right);
    }

    @Override
    public Short max(Short left, Short right) {
        return (short) Math.max(left, right);
    }

    @Override
    public Short add(Short left, Short right) throws EvaluationException {
        if (left > 0 && Short.MAX_VALUE - left < right || left < 0 && Short.MIN_VALUE - left > right) {
            throwEvaluationException(new OverflowException());
        }
        return (short) (left + right);
    }

    @Override
    public Short sub(Short left, Short right) throws EvaluationException {
        if (left >= 0 && left - Short.MAX_VALUE > right || left < 0 && left - Short.MIN_VALUE < right) {
            throwEvaluationException(new OverflowException());
        }
        return (short) (left - right);
    }

    @Override
    public Short neg(Short value) throws EvaluationException {
        if (value == Short.MIN_VALUE) {
            throwEvaluationException(new OverflowException());
        }
        return (short) -value;
    }

    @Override
    public Short mul(Short left, Short right) throws EvaluationException {
        int limit = left > 0 == right > 0 ? Short.MAX_VALUE : Short.MIN_VALUE;
        if (left < 0 && right < 0 && limit / left > right ||
                left < 0 && right > 0 && limit / right > left ||
                left > 0 && right < 0 && limit / left > right ||
                left > 0 && right > 0 && limit / right < left) {
            throwEvaluationException(new OverflowException());
        }
        return (short) (left * right);
    }

    @Override
    public Short div(Short left, Short right) throws EvaluationException {
        if (right == 0) {
            throw new DivisionByZeroException();
        }
        if (left == Short.MIN_VALUE && right == -1) {
            throwEvaluationException(new OverflowException());
        }
        return (short) (left / right);
    }

    @Override
    public Short bitCount(Short value) {
        return (short) Integer.bitCount(Short.toUnsignedInt(value));
    }

    @Override
    public Short not(Short value) {
        return (short) ~value;
    }

    @Override
    public Short and(Short left, Short right) {
        return (short) (left & right);
    }

    @Override
    public Short or(Short left, Short right) {
        return (short) (left | right);
    }

    @Override
    public Short xor(Short left, Short right) {
        return (short) (left ^ right);
    }

    @Override
    @Deprecated
    public Short log(Short left, Short right) throws EvaluationException {
        if (left <= 0) {
            throw new IllegalArgumentException(String.format("Log argument %d is non-positive", left));
        } else if (right <= 1) {
            throw new IllegalArgumentException(String.format("Log base %d is lesser than 2", right));
        }
        short l = 0, r = 16;
        while (r - l > 1) {
            short m = (short) ((l + r) / 2);
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
    public Short pow(Short left, Short right) throws EvaluationException {
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
                return mul(pow(left, (short) (right - 1)), left);
            } else {
                short half = pow(left, (short) (right / 2));
                return mul(half, half);
            }
        }
    }
    
}
