package cc.domovoi.poi.ooxml.template.datapainter;

import cc.domovoi.poi.ooxml.template.DataPainter;
import cc.domovoi.poi.ooxml.template.DataSupplier;
import cc.domovoi.poi.ooxml.template.PainterContext;
import org.apache.poi.ss.usermodel.CellStyle;

import java.lang.reflect.ParameterizedType;
import java.util.UUID;

public class CellDataPainter<T> implements DataPainter {

    private String id;

    private Integer rowIndex;

    private Integer colIndex;

    private Integer width;

    private Integer height;

    private CellStyle cellStyle;

    private DataSupplier<Object, T> supplier;

    public CellDataPainter(String id, Integer rowIndex, Integer colIndex, Integer width, Integer height, CellStyle cellStyle, DataSupplier<Object, T> supplier) {
        this.id = id;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.width = width;
        this.height = height;
        this.cellStyle = cellStyle;
        this.supplier = supplier;
    }

    public CellDataPainter(Integer rowIndex, Integer colIndex, Integer width, Integer height, CellStyle cellStyle, DataSupplier<Object, T> supplier) {
        this.id = UUID.randomUUID().toString();
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.width = width;
        this.height = height;
        this.cellStyle = cellStyle;
        this.supplier = supplier;
    }

    @SuppressWarnings("unchecked")
    public Class<T> dataClass() {
        return (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public void init(PainterContext painterContext) {
        painterContext.attachDataPainter(id, this);
    }

    @Override
    public void beforePaint(PainterContext painterContext) {

    }

    @Override
    public void paint(PainterContext painterContext) {
        T data = supplier.apply(painterContext.genData(this));
        painterContext.drawCell(this.id, data);
    }

    @Override
    public void afterPaint(PainterContext painterContext) {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    public Integer getColIndex() {
        return colIndex;
    }

    public void setColIndex(Integer colIndex) {
        this.colIndex = colIndex;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public CellStyle getCellStyle() {
        return cellStyle;
    }

    public void setCellStyle(CellStyle cellStyle) {
        this.cellStyle = cellStyle;
    }

    public DataSupplier<Object, T> getSupplier() {
        return supplier;
    }

    public void setSupplier(DataSupplier<Object, T> supplier) {
        this.supplier = supplier;
    }
}
