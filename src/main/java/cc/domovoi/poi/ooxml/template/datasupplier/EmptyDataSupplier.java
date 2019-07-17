package cc.domovoi.poi.ooxml.template.datasupplier;

import cc.domovoi.poi.ooxml.template.DataSupplier;

public class EmptyDataSupplier implements DataSupplier<Object, String> {

    public static EmptyDataSupplier empty = new EmptyDataSupplier();

    public EmptyDataSupplier() {
    }

    @Override
    public String apply(Object o) {
        return "";
    }

    @Override
    public Class<String> dataType() {
        return String.class;
    }
}
