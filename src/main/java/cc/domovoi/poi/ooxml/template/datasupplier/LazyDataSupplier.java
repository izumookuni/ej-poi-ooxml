package cc.domovoi.poi.ooxml.template.datasupplier;

import cc.domovoi.poi.ooxml.template.DataSupplier;

import java.util.function.Supplier;

public class LazyDataSupplier<T> implements DataSupplier<Object, T> {

    private Class<T> dataType;

    private Supplier<? extends T> supplier;

    public LazyDataSupplier(Class<T> dataType, Supplier<? extends T> supplier) {
        this.dataType = dataType;
        this.supplier = supplier;
    }

    @Override
    public Class<T> dataType() {
        return dataType;
    }

    @Override
    public T apply(Object o) {
        return supplier.get();
    }
}
