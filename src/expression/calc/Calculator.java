package expression.calc;

import expression.exceptions.EvaluationException;
import expression.exceptions.NumberConversionException;
import expression.exceptions.OperationUnsupportedException;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 02-Apr-18
 * @time 12:51
 */

public interface Calculator<T> {

    <P> T valueOf(P value) throws NumberConversionException;

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
    };

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
