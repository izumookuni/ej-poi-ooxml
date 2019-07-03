package cc.domovoi.poi.ooxml.template.datasupplier;

import cc.domovoi.poi.ooxml.template.DataSupplier;
import org.jooq.lambda.Seq;
import org.joor.Reflect;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.joor.Reflect.*;

public class SimplePropertyDataSupplier<T> implements DataSupplier<Object, T> {

    private List<String> properties;

    public SimplePropertyDataSupplier(List<String> properties) {
        this.properties = properties;
    }

    public SimplePropertyDataSupplier(String... properties) {
        this.properties = Stream.of(properties).collect(Collectors.toList());
    }

    @Override
    public T apply(Object o) {
        return Seq.foldLeft(properties.stream(), on(o), Reflect::get).get();
    }
}
