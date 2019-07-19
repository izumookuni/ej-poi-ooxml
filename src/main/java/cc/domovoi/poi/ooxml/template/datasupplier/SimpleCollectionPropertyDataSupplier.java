package cc.domovoi.poi.ooxml.template.datasupplier;

import java.util.Collection;
import java.util.List;

public class SimpleCollectionPropertyDataSupplier<T> extends SimplePropertyDataSupplier<Collection<T>> {

    private Class<T> innerDataType;

    public SimpleCollectionPropertyDataSupplier(Class<T> clazz, List<String> properties) {
        super(null, properties);
        this.innerDataType = clazz;
    }

    public SimpleCollectionPropertyDataSupplier(Class<T> clazz, String... properties) {
        super(null, properties);
        this.innerDataType = clazz;
    }

    public Class<T> innerDataType() {
        return innerDataType;
    }

}
