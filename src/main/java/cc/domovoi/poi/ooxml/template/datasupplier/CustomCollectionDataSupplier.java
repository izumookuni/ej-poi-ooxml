package cc.domovoi.poi.ooxml.template.datasupplier;

import java.util.Collection;
import java.util.function.Function;

public class CustomCollectionDataSupplier<T> extends CustomDataSupplier<Collection<T>> {

    private Class<T> innerDataType;

    public CustomCollectionDataSupplier(Class<T> clazz, Function<Object, Collection<T>> operation) {
        super(null, operation);
        this.innerDataType = clazz;
    }

    public Class<T> innerDataType() {
        return innerDataType;
    }
}
