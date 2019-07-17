package cc.domovoi.poi.ooxml.template;

import cc.domovoi.lambda.EJLambda;
import cc.domovoi.poi.ooxml.template.datasupplier.ConditionDataSupplier;
import cc.domovoi.poi.ooxml.template.datasupplier.GroupedDataSupplier;

import java.util.function.Function;
import java.util.function.Predicate;

public interface DataSupplier<I, O> extends Function<I, O> {

    Class<O> dataType();

    default GroupedDataSupplier<I, O> grouped() {
        return new GroupedDataSupplier<>(dataType(), this);
    }

    default GroupedDataSupplier<I, O> grouped(Class<O> clazz) {
        return new GroupedDataSupplier<>(clazz, this);
    }

    default GroupedDataSupplier<I, O> grouped(DataSupplier<I, O> that) {
        return new GroupedDataSupplier<>(dataType(), this).add(that);
    }

    default GroupedDataSupplier<I, O> grouped(Class<O> clazz, DataSupplier<I, O> that) {
        return new GroupedDataSupplier<>(clazz, this).add(that);
    }

    default ConditionDataSupplier<I, O> condition() {
        return new ConditionDataSupplier<>(dataType(), EJLambda.predicateTrue(), EJLambda.predicateTrue(), this);
    }

    default ConditionDataSupplier<I, O> condition(Predicate<? super I> inputPredicate, Predicate<? super O> outputPredicate) {
        return new ConditionDataSupplier<>(dataType(), inputPredicate, outputPredicate, this);
    }

    default ConditionDataSupplier<I, O> condition(Class<O> clazz) {
        return new ConditionDataSupplier<>(clazz, EJLambda.predicateTrue(), EJLambda.predicateTrue(), this);
    }

    default ConditionDataSupplier<I, O> condition(Class<O> clazz, Predicate<? super I> inputPredicate, Predicate<? super O> outputPredicate) {
        return new ConditionDataSupplier<>(clazz, inputPredicate, outputPredicate, this);
    }

    default ConditionDataSupplier<I, O> inCondition(Predicate<? super I> predicate) {
        return new ConditionDataSupplier<>(dataType(), predicate, EJLambda.predicateTrue(), this);
    }

    default ConditionDataSupplier<I, O> inCondition(Class<O> clazz, Predicate<? super I> predicate) {
        return new ConditionDataSupplier<>(clazz, predicate, EJLambda.predicateTrue(), this);
    }

    default ConditionDataSupplier<I, O> outCondition(Predicate<? super O> predicate) {
        return new ConditionDataSupplier<>(dataType(), EJLambda.predicateTrue(), predicate, this);
    }

    default ConditionDataSupplier<I, O> outCondition(Class<O> clazz, Predicate<? super O> predicate) {
        return new ConditionDataSupplier<>(clazz, EJLambda.predicateTrue(), predicate, this);
    }

}
