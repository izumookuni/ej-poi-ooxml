package cc.domovoi.poi.ooxml.template;

import cc.domovoi.poi.ooxml.template.datastamp.DataStamp;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;
import java.util.function.BiFunction;

public class CellStyleStamp extends DataStamp<CellStyle, Workbook> {

    private static BiFunction<? super Map<String, Object>, ? super Workbook, ? extends CellStyle> dataGenerator = (p, w) -> {
        CellStyle cellStyle = w.createCellStyle();

        return cellStyle;
    };

    public CellStyleStamp(String id, Map<String, Object> propertyMap) {
        super(id, propertyMap, dataGenerator);
    }
}
