package cc.domovoi.poi.ooxml.template;

import cc.domovoi.poi.ooxml.template.cellvalue.CellValueSetters;
import cc.domovoi.poi.ooxml.template.datapainter.CellDataPainter;
import org.apache.poi.ss.usermodel.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class PainterContext {

    private Map<String, DataPainter> dataPainterMap = new LinkedHashMap<>();

    private Map<String, CellStyle> cellStyleMap = new HashMap<>();

    private Object data;

    private Workbook workbook;

    private Map<String, Function<Object, Object>> customDataGetter = new HashMap<>();

    private Sheet lastSheet;

    private Row lastRow;

    public void attachDataPainter(String id, DataPainter dataPainter) {
        dataPainterMap.putIfAbsent(id, dataPainter);
    }

    public Object genData(String id) {
        return customDataGetter.getOrDefault(id, Function.identity()).apply(data);
    }

    public void drawCell(String id, Object data) {
        CellDataPainter<?> dataPainter = (CellDataPainter<?>) dataPainterMap.get(id);
        Row row = lastSheet.getRow(dataPainter.getRowIndex());
        if (row == null) {
            row = lastSheet.createRow(dataPainter.getRowIndex());
        }
        Cell cell = row.createCell(dataPainter.getColIndex());
        if (cellStyleMap.containsKey(id)) {
            cell.setCellStyle(cellStyleMap.get(id));
        }
        CellValueSetters.forClass(dataPainter.dataClass()).setCellValue(data, cell);
    }

    public List<DataPainter> findChildren(String id) {
        return null;
    }

}
