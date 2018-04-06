package expression.calc;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.EvaluationException;
import expression.exceptions.IllegalArgumentException;
import expression.exceptions.NumberParsingException;

import java.math.BigInteger;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 05-Apr-18
 * @time 08:30
 */

public class BigIntegerCalculator implements Calculator<BigInteger> {

    private BigInteger TWO = BigInteger.valueOf(2);

    @Override
    public BigInteger parseString(String string) throws NumberParsingException {
        try {
            return new BigInteger(string);
        } catch (NumberFormatException e) {
            throw new NumberParsingException(String.format("Can't parse BigInteger from %s", string));
        }
    }

    @Override
    public BigInteger min(BigInteger left, BigInteger right) {
        return left.min(right);
    }

    @Override
    public BigInteger max(BigInteger left, BigInteger right) {
        return left.max(right);
    }

    private void checkNull(BigInteger value) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException("Null given");
        }
    }

    private void checkNull(BigInteger left, BigInteger right) throws IllegalArgumentException {
        checkNull(left);
        checkNull(right);
    }

    @Override
    public BigInteger add(BigInteger left, BigInteger right) throws EvaluationException {
        checkNull(left, right);
        return left.add(right);
    }

    @Override
    public BigInteger sub(BigInteger left, BigInteger right) throws EvaluationException {
        checkNull(left, right);
        return left.subtract(right);
    }

    @Override
    public BigInteger neg(BigInteger value) throws EvaluationException {
        checkNull(value);
        return value.negate();
    }

    @Override
    public BigInteger mul(BigInteger left, BigInteger right) throws EvaluationException {
        checkNull(left, right);
        return left.multiply(right);
    }

    @Override
    public BigInteger div(BigInteger left, BigInteger right) throws EvaluationException {
        checkNull(left, right);
        if (right.equals(BigInteger.ZERO)) {
            throw new DivisionByZeroException();
        }
        return left.divide(right);
    }

    @Override
    public BigInteger bitCount(BigInteger value) throws EvaluationException {
        checkNull(value);
        return valueOf(value.bitCount());
    }

    @Override
    public BigInteger not(BigInteger value) throws EvaluationException {
        checkNull(value);
        return value.not();
    }

    @Override
    public BigInteger and(BigInteger left, BigInteger right) throws EvaluationException {
        checkNull(left, right);
        return left.and(right);
    }

    @Override
    public BigInteger or(BigInteger left, BigInteger right) throws EvaluationException {
        checkNull(left, right);
        return left.or(right);
    }

    @Override
    public BigInteger xor(BigInteger left, BigInteger right) throws EvaluationException {
        checkNull(left, right);
        return left.xor(right);
    }

    @Override
    @Deprecated
    public BigInteger log(BigInteger left, BigInteger right) throws EvaluationException {
        checkNull(left, right);
        if (left.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException(String.format("Log argument %s is non-positive", left.toString()));
        }
        if (right.compareTo(BigInteger.ONE) < 1) {
            throw new IllegalArgumentException(String.format("Log base %s is lesser than 2", right.toString()));
        }
        BigInteger l = BigInteger.ZERO, r = left;
        while (r.subtract(l).compareTo(BigInteger.ONE) > 0) {
            BigInteger m = r.add(l).divide(TWO);
            if (pow(right, m).compareTo(left) <  1) {
                l = m;
            } else {
                r = m;
            }
        }
        return l;
    }

    @Override
    @Deprecated
    public BigInteger pow(BigInteger left, BigInteger right) throws EvaluationException {
        checkNull(left, right);
        if (right.equals(BigInteger.ZERO)) {
            return BigInteger.ONE;
        } else if (right.equals(BigInteger.ONE)) {
            return left;
        } else {
            if (right.testBit(0)) {
                return mul(pow(left, right.subtract(BigInteger.ONE)), left);
            } else {
                BigInteger half = pow(left, right.divide(TWO));
                return mul(half, half);
            }
        }
    }
}
