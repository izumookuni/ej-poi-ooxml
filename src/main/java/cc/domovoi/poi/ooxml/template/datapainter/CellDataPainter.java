package cc.domovoi.poi.ooxml.template.datapainter;

import cc.domovoi.poi.ooxml.CellStyles;
import cc.domovoi.poi.ooxml.Cells;
import cc.domovoi.poi.ooxml.Rows;
import cc.domovoi.poi.ooxml.Sheets;
import cc.domovoi.poi.ooxml.template.DataPainter;
import cc.domovoi.poi.ooxml.template.DataSupplier;
import cc.domovoi.poi.ooxml.template.PainterContext;
import cc.domovoi.poi.ooxml.template.cellvalue.CellValueSetter;
import cc.domovoi.poi.ooxml.template.cellvalue.CellValueSetters;
import cc.domovoi.poi.ooxml.template.datasupplier.EmptyDataSupplier;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class CellDataPainter<T> implements DataPainter<T> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

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

    @Override
    public String toString() {
        return "CellDataPainter{" +
                "id='" + id + '\'' +
                '}';
    }

    @SuppressWarnings("unchecked")
    public Class<T> dataClass() {
//        return (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return supplier.dataType();
    }

    @Override
    public String getPid() {
        return null;
    }

    public Boolean singleton() {
        return width == 1 && height == 1;
    }

    public Integer getRowOffset() {
        return (Objects.nonNull(rowIndex) ? rowIndex : 0) + (Objects.nonNull(this.height) ? this.height : 0) - 1;
    }

    public Integer getColOffset() {
        return (Objects.nonNull(colIndex) ? colIndex : 0) + (Objects.nonNull(this.width) ? this.width : 0) - 1;
    }

    @Override
    public void setRowOffset(Integer rowOffset) {
//        this.height = rowOffset + 1;
    }

    @Override
    public void setColOffset(Integer colOffset) {
//        this.width = colOffset + 1;
    }

    protected Cell detectCell(PainterContext painterContext, Integer rowIndex, Integer colIndex) {
        final Cell cell;
        if (singleton()) {
            cell = Sheets.getOrCreateRow(rowIndex).andThen(Rows.getOrCreateCell(colIndex)).apply(painterContext.getLastSheet());
        }
        else {
            cell = Cells.addMergedRegionAsCell(painterContext.getLastSheet(), rowIndex, rowIndex + this.height - 1, colIndex, colIndex + this.width - 1);
        }
        return cell;
    }

    protected void innerPaint(Cell cell, T data, PainterContext painterContext) {
        if (Objects.nonNull(this.cellStyle)) {
            cell.setCellStyle(this.cellStyle);
        }
        if (Objects.nonNull(data)) {
            final CellStyle innerCellStyle;
            if (data instanceof Date) {
                innerCellStyle = CellStyles.attachOrCreateDateCellStyle(painterContext.getWorkbook(), this.cellStyle, CellStyles.dateTimePattern);
            }
            else if (data instanceof LocalDateTime) {
                innerCellStyle = CellStyles.attachOrCreateDateCellStyle(painterContext.getWorkbook(), this.cellStyle, CellStyles.dateTimePattern);
            }
            else if (data instanceof LocalDate) {
                innerCellStyle = CellStyles.attachOrCreateDateCellStyle(painterContext.getWorkbook(), this.cellStyle, CellStyles.datePattern);
            }
            else if (data instanceof LocalTime) {
                innerCellStyle = CellStyles.attachOrCreateDateCellStyle(painterContext.getWorkbook(), this.cellStyle, CellStyles.timePattern);
            }
            else {
                innerCellStyle = this.cellStyle;
            }
            if (Objects.nonNull(innerCellStyle)) {
                cell.setCellStyle(innerCellStyle);
            }
            CellValueSetter<?> cellValueSetter = CellValueSetters.forClass(this.dataClass());
            cellValueSetter.setCellValue(data, cell);
        }
        else {
            cell.setCellValue(EmptyDataSupplier.empty.apply(null));
        }
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
        Cell cell = detectCell(painterContext, this.rowIndex, this.colIndex);
        logger.debug(String.format("detectCell(%s,%s)", this.rowIndex, this.colIndex));
        innerPaint(cell, data, painterContext);
        logger.debug(String.format("insertData: %s", data));
//        T data = supplier.apply(painterContext.genData(this));
//        painterContext.drawCell(this.id, this.cellStyle, data);
    }

    @Override
    public void afterPaint(PainterContext painterContext) {
        painterContext.setLastRowIndex(getRowOffset());
        painterContext.setLastColIndex(getColOffset());
        logger.debug(String.format("setLastRowIndex(%s)", getRowOffset()));
        logger.debug(String.format("setLastRowIndex(%s)", getColOffset()));
//        painterContext.setLastRow(Sheets.getOrCreateRow(painterContext.getLastSheet(), getRowOffset()));
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
