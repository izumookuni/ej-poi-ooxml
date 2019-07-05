package cc.domovoi.poi.ooxml.template.datastamp;

import cc.domovoi.poi.ooxml.template.PainterContext;

import java.util.Map;
import java.util.function.Function;

public interface DataStamp<T> {

    String getId();

    Function<? super PainterContext, ? extends Map<String, T>> getDataMap();

    T genData(PainterContext painterContext);
}
