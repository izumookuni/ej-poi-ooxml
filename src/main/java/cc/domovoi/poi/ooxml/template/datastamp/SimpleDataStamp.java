package cc.domovoi.poi.ooxml.template.datastamp;

import java.util.Map;

public class SimpleDataStamp<T, U> extends DataStamp<T, U> {

    public SimpleDataStamp(String id) {
        super(id, null, null);
    }

    @Override
    public T genData(Map<String, T> data, U context) {
        return data.get(getId());
    }
}
