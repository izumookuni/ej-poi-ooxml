package cc.domovoi.poi.ooxml.template.datasupplier;

import cc.domovoi.poi.ooxml.template.DataSupplier;

import java.util.function.Function;

public class CustomDataSupplier<T> implements DataSupplier<Object, T> {

    @SuppressWarnings("unchecked")
    public static <T2> CustomDataSupplier<T2> self() {
        return (CustomDataSupplier<T2>) new CustomDataSupplier<>(Object.class, o -> (T2) o);
    }

    private Class<T> dataType;

    private Function<Object, T> operation;

    public CustomDataSupplier(Class<T> dataType, Function<Object, T> operation) {
        this.operation = operation;
    }

    @Override
    public T apply(Object o) {
        return operation.apply(o);
    }

    @Override
    public Class<T> dataType() {
        return dataType;
    }

    @SuppressWarnings("unchecked")
    public <V> CustomDataSupplier<V> then(Class<V> clazz, Function<? super T, ? extends V> after) {
        return new CustomDataSupplier<>(clazz, operation.andThen(after));
    }
}
