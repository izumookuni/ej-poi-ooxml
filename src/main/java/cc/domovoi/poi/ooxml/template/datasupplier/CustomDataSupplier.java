package cc.domovoi.poi.ooxml.template.datasupplier;

import cc.domovoi.poi.ooxml.template.DataSupplier;
import cc.domovoi.poi.ooxml.template.datatype.DataType;

import java.lang.reflect.ParameterizedType;
import java.util.function.Function;

public class CustomDataSupplier<T> implements DataSupplier<Object, T> {

    @SuppressWarnings("unchecked")
    public static <T2> CustomDataSupplier<T2> self() {
        return new CustomDataSupplier<>(o -> (T2) o);
    }

    private Function<Object, T> operation;

    public CustomDataSupplier(Function<Object, T> operation) {
        this.operation = operation;
    }

    @Override
    public T apply(Object o) {
        return operation.apply(o);
    }

    @Override
    public Class<T> dataType() {
        return (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
