package cc.domovoi.poi.ooxml.template.datapainter;

import cc.domovoi.poi.ooxml.template.DataPainter;
import cc.domovoi.poi.ooxml.template.DataSupplier;
import cc.domovoi.poi.ooxml.template.PainterContext;
import org.apache.poi.ss.usermodel.CellStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class RegionDataPainter<T> implements DataPainter<T> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // same as path
    private String id;

    private String pid;

    private Integer rowIndex;

    private Integer colIndex;

    private CellStyle cellStyle;

    private DataSupplier<Object, T> supplier;

    private List<DataPainter> children = new ArrayList<>();

    private Boolean newline;

    public RegionDataPainter(String id, String pid, Integer rowIndex, Integer colIndex, CellStyle cellStyle, Boolean newline, DataSupplier<Object, T> supplier) {
        this.id = id;
        this.pid = pid;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.cellStyle = cellStyle;
        this.newline = newline;
        this.supplier = supplier;
    }

    public RegionDataPainter(String pid, Integer rowIndex, Integer colIndex, CellStyle cellStyle, Boolean newline, DataSupplier<Object, T> supplier) {
        this.id = UUID.randomUUID().toString();
        this.pid = pid;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.cellStyle = cellStyle;
        this.newline = newline;
        this.supplier = supplier;
    }

    @Override
    public String toString() {
        return "RegionDataPainter{" +
                "id='" + id + '\'' +
                ", pid='" + pid + '\'' +
                '}';
    }

    public void addChild(DataPainter dataPainter) {
        children.add(dataPainter);
    }

    @Override
    public void init(PainterContext painterContext) {
        painterContext.attachDataPainter(id, this);
        if (pid != null) {
            RegionDataPainter<?> regionDataPainter = ((RegionDataPainter<?>) painterContext.getDataPainterMap().get(pid));
//            this.newline = !regionDataPainter.newline;
            regionDataPainter.addChild(this);
        }
    }

    @Override
    public void beforePaint(PainterContext painterContext) {
        if (Objects.nonNull(this.rowIndex)) {
            painterContext.setLastRegionRowIndex(this.rowIndex - 1);
        }
        if (Objects.nonNull(this.colIndex)) {
            painterContext.setLastRegionColIndex(this.colIndex - 1);
        }
        if (!children.isEmpty() && children.stream().allMatch(dataPainter -> dataPainter instanceof RegionDataPainter)) {
            ((RegionDataPainter<?>)children.get(children.size() - 1)).setNewline(true);
            painterContext.attachDataGetter(dataPainter -> this.id.equals(dataPainter.getPid()), () -> supplier.apply(painterContext.genData(this)));
        }
        else if (children.stream().allMatch(dataPainter -> dataPainter instanceof RelativeCellDataPainter)) {
//            painterContext.attachDataGetter(dataPainter -> this.id.equals(dataPainter.getPid()), () -> supplier.apply(painterContext.genData(this)));
            painterContext.attachDataGetter(dataPainter -> this.id.equals(dataPainter.getPid()), () -> {
                Object data = painterContext.genData(this);
                logger.debug("data: " + data);
                return supplier.apply(data);
            });
        }
        else {
            throw new RuntimeException("children is not all RegionDataPainter or all RelativeCellDataPainter");
        }

    }

    @Override
    public void paint(PainterContext painterContext) {
        children.forEach(child -> child.postPaint(painterContext));
    }

    @Override
    public void afterPaint(PainterContext painterContext) {
        if (children.stream().allMatch(dataPainter -> dataPainter instanceof RelativeCellDataPainter)) {
            if (newline) {
                Integer lastRowIndex = children.stream().map(dataPainter -> (RelativeCellDataPainter) dataPainter).max(Comparator.comparingInt(RelativeCellDataPainter::getMaxRelativeRowOffset)).map(RelativeCellDataPainter::getMaxRelativeRowOffset).orElse(null);
                if (Objects.isNull(lastRowIndex) && Objects.nonNull(rowIndex)) {
                    lastRowIndex = rowIndex;
                }
                assert Objects.nonNull(lastRowIndex);
                painterContext.setLastRegionRowIndex(lastRowIndex + painterContext.getLastRegionRowIndex() + 1);
//                painterContext.setLastRow(Sheets.getOrCreateRow(painterContext.getLastSheet(), lastRowIndex));

                painterContext.setLastRegionColIndex(-1);
            }
            else {
                Integer lastColIndex = children.stream().map(dataPainter -> (RelativeCellDataPainter) dataPainter).max(Comparator.comparingInt(RelativeCellDataPainter::getMaxRelativeColOffset)).map(RelativeCellDataPainter::getMaxRelativeColOffset).orElse(null);
                if (Objects.isNull(lastColIndex) && Objects.nonNull(colIndex)) {
                    lastColIndex = colIndex;
                }
                if (Objects.nonNull(lastColIndex)) {
                    painterContext.setLastRegionColIndex(lastColIndex + painterContext.getLastRegionColIndex() + 1);
                }
            }
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

    public Boolean getNewline() {
        return newline;
    }

    public void setNewline(Boolean newline) {
        this.newline = newline;
    }
}
