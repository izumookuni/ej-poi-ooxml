package cc.domovoi.poi.ooxml.template;

import cc.domovoi.poi.ooxml.Cells;
import cc.domovoi.poi.ooxml.Sheets;
import cc.domovoi.poi.ooxml.Workbooks;
import cc.domovoi.poi.ooxml.template.cellvalue.CellValueSetters;
import cc.domovoi.poi.ooxml.template.datapainter.CellDataPainter;
import cc.domovoi.poi.ooxml.template.datapainter.RegionDataPainter;
import cc.domovoi.poi.ooxml.template.datapainter.RelativeCellDataPainter;
import cc.domovoi.poi.ooxml.template.datapainter.RepeatRegionDataPainter;
import cc.domovoi.poi.ooxml.template.datasupplier.*;
import cc.domovoi.poi.ooxml.template.datatype.DataType;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class PainterApplication {

    private static PainterContext painterContext = new PainterContext();

    public static Workbook create() {
        Workbook workbook = Workbooks.create();
        painterContext.setWorkbook(workbook);
        return workbook;
    }

    public static Workbook load(String path) {
        Optional<Workbook> workbook = Workbooks.load(path);
        assert workbook.isPresent();
        painterContext.setWorkbook(workbook.get());
        return workbook.get();
    }

    public static Workbook load(File file) {
        Optional<Workbook> workbook = Workbooks.load(file);
        assert workbook.isPresent();
        painterContext.setWorkbook(workbook.get());
        return workbook.get();
    }

    public static Workbook load(InputStream inputStream) {
        Optional<Workbook> workbook = Workbooks.load(inputStream);
        assert workbook.isPresent();
        painterContext.setWorkbook(workbook.get());
        return workbook.get();
    }

    public static void write(String path) {
        Workbooks.write(painterContext.getWorkbook(), path);
    }

    public static void write(File file) {
        Workbooks.write(painterContext.getWorkbook(), file);
    }

    public static void write(OutputStream outputStream) {
        Workbooks.write(painterContext.getWorkbook(), outputStream);
    }

    public static Sheet sheet(String name) {
        Sheet sheet = Workbooks.getOrCreateSheet(painterContext.getWorkbook(), name);
        assert Objects.nonNull(sheet);
        painterContext.detectSheet(sheet);
        return sheet;
    }

    public static Sheet sheet(Integer index) {
        Sheet sheet = painterContext.getWorkbook().getSheetAt(index);
        assert Objects.nonNull(sheet);
        painterContext.detectSheet(sheet);
        return sheet;
    }

    public static void rowHeight(Number heightInPoints) {
        painterContext.getLastSheet().setDefaultRowHeightInPoints(heightInPoints.floatValue());
    }

//    public static void rowHeightSize(Short height) {
//        painterContext.getLastSheet().setDefaultRowHeight(height);
//    }

    public static void rowHeight(Integer rowIndex, Number heightInPoints) {
        Row row = Sheets.getOrCreateRow(painterContext.getLastSheet(), rowIndex);
        row.setHeightInPoints(heightInPoints.floatValue());
    }

//    public static void rowHeightSize(Integer rowIndex, Short height) {
//        Row row = Sheets.getOrCreateRow(painterContext.getLastSheet(), rowIndex);
//        row.setHeight(height);
//    }

    public static void rowHeight(String rowString, Number heightInPoints) {
        Row row = Sheets.getOrCreateRow(painterContext.getLastSheet(), Cells.convertRowStringToIndex(rowString));
        row.setHeightInPoints(heightInPoints.floatValue());
    }

//    public static void rowHeightSize(String rowString, Short height) {
//        Row row = Sheets.getOrCreateRow(painterContext.getLastSheet(), Cells.convertRowStringToIndex(rowString));
//        row.setHeight(height);
//    }

    public static void colWidth(Integer width) {
        painterContext.getLastSheet().setDefaultColumnWidth(width);
    }

    public static void colWidth(Integer colIndex, Integer width) {
        painterContext.getLastSheet().setColumnWidth(colIndex, width);
    }

    public static void colWidth(String colString, Integer width) {
        painterContext.getLastSheet().setColumnWidth(Cells.convertColStringToIndex(colString), width);
    }

    public static void data(Object data) {
        painterContext.setData(data);
    }

    public static void data(Object data, Sheet sheet) {
        painterContext.getDataMap().putIfAbsent(sheet.getSheetName(), data);
    }

    public static EmptyDataSupplier none() {
        return EmptyDataSupplier.empty;
    }

    public static ConstDataSupplier<String> str(String s) {
        return new ConstDataSupplier<>(s, String.class);
    }

    public static ConstDataSupplier<Number> num(Double num) {
        return new ConstDataSupplier<>(num, Number.class);
    }

    public static ConstDataSupplier<Boolean> bool(Boolean b) {
        return new ConstDataSupplier<>(b, Boolean.class);
    }

    public static ConstDataSupplier<Date> date(String date, String formula) {
        return new ConstDataSupplier<>(Date.from(LocalDateTime.parse(date, DateTimeFormatter.ofPattern(formula)).toInstant(ZoneOffset.of(CellValueSetters.zoneOffsetId()))), Date.class);
    }

    public static SimplePropertyDataSupplier<String> strProperty(String... properties) {
        return new SimplePropertyDataSupplier<>(String.class, Arrays.asList(properties));
    }

    public static SimplePropertyDataSupplier<Number> numProperty(String... properties) {
        return new SimplePropertyDataSupplier<>(Number.class, properties);
    }

    public static SimplePropertyDataSupplier<Boolean> boolProperty(String... properties) {
        return new SimplePropertyDataSupplier<>(Boolean.class, properties);
    }

    public static SimplePropertyDataSupplier<Date> dateProperty(String... properties) {
        return new SimplePropertyDataSupplier<>(Date.class, properties);
    }

    public static SimplePropertyDataSupplier<LocalDateTime> localDateTimeProperty(String... properties) {
        return new SimplePropertyDataSupplier<>(LocalDateTime.class, properties);
    }

    public static SimplePropertyDataSupplier<LocalDate> localDateProperty(String... properties) {
        return new SimplePropertyDataSupplier<>(LocalDate.class, properties);
    }

    public static SimplePropertyDataSupplier<LocalTime> localTimeProperty(String... properties) {
        return new SimplePropertyDataSupplier<>(LocalTime.class, properties);
    }

    public static <T> SimplePropertyDataSupplier<T> property(Class<T> clazz, String... properties) {
        return new SimplePropertyDataSupplier<>(clazz, properties);
    }

    public static <T> SimplePropertyDataSupplier<T> property(String... properties) {
        return new SimplePropertyDataSupplier<>(null, properties);
    }

    public static <T> CustomDataSupplier<T> property(Function<Object, T> operation) {
        return new CustomDataSupplier<T>(operation);
    }

    public static <T> CustomDataSupplier<T> self() {
        return CustomDataSupplier.self();
    }

    public static Font font(String id, Consumer<? super Font> consumer) {
        return new FontStamp(id, consumer).genData(painterContext);
    }

    public static Font font(String id) {
        return FontStamp.stamp(id).genData(painterContext);
    }

    public static CellStyle style(String id, Consumer<? super CellStyle> consumer) {
        return new CellStyleStamp(id, consumer).genData(painterContext);
    }

    public static void postPaint() {
        painterContext.postPaint();
    }

    public static CellStyle style(String id) {
        return CellStyleStamp.stamp(id).genData(painterContext);
    }

    /**
     * create an Absolute cell
     * @param id
     * @param absRowIndex
     * @param absColIndex
     * @param width
     * @param height
     * @param cellStyle
     * @param dataSupplier
     * @param <T>
     * @return
     */
    public static <T> String cell(String id, Integer absRowIndex, Integer absColIndex, Integer width, Integer height, CellStyle cellStyle, DataSupplier<Object, T> dataSupplier) {
        CellDataPainter<T> cellDataPainter = new CellDataPainter<>(id, absRowIndex, absColIndex, width, height, cellStyle, dataSupplier);
        cellDataPainter.init(painterContext);
        return id;
    }

    /**
     * create a Relative cell
     * @param id
     * @param regionId
     * @param relativeRowIndex
     * @param relativeColIndex
     * @param width
     * @param height
     * @param cellStyle
     * @param dataSupplier
     * @param <T>
     * @return
     */
    public static <T> String cell(String id, String regionId, Integer relativeRowIndex, Integer relativeColIndex, Integer width, Integer height, CellStyle cellStyle, DataSupplier<Object, T> dataSupplier) {
        RelativeCellDataPainter<T> cellDataPainter = new RelativeCellDataPainter<>(id, regionId, relativeRowIndex, relativeColIndex, width, height, cellStyle, dataSupplier);
        cellDataPainter.init(painterContext);
        // Todo: ...
        return id;
    }

    /**
     * add a top region
     * @param id
     * @param rowIndex
     * @param colIndex
     * @param cellStyle
     * @param dataSupplier
     * @param <T>
     * @return
     */
    public static <T> String region(String id, Integer rowIndex, Integer colIndex, CellStyle cellStyle, Boolean newline, DataSupplier<Object, T> dataSupplier) {
        RegionDataPainter<T> regionDataPainter = new RegionDataPainter<>(id, null, rowIndex, colIndex, cellStyle, newline, dataSupplier);
        regionDataPainter.init(painterContext);
        // Todo: ...
        return id;
    }

    /**
     * add a sub region
     * @param id
     * @param pid
     * @param rowIndex
     * @param colIndex
     * @param cellStyle
     * @param dataSupplier
     * @param <T>
     * @return
     */
    public static <T> String region(String id, String pid, Integer rowIndex, Integer colIndex, CellStyle cellStyle, Boolean newline, DataSupplier<Object, T> dataSupplier) {
        RegionDataPainter<T> regionDataPainter = new RegionDataPainter<>(id, pid, rowIndex, colIndex, cellStyle, newline, dataSupplier);
        regionDataPainter.init(painterContext);
        // Todo: ...
        return id;
    }

    public static <T> String repeat(String id, Integer rowIndex, Integer colIndex, CellStyle cellStyle, DataSupplier<Object, Collection<T>> dataSupplier) {
        RepeatRegionDataPainter<T> repeatRegionDataPainter = new RepeatRegionDataPainter<>(id, null, rowIndex, colIndex, cellStyle, dataSupplier);
        repeatRegionDataPainter.init(painterContext);
        return id;
    }


}
