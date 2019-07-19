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
import java.util.function.Supplier;

public class PainterApplication {

    private static PainterContext painterContext = new PainterContext();

    // workbook

    /**
     * Create a new workbook.
     *
     * @return new workbook
     */
    public static Workbook create() {
        Workbook workbook = Workbooks.create();
        painterContext.setWorkbook(workbook);
        return workbook;
    }

    /**
     * Load a workbook from the specified path.
     *
     * @param path path
     * @return workbook
     */
    public static Workbook load(String path) {
        Optional<Workbook> workbook = Workbooks.load(path);
        assert workbook.isPresent();
        painterContext.setWorkbook(workbook.get());
        return workbook.get();
    }

    /**
     * Load a workbook from the specified `File`.
     *
     * @param file `File`
     * @return workbook
     */
    public static Workbook load(File file) {
        Optional<Workbook> workbook = Workbooks.load(file);
        assert workbook.isPresent();
        painterContext.setWorkbook(workbook.get());
        return workbook.get();
    }

    /**
     * Load a workbook from the specified `InputStream`.
     *
     * @param inputStream `InputStream`
     * @return workbook
     */
    public static Workbook load(InputStream inputStream) {
        Optional<Workbook> workbook = Workbooks.load(inputStream);
        assert workbook.isPresent();
        painterContext.setWorkbook(workbook.get());
        return workbook.get();
    }

    /**
     * Write the workbook to the specified path.
     *
     * @param path path
     */
    public static void write(String path) {
        Workbooks.write(painterContext.getWorkbook(), path);
    }

    /**
     * Write the workbook to the specified `File`.
     *
     * @param file `File`
     */
    public static void write(File file) {
        Workbooks.write(painterContext.getWorkbook(), file);
    }

    /**
     * Write the workbook to the specified `OutputStream`.
     *
     * @param outputStream `OutputStream`
     */
    public static void write(OutputStream outputStream) {
        Workbooks.write(painterContext.getWorkbook(), outputStream);
    }

    /**
     * Create a new `Sheet` with specified name, or get the `Sheet` with specified name if the name is exists.
     * Then set this `Sheet` to `PainterContext.lastSheet`.
     *
     * @param name sheet name
     * @return `Sheet` with specified name
     */
    public static Sheet sheet(String name) {
        Sheet sheet = Workbooks.getOrCreateSheet(painterContext.getWorkbook(), name);
        assert Objects.nonNull(sheet);
        painterContext.detectSheet(sheet);
        return sheet;
    }

    /**
     * Get the `Sheet` with specified index if the index is exists.
     * Then set this `Sheet` to `PainterContext.lastSheet`.
     *
     * @param index sheet index
     * @return `Sheet` with specified index
     */
    public static Sheet sheet(Integer index) {
        Sheet sheet = painterContext.getWorkbook().getSheetAt(index);
        assert Objects.nonNull(sheet);
        painterContext.detectSheet(sheet);
        return sheet;
    }

    // data

    public static void data(Object data) {
        painterContext.setData(data);
    }

    public static void data(Object data, Sheet sheet) {
        painterContext.getDataMap().putIfAbsent(sheet.getSheetName(), data);
    }

    // action

    public static void postPaint() {
        painterContext.postPaint();
    }

    public static void clear() {
        painterContext.clear();
    }

    // property

    public static EmptyDataSupplier none() {
        return EmptyDataSupplier.empty;
    }

    public static ConstDataSupplier<String> str(String s) {
        return new ConstDataSupplier<>(String.class, s);
    }

    public static ConstDataSupplier<Number> num(Double num) {
        return new ConstDataSupplier<>(Number.class, num);
    }

    public static ConstDataSupplier<Boolean> bool(Boolean b) {
        return new ConstDataSupplier<>(Boolean.class, b);
    }

    public static ConstDataSupplier<Date> date(String date, String formula) {
        return new ConstDataSupplier<>(Date.class, Date.from(LocalDateTime.parse(date, DateTimeFormatter.ofPattern(formula)).toInstant(ZoneOffset.of(CellValueSetters.zoneOffsetId()))));
    }

    public static LazyDataSupplier<String> strLazy(Supplier<? extends String> s) {
        return new LazyDataSupplier<>(String.class, s);
    }

    public static LazyDataSupplier<Number> numLazy(Supplier<? extends Number> s) {
        return new LazyDataSupplier<>(Number.class, s);
    }

