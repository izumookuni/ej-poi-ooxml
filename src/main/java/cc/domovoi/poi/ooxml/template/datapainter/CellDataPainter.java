package cc.domovoi.poi.ooxml.template.datapainter;

import cc.domovoi.poi.ooxml.template.DataPainter;
import cc.domovoi.poi.ooxml.template.DataSupplier;
import cc.domovoi.poi.ooxml.template.PainterContext;
import org.apache.poi.ss.usermodel.CellStyle;

import java.lang.reflect.ParameterizedType;
import java.util.UUID;

public class CellDataPainter<T> implements DataPainter {

    private String id;

    private String pid;

    private Integer rowIndex;

    private Integer colIndex;

    private Integer width;

    private Integer height;

    private CellStyle cellStyle;

    private DataSupplier<Object, T> supplier;

    public CellDataPainter(String id, String pid, Integer rowIndex, Integer colIndex, Integer width, Integer height, CellStyle cellStyle, DataSupplier<Object, T> supplier) {
        this.id = id;
        this.pid = pid;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.width = width;
        this.height = height;
        this.cellStyle = cellStyle;
        this.supplier = supplier;
    }

    public CellDataPainter(String pid, Integer rowIndex, Integer colIndex, Integer width, Integer height, CellStyle cellStyle, DataSupplier<Object, T> supplier) {
        this.id = UUID.randomUUID().toString();
        this.pid = pid;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.width = width;
        this.height = height;
        this.cellStyle = cellStyle;
        this.supplier = supplier;
    }

    public String getId() {
        return id;
    }

    public String getPid() {
        return pid;
    }

    public Integer getRowIndex() {
        return rowIndex;
    }

    public Integer getColIndex() {
        return colIndex;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public CellStyle getCellStyle() {
        return cellStyle;
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
    public void paint(PainterContext painterContext) {
        T data = supplier.apply(painterContext.genData(this.id));
        painterContext.drawCell(this.id, data);
    }
}
