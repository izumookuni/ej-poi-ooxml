package cc.domovoi.poi.ooxml.template.datasupplier;

import cc.domovoi.poi.ooxml.template.DataSupplier;

public class ConstDataSupplier<O> implements DataSupplier<Object, O> {

    private O data;

    public ConstDataSupplier(O data) {
        this.data = data;
    }

    @Override
    public O apply(Object o) {
        return data;
    }
}
