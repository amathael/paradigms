package java.expression.parser.grammar;

import java.expression.elements.TripleExpression;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 30-Mar-18
 * @time 07:25
 */

public class Token<T> {

    private String name;

    private BiFunction<TripleExpression<T>, TripleExpression<T>, TripleExpression<T>> binaryOperation;
    private Function<TripleExpression<T>, TripleExpression<T>> unaryOperation;

    public Token(String name) {
        this.name = name;
        binaryOperation = null;
        unaryOperation = null;
    }

    public Token(String name,
                 BiFunction<TripleExpression<T>, TripleExpression<T>, TripleExpression<T>> binaryOperation,
                 Function<TripleExpression<T>, TripleExpression<T>> unaryOperation) {
        this.name = name;
        this.binaryOperation = binaryOperation;
        this.unaryOperation = unaryOperation;
    }

    public String getName() {
        return name;
    }

    public TripleExpression<T> apply(TripleExpression<T> left, TripleExpression<T> right) {
        assert binaryOperation != null : "No binary operation for this token";
        return binaryOperation.apply(left, right);
    }

    public TripleExpression<T> apply(TripleExpression<T> arg) {
        assert unaryOperation != null : "No unary operation for this token";
        return unaryOperation.apply(arg);
    }

    public boolean isBinary() {
        return binaryOperation != null;
    }

    public boolean isUnary() {
        return unaryOperation != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(name, token.name) &&
                Objects.equals(binaryOperation, token.binaryOperation) &&
                Objects.equals(unaryOperation, token.unaryOperation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
