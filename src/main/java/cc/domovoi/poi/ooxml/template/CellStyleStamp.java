package cc.domovoi.poi.ooxml.template;

import cc.domovoi.poi.ooxml.template.datastamp.DataStamp;
import cc.domovoi.poi.ooxml.template.datastamp.DataStampImpl;
import cc.domovoi.poi.ooxml.template.datastamp.SimpleDataStampImpl;
import org.apache.poi.ss.usermodel.CellStyle;

import java.util.function.Consumer;

public class CellStyleStamp extends DataStampImpl<CellStyle> {

    public static DataStamp<CellStyle> stamp(String id) {
        return new SimpleDataStampImpl<>(id, PainterContext::getCellStyleMap);
    }

    private Consumer<? super CellStyle> consumer;

    public CellStyleStamp(String id, Consumer<? super CellStyle> consumer) {
        super(id, PainterContext::getCellStyleMap);
        this.consumer = consumer;
    }

    @Override
    public CellStyle genData(PainterContext painterContext) {
        CellStyle cellStyle = painterContext.getWorkbook().createCellStyle();
        consumer.accept(cellStyle);
        getDataMap().apply(painterContext).put(getId(), cellStyle);
        return cellStyle;
    }
}
