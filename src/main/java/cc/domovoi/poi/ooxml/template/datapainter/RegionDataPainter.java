package cc.domovoi.poi.ooxml.template.datapainter;

import cc.domovoi.poi.ooxml.Sheets;
import cc.domovoi.poi.ooxml.template.DataPainter;
import cc.domovoi.poi.ooxml.template.DataSupplier;
import cc.domovoi.poi.ooxml.template.PainterContext;
import org.apache.poi.ss.usermodel.CellStyle;

import java.util.*;

public class RegionDataPainter<T> implements DataPainter {

    // same as path
    private String id;

    private String pid;

    private Integer rowIndex;

    private Integer colIndex;

    private CellStyle cellStyle;

    private DataSupplier<Object, T> supplier;

    private List<DataPainter> children = new ArrayList<>();

    public RegionDataPainter(String id, String pid, Integer rowIndex, Integer colIndex, CellStyle cellStyle, DataSupplier<Object, T> supplier) {
        this.id = id;
        this.pid = pid;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.cellStyle = cellStyle;
        this.supplier = supplier;
    }

    public RegionDataPainter(String pid, Integer rowIndex, Integer colIndex, CellStyle cellStyle, DataSupplier<Object, T> supplier) {
        this.id = UUID.randomUUID().toString();
        this.pid = pid;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.cellStyle = cellStyle;
        this.supplier = supplier;
    }

    public void addChild(DataPainter dataPainter) {
        children.add(dataPainter);
    }

    @Override
    public void init(PainterContext painterContext) {
        painterContext.attachDataPainter(id, this);
        if (pid != null) {
            ((RegionDataPainter<?>) painterContext.getDataPainterMap().get(pid)).addChild(this);
        }
    }

    @Override
    public void beforePaint(PainterContext painterContext) {
        assert children.isEmpty() || children.stream().allMatch(dataPainter -> dataPainter instanceof RegionDataPainter) || children.stream().allMatch(dataPainter -> dataPainter instanceof RelativeCellDataPainter);
        painterContext.attachDataGetter(dataPainter -> this.id.equals(dataPainter.getPid()), () -> supplier.apply(painterContext.genData(this)));
    }

    @Override
    public void paint(PainterContext painterContext) {
        children.forEach(child -> child.postPaint(painterContext));
    }

    @Override
    public void afterPaint(PainterContext painterContext) {
        if (children.stream().allMatch(dataPainter -> dataPainter instanceof RelativeCellDataPainter)) {
            Integer lastRowIndex = children.stream().map(dataPainter -> (RelativeCellDataPainter) dataPainter).max(Comparator.comparingInt(RelativeCellDataPainter::getMaxRelativeRowOffset)).map(RelativeCellDataPainter::getMaxRelativeRowOffset).orElse(null);
            Integer lastColIndex = children.stream().map(dataPainter -> (RelativeCellDataPainter) dataPainter).max(Comparator.comparingInt(RelativeCellDataPainter::getMaxRelativeColOffset)).map(RelativeCellDataPainter::getMaxRelativeColOffset).orElse(null);
            if (Objects.isNull(lastRowIndex) && Objects.nonNull(rowIndex)) {
                lastRowIndex = rowIndex;
            }
            if (Objects.isNull(lastColIndex) && Objects.nonNull(colIndex)) {
                lastColIndex = colIndex;
            }
            assert Objects.nonNull(lastRowIndex);
            painterContext.setLastRowIndex(lastRowIndex);
            if (Objects.nonNull(lastColIndex)) {
                painterContext.setLastColIndex(lastColIndex);
            }
            painterContext.setLastRow(Sheets.getOrCreateRow(painterContext.getLastSheet(), lastRowIndex));
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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

    public List<DataPainter> getChildren() {
        return children;
    }

    public void setChildren(List<DataPainter> children) {
        this.children = children;
    }
}
