package cc.domovoi.poi.ooxml.template.datasupplier;

import cc.domovoi.poi.ooxml.template.DataSupplier;

public class ConstDataSupplier<O> implements DataSupplier<Object, O> {

    private O data;

    private Class<O> dataType;

    public ConstDataSupplier(Class<O> dataType, O data) {
        this.data = data;
        this.dataType = dataType;
    }

    @Override
    public O apply(Object o) {
        return data;
    }

    @Override
    public Class<O> dataType() {
        return dataType;
    }
}
