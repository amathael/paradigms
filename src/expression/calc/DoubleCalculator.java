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
    private boolean PRECISION_LOSS_IGNORE = false;

    public DoubleCalculator(boolean PRECISION_LOSS_IGNORE) {
        this.PRECISION_LOSS_IGNORE = PRECISION_LOSS_IGNORE;
    }

    @Override
    public <P> Double valueOf(P value) throws NumberConversionException {
        try {
            return Double.valueOf(String.valueOf(value));
        } catch (ClassCastException | NumberFormatException e) {
            throw new NumberConversionException("Cannot cast " + String.valueOf(value) + " to Double");
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
            throw new OverflowException();
        }
        return left + right;
    }

    @Override
    public Double sub(Double left, Double right) throws EvaluationException {
        checkNaN(left, right);
        if (left > 0 && left - Double.MAX_VALUE > right || left < 0 && left + Double.MAX_VALUE < right) {
            throw new OverflowException();
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
        if (absRight < 1 && Double.MIN_VALUE / absRight > absLeft && !PRECISION_LOSS_IGNORE) {
            throw new PrecisionLossException();
        }
        if (absRight > 1 && Double.MAX_VALUE / absRight < absLeft) {
            throw new OverflowException();
        }
        return left * right;
    }

    @Override
    public Double div(Double left, Double right) throws EvaluationException {
        checkNaN(left, right);
        if (right == 0) {
            throw new DivisionByZeroException();
        }
        double absLeft = left >= 0 ? left : -left, absRight = right > 0 ? right : -right;
        if (absRight < 1 && Double.MAX_VALUE * absRight < absLeft) {
            throw new OverflowException();
        }
        if (absRight > 1 && Double.MIN_VALUE * absRight < absLeft && !PRECISION_LOSS_IGNORE) {
            throw new PrecisionLossException();
        }
        return left / right;
    }

    @Override
    @Deprecated
    public Double log(Double left, Double right) throws EvaluationException {
        if (left <= 0) {
            throw new IllegalArgumentException(String.format("Log argument %f is negative", left));
        } else if (right < 0) {
            throw new IllegalArgumentException(String.format("Log base %f is negative", right));
        } else if (right == 1) {
            throw new IllegalArgumentException("Log with base 1 is not a determined value");
        }
        boolean negate = false;
        if (right < 1) {
            negate = true;
            right = 1 / right;
        }
        if (left < 1) {
            negate = !negate;
            left = 1 / left;
        }
        double l = 0, r = Double.MAX_VALUE;
        while (r - l > eps) {
            double m = (l + r) / 2;
            boolean greater = false;
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
        return negate ? l : -l;
    }

    @Override
    @Deprecated
    public Double pow(Double left, Double right) throws EvaluationException {
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
