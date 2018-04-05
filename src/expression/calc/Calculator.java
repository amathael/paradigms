package expression.calc;

import expression.exceptions.*;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 02-Apr-18
 * @time 12:51
 */

public interface Calculator<T> {

    default void throwOverflowException(OverflowException exception) throws OverflowException {
        throw exception;
    }

    default void throwPrecisionLossException() throws PrecisionLossException {
        throw new PrecisionLossException();
    }

    T parseString(String string) throws NumberParsingException;

    default <P> T valueOf(P value) throws NumberParsingException {
        return parseString(String.valueOf(value));
    }

    T add(T left, T right) throws EvaluationException;

    T sub(T left, T right) throws EvaluationException;

    T neg(T value) throws EvaluationException;

    T mul(T left, T right) throws EvaluationException;

    T div(T left, T right) throws EvaluationException;

    default T bitCount(T value) throws EvaluationException {
        throw new OperationUnsupportedException("in " + this.getClass().getSimpleName());
    }

    default T not(T value) throws EvaluationException {
        throw new OperationUnsupportedException("in " + this.getClass().getSimpleName());
    }

    ;

    default T and(T left, T right) throws EvaluationException {
        throw new OperationUnsupportedException("in " + this.getClass().getSimpleName());
    }

    default T or(T left, T right) throws EvaluationException {
        throw new OperationUnsupportedException("in " + this.getClass().getSimpleName());
    }

    default T xor(T left, T right) throws EvaluationException {
        throw new OperationUnsupportedException("in " + this.getClass().getSimpleName());
    }

    T log(T left, T right) throws EvaluationException;

    T pow(T left, T right) throws EvaluationException;

}
