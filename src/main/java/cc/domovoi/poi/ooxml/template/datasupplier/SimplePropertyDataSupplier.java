package cc.domovoi.poi.ooxml.template.datasupplier;

import cc.domovoi.poi.ooxml.template.DataSupplier;
import cc.domovoi.poi.ooxml.template.datatype.DataType;
import org.jooq.lambda.Seq;
import org.joor.Reflect;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.joor.Reflect.*;

public class SimplePropertyDataSupplier<T> implements DataSupplier<Object, T> {

    private Class<T> dataType;

    private List<String> properties;

    public SimplePropertyDataSupplier(Class<T> dataType, List<String> properties) {
        this.dataType = dataType;
        this.properties = properties;
    }

    public SimplePropertyDataSupplier(Class<T> dataType, String... properties) {
        this.dataType = dataType;
        this.properties = Stream.of(properties).collect(Collectors.toList());
    }

    @Override
    public T apply(Object o) {
//        Reflect reflect = on(o);
//        for (String property : properties) {
//            reflect = on((Object) reflect.get(property));
//
//        }
//        return reflect.get();
        return Seq.foldLeft(properties.stream(), on(o), (r, p) -> on((Object) r.get(p))).get();
    }

    @Override
    public Class<T> dataType() {
        return dataType;
    }
}
