package cc.domovoi.poi.ooxml.template.datasupplier;

import cc.domovoi.poi.ooxml.template.DataSupplier;

import java.util.function.Function;

public class CustomDataSupplier<T> implements DataSupplier<Object, T> {

    private Function<Object, T> operation;

    public CustomDataSupplier(Function<Object, T> operation) {
        this.operation = operation;
    }

    @Override
    public T apply(Object o) {
        return operation.apply(o);
    }
}
