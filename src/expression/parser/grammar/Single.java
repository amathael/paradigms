package expression.parser.grammar;

import expression.BinaryOperation;
import expression.CommonExpression;
import expression.UnaryOperation;
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
public class Single {

    private String name;

    private Class<? extends BinaryOperation> binaryOperation;

    private Class<? extends UnaryOperation> unaryOperation;

    public Single(String name) {
        this.name = name;
        binaryOperation = null;
        unaryOperation = null;
    }

    public Single(String name, Class<? extends BinaryOperation> binaryOperation, Class<? extends UnaryOperation> unaryOperation) {
        this.name = name;
        this.binaryOperation = binaryOperation;
        this.unaryOperation = unaryOperation;
    }

    public String getName() {
        return name;
    }

    public CommonExpression apply(CommonExpression left, CommonExpression right) {
        assert binaryOperation != null : "No binary operation for this token";
        try {
            return binaryOperation.getDeclaredConstructor(CommonExpression.class, CommonExpression.class).newInstance(left, right);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new NotImplementedException();
        }
    }

    public CommonExpression apply(CommonExpression value) {
        assert unaryOperation != null : "No unary operation for this token";
        try {
            return unaryOperation.getDeclaredConstructor(CommonExpression.class).newInstance(value);
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
        Single single = (Single) o;
        return Objects.equals(name, single.name) &&
                Objects.equals(binaryOperation, single.binaryOperation) &&
                Objects.equals(unaryOperation, single.unaryOperation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
