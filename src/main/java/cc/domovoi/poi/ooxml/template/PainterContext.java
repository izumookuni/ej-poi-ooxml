package cc.domovoi.poi.ooxml.template;

import cc.domovoi.poi.ooxml.Workbooks;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class PainterContext {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Map<String, List<DataPainter>> rootDataPaintMap = new LinkedHashMap<>();

    private Map<String, DataPainter> dataPainterMap = new LinkedHashMap<>();

    private Map<String, CellStyle> cellStyleMap = new HashMap<>();

    private Map<String, Font> fontMap = new HashMap<>();

    private Map<String, Object> dataMap = new LinkedHashMap<>();

    private Map<String, Object> tempData = new HashMap<>();

    private Workbook workbook;

    private Map<Predicate<DataPainter>, Supplier<Object>> customDataGetter = new LinkedHashMap<>();

    private Sheet lastSheet;

//    private Row lastRow;

    private Integer lastRowIndex;

    private Integer lastColIndex;

    private Integer lastRegionRowIndex;

    private Integer lastRegionColIndex;

    public void postPaint() {
//        dataPainterMap.values().stream().filter(DataPainter::root).forEach(dataPainter -> dataPainter.postPaint(this));
        rootDataPaintMap.forEach((sheetName, dataPainterList) -> {
            setLastSheet(workbook.getSheet(sheetName));
            dataPainterList.forEach(dataPainter -> dataPainter.postPaint(this));
        });
    }

    public void clear() {
        clear(true);
    }

    public void clear(boolean closeWorkbook) {
        try {
            this.rootDataPaintMap.clear();
            this.dataPainterMap.clear();
            this.cellStyleMap.clear();
            this.fontMap.clear();
            this.dataMap.clear();
            this.tempData.clear();
            if (closeWorkbook && Objects.nonNull(this.workbook)) {
                this.workbook.close();
            }
            this.workbook = null;
            this.customDataGetter.clear();
            this.lastSheet = null;
            this.lastRowIndex = null;
            this.lastColIndex = null;
            this.lastRegionRowIndex = null;
            this.lastRegionColIndex = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void detectSheet(Sheet sheet) {
        setLastSheet(sheet);
        rootDataPaintMap.putIfAbsent(sheet.getSheetName(), new ArrayList<>());
    }

    public void attachDataPainter(String id, DataPainter dataPainter) {
        dataPainterMap.putIfAbsent(id, dataPainter);
        logger.debug(String.format("attachDataPainter(%s -> %s)", id, dataPainter));
        if (dataPainter.root()) {
            if (Objects.isNull(lastSheet)) {
                detectSheet(Workbooks.createSheet(workbook));
            }
            rootDataPaintMap.get(lastSheet.getSheetName()).add(dataPainter);
        }
    }

    public void attachDataGetter(Predicate<DataPainter> p, Supplier<Object> customData) {
        customDataGetter.put(p, customData);
    }

    public Object genData(DataPainter dataPainter) {
        logger.debug(String.format("%s match: ", dataPainter) + customDataGetter.keySet().stream().anyMatch(p -> p.test(dataPainter)));
        return customDataGetter.entrySet().stream().filter(entry -> entry.getKey().test(dataPainter)).findFirst().map(Map.Entry::getValue).orElse(this::getData).get();
//        return customDataGetter.entrySet().stream().filter(entry -> entry.getKey().test(dataPainter)).findFirst().map(Map.Entry::getValue).orElseGet(CustomDataSupplier::self).apply(data);
    }

    public Object getData() {
        return dataMap.get(lastSheet.getSheetName());
    }

    public void setData(Object data) {
        if (Objects.isNull(lastSheet)) {
            detectSheet(Workbooks.createSheet(workbook));
        }
        dataMap.putIfAbsent(lastSheet.getSheetName(), data);
    }

    public void drawCell(String id, CellStyle cellStyle, Object data) {
//        CellDataPainter<?> dataPainter = (CellDataPainter<?>) dataPainterMap.get(id);
//        Row row = lastSheet.getRow(dataPainter.getRowIndex());
//        if (row == null) {
//            row = lastSheet.createRow(dataPainter.getRowIndex());
//        }
//        Cell cell = row.createCell(dataPainter.getColIndex());
//        if (Objects.nonNull(cellStyle)) {
//            cell.setCellStyle(cellStyle);
//        }
//        if (Objects.nonNull(data)) {
//            CellValueSetters.forClass(dataPainter.dataClass()).setCellValue(data, cell);
//        }
//        else {
//            cell.setCellValue(EmptyDataSupplier.empty.apply(null));
//        }
    }

    public Map<String, List<DataPainter>> getRootDataPaintMap() {
        return rootDataPaintMap;
    }

    public void setRootDataPaintMap(Map<String, List<DataPainter>> rootDataPaintMap) {
        this.rootDataPaintMap = rootDataPaintMap;
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

    public Map<String, Font> getFontMap() {
        return fontMap;
    }

    public void setFontMap(Map<String, Font> fontMap) {
        this.fontMap = fontMap;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
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

    public Map<Predicate<DataPainter>, Supplier<Object>> getCustomDataGetter() {
        return customDataGetter;
    }

    public void setCustomDataGetter(Map<Predicate<DataPainter>, Supplier<Object>> customDataGetter) {
        this.customDataGetter = customDataGetter;
    }

    public Sheet getLastSheet() {
        return lastSheet;
    }

    public void setLastSheet(Sheet lastSheet) {
        this.lastSheet = lastSheet;
    }

//    public Row getLastRow() {
//        return lastRow;
//    }
//
//    public void setLastRow(Row lastRow) {
//        this.lastRow = lastRow;
//    }

    public Integer getLastRowIndex() {
        return lastRowIndex;
    }

    public void setLastRowIndex(Integer lastRowIndex) {
        this.lastRowIndex = lastRowIndex;
    }

    public Integer getLastColIndex() {
        return lastColIndex;
    }

    public void setLastColIndex(Integer lastColIndex) {
        this.lastColIndex = lastColIndex;
    }

    public Integer getLastRegionRowIndex() {
        return lastRegionRowIndex;
    }

    public void setLastRegionRowIndex(Integer lastRegionRowIndex) {
        this.lastRegionRowIndex = lastRegionRowIndex;
    }

    public Integer getLastRegionColIndex() {
        return lastRegionColIndex;
    }

    public void setLastRegionColIndex(Integer lastRegionColIndex) {
        this.lastRegionColIndex = lastRegionColIndex;
    }
}
