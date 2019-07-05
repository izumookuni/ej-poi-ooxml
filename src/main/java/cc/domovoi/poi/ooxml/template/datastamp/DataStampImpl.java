package cc.domovoi.poi.ooxml.template.datastamp;

import cc.domovoi.poi.ooxml.template.PainterContext;

import java.util.Map;
import java.util.function.Function;

public abstract class DataStampImpl<T> implements DataStamp<T> {

    private String id;

    private Function<? super PainterContext, ? extends Map<String, T>> dataMap;

    public DataStampImpl(String id, Function<? super PainterContext, ? extends Map<String, T>> dataMap) {
        this.id = id;
        this.dataMap = dataMap;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Function<? super PainterContext, ? extends Map<String, T>> getDataMap() {
        return dataMap;
    }
}
