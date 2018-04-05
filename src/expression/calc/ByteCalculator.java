package expression.calc;

import expression.exceptions.*;
import expression.exceptions.IllegalArgumentException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 05-Apr-18
 * @time 13:43
 */

public class ByteCalculator implements Calculator<Byte> {

    private final boolean EXCEPTION_IGNORE;

    public ByteCalculator() {
        EXCEPTION_IGNORE = false;
    }

    public ByteCalculator(boolean exceptionIgnore) {
        EXCEPTION_IGNORE = exceptionIgnore;
    }

    @Override
    public void throwEvaluationException(EvaluationException e) throws EvaluationException {
        if (!EXCEPTION_IGNORE) {
            throw e;
        }
    }

    @Override
    public Byte parseString(String string) throws NumberParsingException {
        try {
            return Byte.parseByte(string);
        } catch (NumberFormatException e) {
            throw new NumberParsingException(String.format("Can't parse Byte from %s", string));
        }
    }

    @Override
    public Byte add(Byte left, Byte right) throws EvaluationException {
        if (left > 0 && Byte.MAX_VALUE - left < right || left < 0 && Byte.MIN_VALUE - left > right) {
            throwEvaluationException(new OverflowException());
        }
        return (byte) (left + right);
    }

    @Override
    public Byte sub(Byte left, Byte right) throws EvaluationException {
        if (left >= 0 && left - Byte.MAX_VALUE > right || left < 0 && left - Byte.MIN_VALUE < right) {
            throwEvaluationException(new OverflowException());
        }
        return (byte) (left - right);
    }

    @Override
    public Byte neg(Byte value) throws EvaluationException {
        if (value == Byte.MIN_VALUE) {
            throwEvaluationException(new OverflowException());
        }
        return (byte) -value;
    }

    @Override
    public Byte mul(Byte left, Byte right) throws EvaluationException {
        int limit = left > 0 == right > 0 ? Byte.MAX_VALUE : Byte.MIN_VALUE;
        if (left < 0 && right < 0 && limit / left > right ||
                left < 0 && right > 0 && limit / right > left ||
                left > 0 && right < 0 && limit / left > right ||
                left > 0 && right > 0 && limit / right < left) {
            throwEvaluationException(new OverflowException());
        }
        return (byte) (left * right);
    }

    @Override
    public Byte div(Byte left, Byte right) throws EvaluationException {
        if (right == 0) {
            throwEvaluationException(new DivisionByZeroException());
        }
        if (left == Byte.MIN_VALUE && right == -1) {
            throwEvaluationException(new OverflowException());
        }
        return (byte) (left / right);
    }

    @Override
    public Byte bitCount(Byte value) {
        return (byte) Integer.bitCount(value);
    }

    @Override
    public Byte not(Byte value) {
        return (byte) ~value;
    }

    @Override
    public Byte and(Byte left, Byte right) {
        return (byte) (left & right);
    }

    @Override
    public Byte or(Byte left, Byte right) {
        return (byte) (left | right);
    }

    @Override
    public Byte xor(Byte left, Byte right) {
        return (byte) (left ^ right);
    }

    @Override
    public Byte log(Byte left, Byte right) throws EvaluationException {
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
                greater = pow(right, (byte) m) > left;
            } catch (OverflowException e) {
                greater = true;
            }
            if (greater) {
                r = m;
            } else {
                l = m;
            }
        }
        return (byte) l;
    }

    @Override
    public Byte pow(Byte left, Byte right) throws EvaluationException {
        if (right < 0) {
            throw new IllegalArgumentException(String.format("Negative power base %d is invalid in bytes", right));
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
                return mul(pow(left, (byte) (right - 1)), left);
            } else {
                byte half = pow(left, (byte) (right / 2));
                return mul(half, half);
            }
        }
    }

}
