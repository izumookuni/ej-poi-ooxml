package cc.domovoi.poi.ooxml.template.datastamp;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class DataStamp<T, U> {

    private String id;

    private Map<String, Object> propertyMap;

    private BiFunction<? super Map<String, Object>, ? super U, ? extends T> dataGenerator;

    public T genData(Map<String, T> data, U context) {
        T d = dataGenerator.apply(propertyMap, context);
        data.put(id, dataGenerator.apply(propertyMap, context));
        return d;
    }

    public DataStamp(String id, Map<String, Object> propertyMap, BiFunction<? super Map<String, Object>, ? super U, ? extends T> dataGenerator) {
        this.id = id;
        this.propertyMap = propertyMap;
        this.dataGenerator = dataGenerator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getPropertyMap() {
        return propertyMap;
    }

    public void setPropertyMap(Map<String, Object> propertyMap) {
        this.propertyMap = propertyMap;
    }

    public BiFunction<? super Map<String, Object>, ? super U, ? extends T> getDataGenerator() {
        return dataGenerator;
    }

    public void setDataGenerator(BiFunction<? super Map<String, Object>, ? super U, ? extends T> dataGenerator) {
        this.dataGenerator = dataGenerator;
    }
}
