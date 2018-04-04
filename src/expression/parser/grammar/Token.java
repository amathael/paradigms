package expression.parser.grammar;

import expression.calc.Calculator;
import expression.calc.IntegerCalculator;
import expression.elements.AbstractBinaryOperation;
import expression.elements.AbstractUnaryOperation;
import expression.elements.TripleExpression;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * Created by isuca in paradigms catalogue
 *
 * @date 30-Mar-18
 * @time 07:25
 */

@SuppressWarnings("WeakerAccess")
public class Token {

    private String name;
    private Calculator<Integer> calc = new IntegerCalculator();

    private Class<? extends AbstractBinaryOperation> binaryOperation;

    private Class<? extends AbstractUnaryOperation> unaryOperation;

    public Token(String name) {
        this.name = name;
        binaryOperation = null;
        unaryOperation = null;
    }

    public Token(String name, Class<? extends AbstractBinaryOperation> binaryOperation, Class<? extends AbstractUnaryOperation> unaryOperation) {
        this.name = name;
        this.binaryOperation = binaryOperation;
        this.unaryOperation = unaryOperation;
    }

    public String getName() {
        return name;
    }

    public TripleExpression apply(TripleExpression left, TripleExpression right) {
        assert binaryOperation != null : "No binary operation for this token";
        try {
            return binaryOperation.getDeclaredConstructor(TripleExpression.class, TripleExpression.class,
                    Calculator.class).newInstance(left, right, calc);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new NotImplementedException();
        }
    }

    public TripleExpression apply(TripleExpression value) {
        assert unaryOperation != null : "No unary operation for this token";
        try {
            return unaryOperation.getDeclaredConstructor(TripleExpression.class, Calculator.class).newInstance(value, calc);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new NotImplementedException();
        }
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