    public static LazyDataSupplier<Boolean> boolLazy(Supplier<? extends Boolean> s) {
        return new LazyDataSupplier<>(Boolean.class, s);
    }

    public static LazyDataSupplier<Date> currentDate() {
        return new LazyDataSupplier<>(Date.class, Date::new);
    }

    public static LazyDataSupplier<Number> numAutoInc(Integer from, Integer step) {
        String id = UUID.randomUUID().toString();
        painterContext.getTempData().put(id, from);
        return new LazyDataSupplier<>(Number.class, () -> {
            Integer i = (Integer) painterContext.getTempData().get(id);
            painterContext.getTempData().replace(id, i + step);
            return i;
        });
    }

    public static LazyDataSupplier<Number> numAutoInc() {
        return numAutoInc(1, 1);
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

    @SuppressWarnings("unchecked")
    public static <T> SimplePropertyDataSupplier<T> property(String... properties) {
        return (SimplePropertyDataSupplier<T>) new SimplePropertyDataSupplier<>(Object.class, properties);
    }

    public static <T> CustomDataSupplier<T> property(Function<Object, T> operation) {
        return new CustomDataSupplier<T>(null, operation);
    }

    public static <T> CustomDataSupplier<T> property(Class<T> clazz, Function<Object, T> operation) {
        return new CustomDataSupplier<T>(clazz, operation);
    }

    @SuppressWarnings("unchecked")
    public static <T> SimpleCollectionPropertyDataSupplier<T> collectionProperty(String... properties) {
        return (SimpleCollectionPropertyDataSupplier<T>) new SimpleCollectionPropertyDataSupplier<>(Object.class, properties);
    }

    public static <T> SimpleCollectionPropertyDataSupplier<T> collectionProperty(Class<T> clazz, String... properties) {
        return new SimpleCollectionPropertyDataSupplier<>(clazz, properties);
    }

    public static <T> CustomCollectionDataSupplier<T> collectionProperty(Function<Object, Collection<T>> operation) {
        return new CustomCollectionDataSupplier<T>(null, operation);
    }

    public static <T> CustomCollectionDataSupplier<T> collectionProperty(Class<T> clazz, Function<Object, Collection<T>> operation) {
        return new CustomCollectionDataSupplier<T>(clazz, operation);
    }

    public static <T> CustomDataSupplier<T> self() {
        return CustomDataSupplier.self();
    }

    // Todo: formula

    // style

    public static Font font(String id, Consumer<? super Font> consumer) {
        return new FontStamp(id, consumer).genData(painterContext);
    }

    public static Font font(String id) {
        return FontStamp.stamp(id).genData(painterContext);
    }

    public static CellStyle style(String id, Consumer<? super CellStyle> consumer) {
        return new CellStyleStamp(id, consumer).genData(painterContext);
    }

    public static CellStyle style(String id, Font font, Consumer<? super CellStyle> consumer) {
        return new CellStyleStamp(id, cellStyle -> {
            cellStyle.setFont(font);
            consumer.accept(cellStyle);
        }).genData(painterContext);
    }

    public static CellStyle style(String id) {
        return CellStyleStamp.stamp(id).genData(painterContext);
    }

    // size

    public static void rowHeight(Number heightInPoints) {
        painterContext.getLastSheet().setDefaultRowHeightInPoints(heightInPoints.floatValue());
    }

    public static void rowHeight(Integer rowIndex, Number heightInPoints) {
        Row row = Sheets.getOrCreateRow(painterContext.getLastSheet(), rowIndex);
        row.setHeightInPoints(heightInPoints.floatValue());
    }

    public static void rowHeight(String ref, Number heightInPoints) {
        Row row = Sheets.getOrCreateRow(painterContext.getLastSheet(), Cells.convertRowStringToIndex(ref));
        row.setHeightInPoints(heightInPoints.floatValue());
    }

    public static void colWidth(Integer width) {
        painterContext.getLastSheet().setDefaultColumnWidth(width);
    }

    public static void colWidth(Integer colIndex, Integer width) {
        painterContext.getLastSheet().setColumnWidth(colIndex, width);
    }

    public static void colWidth(String ref, Integer width) {
        painterContext.getLastSheet().setColumnWidth(Cells.convertColStringToIndex(ref), width);
    }

    // free

    public static void contextOp(Consumer<? super PainterContext> op) {
        op.accept(painterContext);
    }

    public static void workbookOp(Consumer<? super Workbook> op) {
        op.accept(painterContext.getWorkbook());
    }

    public static void sheetOp(Consumer<? super Sheet> op) {
        op.accept(painterContext.getLastSheet());
    }

    public static void sheetOp(Sheet sheet, Consumer<? super Sheet> op) {
        op.accept(sheet);
    }

    public static <T> T stamp(String id, T data) {
        painterContext.getTempData().put(id, data);
        return data;
    }

    @SuppressWarnings("unchecked")
    public static <T> T stamp(String id) {
        return (T) painterContext.getTempData().get(id);
    }

    public static <T> T lazyStamp(String id, Supplier<? extends T> data) {
        painterContext.getTempData().put(id, data);
        return data.get();
    }

    @SuppressWarnings("unchecked")
    public static <T> T lazyStamp(String id) {
        return ((Supplier<? extends T>) painterContext.getTempData().get(id)).get();
    }

    // struct

    // Absolute cell

    /**
     * Create an Absolute cell.
     *
     * @param id           cell id
     * @param absRowIndex  abstract row index
     * @param absColIndex  abstract col index
     * @param width        cell width
     * @param height       col height
     * @param cellStyle    cell style
     * @param dataSupplier dataSupplier
     * @param <T>          data type
     * @return cell id
     */
    public static <T> String cell(String id, Integer absRowIndex, Integer absColIndex, Integer width, Integer height, CellStyle cellStyle, DataSupplier<Object, T> dataSupplier) {
        CellDataPainter<T> cellDataPainter = new CellDataPainter<>(id, absRowIndex, absColIndex, width, height, cellStyle, dataSupplier);
        cellDataPainter.init(painterContext);
        return id;
    }

    /**
     * Create an Absolute cell with cellStyle null.
     *
     * @param id           cell id
     * @param absRowIndex  abstract row index
     * @param absColIndex  abstract col index
     * @param width        cell width
     * @param height       col height
     * @param dataSupplier dataSupplier
     * @param <T>          data type
     * @return cell id
     */
    public static <T> String cell(String id, Integer absRowIndex, Integer absColIndex, Integer width, Integer height, DataSupplier<Object, T> dataSupplier) {
        return cell(id, absRowIndex, absColIndex, width, height, null, dataSupplier);
    }

    /**
     * Create an Absolute cell with width and height 1.
     *
     * @param id           cell id
     * @param absRowIndex  abstract row index
     * @param absColIndex  abstract col index
     * @param cellStyle    cell style
     * @param dataSupplier dataSupplier
     * @param <T>          data type
     * @return cell id
     */
    public static <T> String cell(String id, Integer absRowIndex, Integer absColIndex, CellStyle cellStyle, DataSupplier<Object, T> dataSupplier) {
        return cell(id, absRowIndex, absColIndex, 1, 1, cellStyle, dataSupplier);
    }

    /**
     * Create an Absolute cell with width and height 1, cellStyle null.
     *
     * @param id           cell id
     * @param absRowIndex  abstract row index
     * @param absColIndex  abstract col index
     * @param dataSupplier dataSupplier
     * @param <T>          data type
     * @return cell id
     */
    public static <T> String cell(String id, Integer absRowIndex, Integer absColIndex, DataSupplier<Object, T> dataSupplier) {
        return cell(id, absRowIndex, absColIndex, 1, 1, null, dataSupplier);
    }

    // Relative cell

    /**
     * Create a Relative cell.
     *
     * @param id               cell id
     * @param regionId         region id
     * @param relativeRowIndex relative row index
     * @param relativeColIndex relative col index
     * @param width            cell width
     * @param height           col height
     * @param cellStyle        cell style
     * @param dataSupplier     dataSupplier
     * @param <T>              data type
     * @return cell id
     */
    public static <T> String cell(String id, String regionId, Integer relativeRowIndex, Integer relativeColIndex, Integer width, Integer height, CellStyle cellStyle, DataSupplier<Object, T> dataSupplier) {
        RelativeCellDataPainter<T> cellDataPainter = new RelativeCellDataPainter<>(id, regionId, relativeRowIndex, relativeColIndex, width, height, cellStyle, dataSupplier);
        cellDataPainter.init(painterContext);
        return id;
    }

    /**
     * Create a Relative cell with cellStyle null.
     *
     * @param id               cell id
     * @param regionId         region id
     * @param relativeRowIndex relative row index
     * @param relativeColIndex relative col index
     * @param width            cell width
     * @param height           col height
     * @param dataSupplier     dataSupplier
     * @param <T>              data type
     * @return cell id
     */
    public static <T> String cell(String id, String regionId, Integer relativeRowIndex, Integer relativeColIndex, Integer width, Integer height, DataSupplier<Object, T> dataSupplier) {
        return cell(id, regionId, relativeRowIndex, relativeColIndex, width, height, null, dataSupplier);
    }

    /**
     * Create a Relative cell with width and height 1.
     *
     * @param id               cell id
     * @param regionId         region id
     * @param relativeRowIndex relative row index
     * @param relativeColIndex relative col index
     * @param cellStyle        cell style
     * @param dataSupplier     dataSupplier
     * @param <T>              data type
     * @return cell id
     */
    public static <T> String cell(String id, String regionId, Integer relativeRowIndex, Integer relativeColIndex, CellStyle cellStyle, DataSupplier<Object, T> dataSupplier) {
        return cell(id, regionId, relativeRowIndex, relativeColIndex, 1, 1, cellStyle, dataSupplier);
    }

    /**
     * Create a Relative cell with width and height 1, cellStyle null.
     *
     * @param id               cell id
     * @param regionId         region id
     * @param relativeRowIndex relative row index
     * @param relativeColIndex relative col index
     * @param dataSupplier     dataSupplier
     * @param <T>              data type
     * @return cell id
     */
    public static <T> String cell(String id, String regionId, Integer relativeRowIndex, Integer relativeColIndex, DataSupplier<Object, T> dataSupplier) {
        return cell(id, regionId, relativeRowIndex, relativeColIndex, 1, 1, null, dataSupplier);
    }

    // top region

    /**
     * Add a top region.
     *
     * @param id           region id
     * @param rowIndex     row index
     * @param colIndex     col index
     * @param cellStyle    cell style
     * @param dataSupplier dataSupplier
     * @param startNewLine      startNewLine flag
     * @param endNewline      endNewline flag
     * @param <T>          data type
     * @return region id
     */
    public static <T> String region(String id, Integer rowIndex, Integer colIndex, Boolean startNewLine, Boolean endNewline, CellStyle cellStyle, DataSupplier<Object, T> dataSupplier) {
        RegionDataPainter<T> regionDataPainter = new RegionDataPainter<>(id, null, rowIndex, colIndex, startNewLine, endNewline, cellStyle, dataSupplier);
        regionDataPainter.init(painterContext);
        return id;
    }

    /**
     * Add a top region with startNewLine and endNewline true.
     *
     * @param id           region id
     * @param rowIndex     row index
     * @param colIndex     col index
     * @param cellStyle    cell style
     * @param dataSupplier dataSupplier
     * @param <T>          data type
     * @return region id
     */
    public static <T> String region(String id, Integer rowIndex, Integer colIndex, CellStyle cellStyle, DataSupplier<Object, T> dataSupplier) {
        return region(id, rowIndex, colIndex, true, true, cellStyle, dataSupplier);
    }

    /**
     * Add a top region with startNewLine and endNewline true, cellStyle null.
     *
     * @param id           region id
     * @param rowIndex     row index
     * @param colIndex     col index
     * @param dataSupplier dataSupplier
     * @param <T>          data type
     * @return region id
     */
    public static <T> String region(String id, Integer rowIndex, Integer colIndex, DataSupplier<Object, T> dataSupplier) {
        return region(id, rowIndex, colIndex, true, true, null, dataSupplier);
    }

    /**
     * Add a top region with startNewLine and endNewline true, cellStyle null, rowIndex and colIndex null.
     *
     * @param id           region id
     * @param dataSupplier dataSupplier
     * @param <T>          data type
     * @return region id
     */
    public static <T> String region(String id, DataSupplier<Object, T> dataSupplier) {
        return region(id, null, null, true, true, null, dataSupplier);
    }

    // sub region

    /**
     * Add a sub region.
     *
     * @param id           region id
     * @param pid          parent region id
     * @param rowIndex     row index
     * @param colIndex     col index
     * @param startNewLine      startNewLine flag
     * @param endNewline      endNewline flag
     * @param cellStyle    cell style
     * @param dataSupplier dataSupplier
     * @param <T>          data type
     * @return region id
     */
    public static <T> String region(String id, String pid, Integer rowIndex, Integer colIndex, Boolean startNewLine, Boolean endNewline, CellStyle cellStyle, DataSupplier<Object, T> dataSupplier) {
        RegionDataPainter<T> regionDataPainter = new RegionDataPainter<>(id, pid, rowIndex, colIndex, startNewLine, endNewline, cellStyle, dataSupplier);
        regionDataPainter.init(painterContext);
        return id;
    }

    /**
     * Add a sub region with startNewLine and endNewline true.
     *
     * @param id           region id
     * @param pid          parent region id
     * @param rowIndex     row index
     * @param colIndex     col index
     * @param cellStyle    cell style
     * @param dataSupplier dataSupplier
     * @param <T>          data type
     * @return region id
     */
    public static <T> String region(String id, String pid, Integer rowIndex, Integer colIndex, CellStyle cellStyle, DataSupplier<Object, T> dataSupplier) {
        return region(id, pid, rowIndex, colIndex, true, true, cellStyle, dataSupplier);
    }

    /**
     * Add a sub region with startNewLine and endNewline true, cellStyle null.
     *
     * @param id           region id
     * @param pid          parent region id
     * @param rowIndex     row index
     * @param colIndex     col index
     * @param dataSupplier dataSupplier
     * @param <T>          data type
     * @return region id
     */
    public static <T> String region(String id, String pid, Integer rowIndex, Integer colIndex, DataSupplier<Object, T> dataSupplier) {
        return region(id, pid, rowIndex, colIndex, true, true, null, dataSupplier);
    }

    /**
     * Add a sub region with startNewLine and endNewline true.
     *
     * @param id           region id
     * @param pid          parent region id
     * @param dataSupplier dataSupplier
     * @param <T>          data type
     * @return region id
     */
    public static <T> String region(String id, String pid, DataSupplier<Object, T> dataSupplier) {
        return region(id, pid, null, null, true, true, null, dataSupplier);
    }

    // repeat region

    /**
     * Add a repeat region.
     *
     * @param id           region id
     * @param rowIndex     row index
     * @param colIndex     col index
     * @param cellStyle    cell style
     * @param dataSupplier dataSupplier
     * @param startNewLine      startNewLine flag
     * @param endNewline      endNewline flag
     * @param <T>          data type
     * @return region id
     */
    public static <T> String repeat(String id, Integer rowIndex, Integer colIndex, Boolean startNewLine, Boolean endNewline, CellStyle cellStyle, DataSupplier<Object, Collection<T>> dataSupplier) {
        RepeatRegionDataPainter<T> repeatRegionDataPainter = new RepeatRegionDataPainter<>(id, null, rowIndex, colIndex, startNewLine, endNewline, cellStyle, dataSupplier);
        repeatRegionDataPainter.init(painterContext);
        return id;
    }

    /**
     * Add a repeat region with startNewLine and endNewline true.
     *
     * @param id           region id
     * @param rowIndex     row index
     * @param colIndex     col index
     * @param cellStyle    cell style
     * @param dataSupplier dataSupplier
     * @param <T>          data type
     * @return region id
     */
    public static <T> String repeat(String id, Integer rowIndex, Integer colIndex, CellStyle cellStyle, DataSupplier<Object, Collection<T>> dataSupplier) {
        return repeat(id, rowIndex, colIndex, true, true, cellStyle, dataSupplier);
    }

    /**
     * Add a repeat region with startNewLine and endNewline true, cellStyle null.
     *
     * @param id           region id
     * @param rowIndex     row index
     * @param colIndex     col index
     * @param dataSupplier dataSupplier
     * @param <T>          data type
     * @return region id
     */
    public static <T> String repeat(String id, Integer rowIndex, Integer colIndex, DataSupplier<Object, Collection<T>> dataSupplier) {
        return repeat(id, rowIndex, colIndex, true, true, null, dataSupplier);
    }

    /**
     * Add a repeat region with startNewLine and endNewline true, cellStyle null, rowIndex and colIndex null.
     *
     * @param id           region id
     * @param dataSupplier dataSupplier
     * @param <T>          data type
     * @return region id
     */
    public static <T> String repeat(String id, DataSupplier<Object, Collection<T>> dataSupplier) {
        return repeat(id, null, null, true, true, null, dataSupplier);
    }

}
