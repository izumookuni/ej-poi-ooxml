package cc.domovoi.poi.ooxml.template;

import cc.domovoi.poi.ooxml.template.cellvalue.CellValueSetters;
import cc.domovoi.poi.ooxml.template.datapainter.CellDataPainter;
import cc.domovoi.poi.ooxml.template.datapainter.RegionDataPainter;
import cc.domovoi.poi.ooxml.template.datapainter.RelativeCellDataPainter;
import cc.domovoi.poi.ooxml.template.datasupplier.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Function;

public class PainterApplication {

    private static PainterContext painterContext = new PainterContext();

    public static void addData(Object data) {
        painterContext.setData(data);
    }

    public static EmptyDataSupplier none() {
        return EmptyDataSupplier.empty;
    }

    public static ConstDataSupplier<String> str(String s) {
        return new ConstDataSupplier<>(s);
    }

    public static ConstDataSupplier<Double> num(Double num) {
        return new ConstDataSupplier<>(num);
    }

    public static ConstDataSupplier<Boolean> bool(Boolean b) {
        return new ConstDataSupplier<>(b);
    }

    public static ConstDataSupplier<Date> date(String date, String formula) {
        return new ConstDataSupplier<>(Date.from(LocalDateTime.parse(date, DateTimeFormatter.ofPattern(formula)).toInstant(ZoneOffset.of(CellValueSetters.zoneOffsetId()))));
    }

    public static <T> SimplePropertyDataSupplier<T> property(String... properties) {
        return new SimplePropertyDataSupplier<>(properties);
    }

    public static <T> CustomDataSupplier<T> property(Function<Object, T> operation) {
        return new CustomDataSupplier<>(operation);
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
    public static <T> String region(String id, Integer rowIndex, Integer colIndex, CellStyle cellStyle, DataSupplier<Object, T> dataSupplier) {
        RegionDataPainter<T> regionDataPainter = new RegionDataPainter<>(id, null, rowIndex, colIndex, cellStyle, dataSupplier);
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
    public static <T> String region(String id, String pid, Integer rowIndex, Integer colIndex, CellStyle cellStyle, DataSupplier<Object, T> dataSupplier) {
        RegionDataPainter<T> regionDataPainter = new RegionDataPainter<>(id, pid, rowIndex, colIndex, cellStyle, dataSupplier);
        regionDataPainter.init(painterContext);
        // Todo: ...
        return id;
    }


}
