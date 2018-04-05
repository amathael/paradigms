package expression.calc;

import expression.exceptions.*;
import expression.exceptions.IllegalArgumentException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 05-Apr-18
 * @time 20:58
 */

public class FloatCalculator implements Calculator<Float> {

    private final Float eps = Float.MIN_VALUE;
    private final boolean EXCEPTION_IGNORE;

    public FloatCalculator() {
        EXCEPTION_IGNORE = true;
    }

    public FloatCalculator(boolean overflowIgnore, boolean precisionLossIgnore) {
        EXCEPTION_IGNORE = overflowIgnore;
    }

    @Override
    public void throwEvaluationException(EvaluationException e) throws EvaluationException {
        if (!EXCEPTION_IGNORE) {
            throw e;
        }
    }

    @Override
    public Float parseString(String string) throws NumberParsingException {
        try {
            return Float.parseFloat(string);
        } catch (NumberFormatException e) {
            throw new NumberParsingException(String.format("Can't parse Float form %s", string));
        }
    }

    private void checkNaN(float value) throws expression.exceptions.IllegalArgumentException {
        if (Float.isNaN(value)) {
            throw new expression.exceptions.IllegalArgumentException("NaN given");
        }
    }

    private void checkNaN(Float left, Float right) throws expression.exceptions.IllegalArgumentException {
        checkNaN(left);
        checkNaN(right);
    }

    @Override
    public Float add(Float left, Float right) throws EvaluationException {
        checkNaN(left, right);
        if (left > 0 && Float.MAX_VALUE - left < right || left < 0 && -Float.MAX_VALUE - left > right) {
            throwEvaluationException(new OverflowException());
        }
        return left + right;
    }

    @Override
    public Float sub(Float left, Float right) throws EvaluationException {
        checkNaN(left, right);
        if (left > 0 && left - Float.MAX_VALUE > right || left < 0 && left + Float.MAX_VALUE < right) {
            throwEvaluationException(new OverflowException());
        }
        return left - right;
    }

    @Override
    public Float neg(Float value) throws EvaluationException {
        checkNaN(value);
        return -value;
    }

    @Override
    public Float mul(Float left, Float right) throws EvaluationException {
        checkNaN(left, right);
        Float absLeft = left >= 0 ? left : -left, absRight = right > 0 ? right : -right;
        if (absRight < 1 && Float.MIN_VALUE / absRight > absLeft) {
            throwEvaluationException(new PrecisionLossException());
        }
        if (absRight > 1 && Float.MAX_VALUE / absRight < absLeft) {
            throwEvaluationException(new OverflowException());
        }
        return left * right;
    }

    @Override
    public Float div(Float left, Float right) throws EvaluationException {
        checkNaN(left, right);
        if (right == 0) {
            throwEvaluationException(new DivisionByZeroException());
        }
        Float absLeft = left >= 0 ? left : -left, absRight = right > 0 ? right : -right;
        if (absRight < 1 && Float.MAX_VALUE * absRight < absLeft) {
            throwEvaluationException(new OverflowException());
        }
        if (absRight > 1 && Float.MIN_VALUE * absRight < absLeft) {
            throwEvaluationException(new PrecisionLossException());
        }
        return left / right;
    }

    @Override
    @Deprecated
    public Float log(Float left, Float right) throws EvaluationException {
        checkNaN(left, right);
        if (left <= 0) {
            throw new expression.exceptions.IllegalArgumentException(String.format("Log argument %f is negative", left));
        } else if (right < 0) {
            throw new expression.exceptions.IllegalArgumentException(String.format("Log base %f is negative", right));
        } else if (right == 1) {
            throw new expression.exceptions.IllegalArgumentException("Log with base 1 is not a determined value");
        }
        double res = new DoubleCalculator(EXCEPTION_IGNORE).pow((double) left, (double) right);
        if (res < -Float.MAX_VALUE || res > Float.MAX_VALUE) {
            throwEvaluationException(new OverflowException());
        }
        return (float) res;
    }

    @Override
    @Deprecated
    public Float pow(Float left, Float right) throws EvaluationException {
        checkNaN(left, right);
        if (left < 0) {
            throw new expression.exceptions.IllegalArgumentException(String.format("Negative power base %f is incorrect in non-integers", left));
        }
        if (left == 0) {
            if (right <= 0) {
                throw new IllegalArgumentException("Zero in non-positive degree is not a determined value");
            }
            return 0f;
        }
        double res = new DoubleCalculator(EXCEPTION_IGNORE).log((double) left, (double) right);
        if (res < -Float.MAX_VALUE || res > Float.MAX_VALUE) {
            throwEvaluationException(new OverflowException());
        }
        return (float) res;
    }
    
}
