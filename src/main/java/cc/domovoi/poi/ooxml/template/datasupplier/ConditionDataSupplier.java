package cc.domovoi.poi.ooxml.template.datasupplier;

import cc.domovoi.lambda.EJLambda;
import cc.domovoi.poi.ooxml.template.DataSupplier;

import java.util.function.Function;
import java.util.function.Predicate;

public class ConditionDataSupplier<I, O> implements DataSupplier<I, O> {

    private Class<O> dataType;

    private Predicate<? super I> inputPredicate;

    private Predicate<? super O> outputPredicate;

    private Function<? super I, ? extends O> function;

    @SuppressWarnings("unchecked")
    public ConditionDataSupplier(Class<O> dataType) {
        this.dataType = dataType;
        this.inputPredicate = EJLambda.predicateTrue();
        this.outputPredicate = EJLambda.predicateTrue();
        this.function = i -> (O) i;
    }

    public ConditionDataSupplier(Class<O> dataType, Predicate<? super I> inputPredicate, Predicate<? super O> outputPredicate, Function<? super I, ? extends O> function) {
        this.dataType = dataType;
        this.inputPredicate = inputPredicate;
        this.outputPredicate = outputPredicate;
        this.function = function;
    }

    @Override
    public Class<O> dataType() {
        return dataType;
    }

    @Override
    public O apply(I i) {
        if (inputPredicate.test(i)) {
            O o = function.apply(i);
            if (outputPredicate.test(o)) {
                return o;
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }
}
