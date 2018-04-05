package expression.calc;

import expression.exceptions.*;
import expression.exceptions.IllegalArgumentException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 02-Apr-18
 * @time 15:35
 */

public class DoubleCalculator implements Calculator<Double> {

    private final double eps = Double.MIN_VALUE;
    private final boolean OVERFLOW_IGNORE, PRECISION_LOSS_IGNORE;

    public DoubleCalculator() {
        OVERFLOW_IGNORE = true;
        PRECISION_LOSS_IGNORE = true;
    }

    public DoubleCalculator(boolean overflowIgnore, boolean precisionLossIgnore) {
        OVERFLOW_IGNORE = overflowIgnore;
        PRECISION_LOSS_IGNORE = precisionLossIgnore;
    }

    @Override
    public void throwOverflowException(OverflowException exception) throws OverflowException {
        if (!OVERFLOW_IGNORE) {
            throw exception;
        }
    }

    @Override
    public void throwPrecisionLossException() throws PrecisionLossException {
        if (!PRECISION_LOSS_IGNORE) {
            throw new PrecisionLossException();
        }
    }

    @Override
    public Double parseString(String string) throws NumberParsingException {
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            throw new NumberParsingException(String.format("Can't parse Double form %s", string));
        }
    }

    private void checkNaN(double value) throws IllegalArgumentException {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException("NaN given");
        }
    }

    private void checkNaN(double left, double right) throws IllegalArgumentException {
        checkNaN(left);
        checkNaN(right);
    }

    @Override
    public Double add(Double left, Double right) throws EvaluationException {
        checkNaN(left, right);
        if (left > 0 && Double.MAX_VALUE - left < right || left < 0 && -Double.MAX_VALUE - left > right) {
            throwOverflowException(new OverflowException());
        }
        return left + right;
    }

    @Override
    public Double sub(Double left, Double right) throws EvaluationException {
        checkNaN(left, right);
        if (left > 0 && left - Double.MAX_VALUE > right || left < 0 && left + Double.MAX_VALUE < right) {
            throwOverflowException(new OverflowException());
        }
        return left - right;
    }

    @Override
    public Double neg(Double value) throws EvaluationException {
        checkNaN(value);
        return -value;
    }

    @Override
    public Double mul(Double left, Double right) throws EvaluationException {
        checkNaN(left, right);
        double absLeft = left >= 0 ? left : -left, absRight = right > 0 ? right : -right;
        if (absRight < 1 && Double.MIN_VALUE / absRight > absLeft) {
            throwPrecisionLossException();
        }
        if (absRight > 1 && Double.MAX_VALUE / absRight < absLeft) {
            throwOverflowException(new OverflowException());
        }
        return left * right;
    }

    @Override
    public Double div(Double left, Double right) throws EvaluationException {
        checkNaN(left, right);
        if (right == 0) {
            throwOverflowException(new DivisionByZeroException());
        }
        double absLeft = left >= 0 ? left : -left, absRight = right > 0 ? right : -right;
        if (absRight < 1 && Double.MAX_VALUE * absRight < absLeft) {
            throwOverflowException(new OverflowException());
        }
        if (absRight > 1 && Double.MIN_VALUE * absRight < absLeft) {
            throwPrecisionLossException();
        }
        return left / right;
    }

    @Override
    @Deprecated
    public Double log(Double left, Double right) throws EvaluationException {
        checkNaN(left, right);
        if (left <= 0) {
            throw new IllegalArgumentException(String.format("Log argument %f is negative", left));
        } else if (right < 0) {
            throw new IllegalArgumentException(String.format("Log base %f is negative", right));
        } else if (right == 1) {
            throw new IllegalArgumentException("Log with base 1 is not a determined value");
        }
        return Math.log(left) / Math.log(right);
    }

    @Override
    @Deprecated
    public Double pow(Double left, Double right) throws EvaluationException {
        checkNaN(left, right);
        if (left < 0) {
            throw new IllegalArgumentException(String.format("Negative power base %f is incorrect in non-integers", left));
        }
        if (left == 0) {
            if (right <= 0) {
                throw new IllegalArgumentException("Zero in non-positive degree is not a determined value");
            }
            return 0.0;
        }
        return Math.pow(left, right);
    }

}
