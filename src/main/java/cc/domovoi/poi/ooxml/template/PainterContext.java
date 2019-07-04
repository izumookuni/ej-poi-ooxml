package cc.domovoi.poi.ooxml.template;

import cc.domovoi.poi.ooxml.template.cellvalue.CellValueSetters;
import cc.domovoi.poi.ooxml.template.datapainter.CellDataPainter;
import cc.domovoi.poi.ooxml.template.datasupplier.CustomDataSupplier;
import cc.domovoi.poi.ooxml.template.datasupplier.EmptyDataSupplier;
import org.apache.poi.ss.usermodel.*;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class PainterContext {

    private Map<String, DataPainter> dataPainterMap = new LinkedHashMap<>();

    private Map<String, CellStyle> cellStyleMap = new HashMap<>();

    private Object data;

    private Map<String, Object> tempData = new HashMap<>();

    private Workbook workbook;

    private Map<Predicate<DataPainter>, DataSupplier<Object, ?>> customDataGetter = new LinkedHashMap<>();

    private Sheet lastSheet;

    private Row lastRow;

    public void attachDataPainter(String id, DataPainter dataPainter) {
        dataPainterMap.putIfAbsent(id, dataPainter);
    }

    public void attachDataGetter(Predicate<DataPainter> p, DataSupplier<Object, ?> dataSupplier) {
        customDataGetter.put(p, dataSupplier);
    }

    public Object genData(DataPainter dataPainter) {
        return customDataGetter.entrySet().stream().filter(entry -> entry.getKey().test(dataPainter)).findFirst().map(Map.Entry::getValue).orElseGet(CustomDataSupplier::self).apply(data);
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
        if (Objects.nonNull(data)) {
            CellValueSetters.forClass(dataPainter.dataClass()).setCellValue(data, cell);
        }
        else {
            cell.setCellValue(EmptyDataSupplier.empty.apply(null));
        }
    }

    public List<DataPainter> findChildren(String id) {
        return null;
    }

    public Map<String, DataPainter> getDataPainterMap() {
        return dataPainterMap;
    }

    public void setDataPainterMap(Map<String, DataPainter> dataPainterMap) {
        this.dataPainterMap = dataPainterMap;
    }

    public Map<String, CellStyle> getCellStyleMap() {
        return cellStyleMap;
    }

    public void setCellStyleMap(Map<String, CellStyle> cellStyleMap) {
        this.cellStyleMap = cellStyleMap;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Map<String, Object> getTempData() {
        return tempData;
    }

    public void setTempData(Map<String, Object> tempData) {
        this.tempData = tempData;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    public Map<Predicate<DataPainter>, DataSupplier<Object, ?>> getCustomDataGetter() {
        return customDataGetter;
    }

    public void setCustomDataGetter(Map<Predicate<DataPainter>, DataSupplier<Object, ?>> customDataGetter) {
        this.customDataGetter = customDataGetter;
    }

    public Sheet getLastSheet() {
        return lastSheet;
    }

    public void setLastSheet(Sheet lastSheet) {
        this.lastSheet = lastSheet;
    }

    public Row getLastRow() {
        return lastRow;
    }

    public void setLastRow(Row lastRow) {
        this.lastRow = lastRow;
    }

}
