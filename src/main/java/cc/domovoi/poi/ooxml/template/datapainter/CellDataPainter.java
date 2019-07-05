package cc.domovoi.poi.ooxml.template.datapainter;

import cc.domovoi.poi.ooxml.Cells;
import cc.domovoi.poi.ooxml.Rows;
import cc.domovoi.poi.ooxml.Sheets;
import cc.domovoi.poi.ooxml.template.DataPainter;
import cc.domovoi.poi.ooxml.template.DataSupplier;
import cc.domovoi.poi.ooxml.template.PainterContext;
import cc.domovoi.poi.ooxml.template.cellvalue.CellValueSetters;
import cc.domovoi.poi.ooxml.template.datasupplier.EmptyDataSupplier;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;
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
    public String getPid() {
        return null;
    }

    public Boolean singleton() {
        return width == 1 && height == 1;
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
        T data = supplier.apply(painterContext.getData());
        final Cell cell;
        if (singleton()) {
            cell = Sheets.getOrCreateRow(this.rowIndex).andThen(Rows.getOrCreateCell(this.colIndex)).apply(painterContext.getLastSheet());
        }
        else {
            cell = Cells.addMergedRegionAsCell(painterContext.getLastSheet(), this.rowIndex, this.rowIndex + this.height - 1, this.colIndex, this.colIndex + this.width - 1);
        }
        if (Objects.nonNull(this.cellStyle)) {
            cell.setCellStyle(this.cellStyle);
        }
        if (Objects.nonNull(data)) {
            CellValueSetters.forClass(this.dataClass()).setCellValue(data, cell);
        }
        else {
            cell.setCellValue(EmptyDataSupplier.empty.apply(null));
        }
//        T data = supplier.apply(painterContext.genData(this));
//        painterContext.drawCell(this.id, this.cellStyle, data);
    }

    @Override
    public void afterPaint(PainterContext painterContext) {
        painterContext.setLastRow(Sheets.getOrCreateRow(painterContext.getLastSheet(), this.rowIndex + this.height - 1));
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
