package cc.domovoi.poi.ooxml.template.datapainter;

import cc.domovoi.poi.ooxml.template.DataPainter;
import cc.domovoi.poi.ooxml.template.DataSupplier;
import cc.domovoi.poi.ooxml.template.PainterContext;
import cc.domovoi.poi.ooxml.utils.NullUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class RegionDataPainter<T> implements DataPainter<T> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    // same as path
    private String id;

    private String pid;

    private Integer rowIndex;

    private Integer colIndex;

    private CellStyle cellStyle;

    private DataSupplier<Object, T> supplier;

    private List<DataPainter> children = new ArrayList<>();

    private Boolean endNewline;

    private Boolean startNewLine;

    private Integer rowStackIndex;

    private Integer colStackIndex;

    private Integer rowOffset;

    private Integer colOffset;

    public RegionDataPainter(String id, String pid, Integer rowIndex, Integer colIndex, Boolean startNewLine, Boolean endNewline, CellStyle cellStyle, DataSupplier<Object, T> supplier) {
        this.id = id;
        this.pid = pid;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.cellStyle = cellStyle;
        this.endNewline = endNewline;
        this.startNewLine = startNewLine;
        this.supplier = supplier;
    }

    public RegionDataPainter(String pid, Integer rowIndex, Integer colIndex, Boolean startNewLine, Boolean endNewline, CellStyle cellStyle, DataSupplier<Object, T> supplier) {
        this.id = UUID.randomUUID().toString();
        this.pid = pid;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.cellStyle = cellStyle;
        this.endNewline = endNewline;
        this.startNewLine = startNewLine;
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

    public Boolean childrenAllInstanceofRegionDataPainter() {
        return !children.isEmpty() && children.stream().allMatch(dataPainter -> dataPainter instanceof RegionDataPainter);
    }

    public Boolean childrenAllInstanceofRelativeCellDataPainter() {
        return !children.isEmpty() && children.stream().allMatch(dataPainter -> dataPainter instanceof RelativeCellDataPainter);
    }

    @Override
    public void init(PainterContext painterContext) {
        painterContext.attachDataPainter(id, this);
        if (pid != null) {
            RegionDataPainter<?> regionDataPainter = ((RegionDataPainter<?>) painterContext.getDataPainterMap().get(pid));
//            this.endNewline = !regionDataPainter.endNewline;
            regionDataPainter.addChild(this);
        }
    }

    protected void configContextIndex(PainterContext painterContext) {
        if (root()) {
            if (Objects.nonNull(this.rowIndex)) {
                painterContext.setLastRegionRowIndex(this.rowIndex - 1);
                logger.debug(String.format("type1 setLastRegionRowIndex(%s)", this.rowIndex - 1));
            }
            else if (Objects.nonNull(painterContext.getLastRowIndex())) {
                painterContext.setLastRegionRowIndex(painterContext.getLastRowIndex());
                logger.debug(String.format("type2 setLastRegionRowIndex(%s)", painterContext.getLastRowIndex()));
            }
            else {
                painterContext.setLastRegionRowIndex(-1);
                logger.debug(String.format("type3 setLastRegionRowIndex(%s)", -1));
            }

            if (Objects.nonNull(this.colIndex)) {
                painterContext.setLastRegionColIndex(this.colIndex - 1);
                logger.debug(String.format("type1 setLastRegionRowIndex(%s)", this.colIndex - 1));
            }
            else if (Objects.nonNull(painterContext.getLastColIndex()) && !startNewLine) {
                painterContext.setLastRegionColIndex(painterContext.getLastColIndex());
                logger.debug(String.format("type2 setLastRegionColIndex(%s)", painterContext.getLastColIndex()));
            }
            else {
                painterContext.setLastRegionColIndex(-1);
                logger.debug(String.format("type3 setLastRegionColIndex(%s)", -1));
            }
            this.rowStackIndex = painterContext.getLastRegionRowIndex();
            this.colStackIndex = painterContext.getLastRegionColIndex();
            logger.debug(String.format("this.rowStackIndex(%s)", this.rowStackIndex));
            logger.debug(String.format("this.colStackIndex(%s)", this.colStackIndex));
        }
        else {
            this.rowStackIndex = painterContext.getLastRegionRowIndex();
            this.colStackIndex = painterContext.getLastRegionColIndex();
            painterContext.setLastRegionRowIndex(this.rowStackIndex + NullUtils.defaultInteger(this.rowIndex));
            painterContext.setLastRegionColIndex(this.colStackIndex + NullUtils.defaultInteger(this.colIndex));
            logger.debug(String.format("this.rowStackIndex(%s)", this.rowStackIndex));
            logger.debug(String.format("this.colStackIndex(%s)", this.colStackIndex));
            logger.debug(String.format("setLastRegionRowIndex(%s)", this.rowStackIndex + NullUtils.defaultInteger(this.rowIndex)));
            logger.debug(String.format("setLastRegionColIndex(%s)", this.colStackIndex + NullUtils.defaultInteger(this.colIndex)));
        }
    }

    protected void configCellStyle() {
        children.forEach(child -> {
            if (Objects.isNull(child.getCellStyle())) {
                child.setCellStyle(this.getCellStyle());
            }
        });
    }

    @Override
    public void beforePaint(PainterContext painterContext) {
//        if (Objects.nonNull(this.rowIndex)) {
//            painterContext.setLastRegionRowIndex(this.rowIndex - 1);
//        }
//        if (Objects.nonNull(this.colIndex)) {
//            painterContext.setLastRegionColIndex(this.colIndex - 1);
//        }
//        if (!children.isEmpty() && children.stream().allMatch(dataPainter -> dataPainter instanceof RegionDataPainter)) {
//            ((RegionDataPainter<?>)children.get(children.size() - 1)).setEndNewline(true);
//            painterContext.attachDataGetter(dataPainter -> this.id.equals(dataPainter.getPid()), () -> supplier.apply(painterContext.genData(this)));
//        }
//        else if (children.stream().allMatch(dataPainter -> dataPainter instanceof RelativeCellDataPainter)) {
//            painterContext.attachDataGetter(dataPainter -> this.id.equals(dataPainter.getPid()), () -> supplier.apply(painterContext.genData(this)));
////            painterContext.attachDataGetter(dataPainter -> this.id.equals(dataPainter.getPid()), () -> {
////                Object data = painterContext.genData(this);
////                logger.debug("data: " + data);
////                return supplier.apply(data);
////            });
//        }
//        else {
//            throw new RuntimeException("children is not all RegionDataPainter or all RelativeCellDataPainter");
//        }

        configCellStyle();
        configContextIndex(painterContext);
        setRowOffset(null);
        setColOffset(null);
        painterContext.attachDataGetter(dataPainter -> this.id.equals(dataPainter.getPid()), () -> supplier.apply(painterContext.genData(this)));

    }

    @Override
    public void paint(PainterContext painterContext) {
        children.forEach(child -> child.postPaint(painterContext));
    }

    @Override
    public void afterPaint(PainterContext painterContext) {
//        if (children.stream().allMatch(dataPainter -> dataPainter instanceof RelativeCellDataPainter)) {
//            if (endNewline) {
//                Integer lastRowIndex = children.stream().map(dataPainter -> (RelativeCellDataPainter) dataPainter).max(Comparator.comparingInt(RelativeCellDataPainter::getRowOffset)).map(RelativeCellDataPainter::getRowOffset).orElse(null);
//                if (Objects.isNull(lastRowIndex) && Objects.nonNull(rowIndex)) {
//                    lastRowIndex = rowIndex;
//                }
//                assert Objects.nonNull(lastRowIndex);
//                painterContext.setLastRegionRowIndex(lastRowIndex + painterContext.getLastRegionRowIndex() + 1);
////                painterContext.setLastRow(Sheets.getOrCreateRow(painterContext.getLastSheet(), lastRowIndex));
//
//                painterContext.setLastRegionColIndex(-1);
//            }
//            else {
//                Integer lastColIndex = children.stream().map(dataPainter -> (RelativeCellDataPainter) dataPainter).max(Comparator.comparingInt(RelativeCellDataPainter::getColOffset)).map(RelativeCellDataPainter::getColOffset).orElse(null);
//                if (Objects.isNull(lastColIndex) && Objects.nonNull(colIndex)) {
//                    lastColIndex = colIndex;
//                }
//                if (Objects.nonNull(lastColIndex)) {
//                    painterContext.setLastRegionColIndex(lastColIndex + painterContext.getLastRegionColIndex() + 1);
//                }
//            }
//        }

        if (root()) {
            if (endNewline) {
                painterContext.setLastRegionRowIndex(rowStackIndex + rowOffset);
                painterContext.setLastRegionColIndex(-1);
            }
            else {
                painterContext.setLastRegionRowIndex(rowStackIndex + rowOffset);
                painterContext.setLastRegionColIndex(colStackIndex + colOffset);
            }
        }
        else {
            RegionDataPainter<?> regionDataPainter = (RegionDataPainter<?>) painterContext.getDataPainterMap().get(getPid());
            if (getPid().endsWith(":self")) {
                if (endNewline) {
                    logger.debug(String.format("p setRowOffset(%s)", NullUtils.defaultInteger(regionDataPainter.getRowOffset()) + this.getRowOffset()));
                    regionDataPainter.setRowOffset(NullUtils.defaultInteger(regionDataPainter.getRowOffset()) + this.getRowOffset());
                    if (NullUtils.defaultInteger(regionDataPainter.getColOffset()) < NullUtils.defaultInteger(this.getColOffset())) {
                        logger.debug(String.format("p setColOffset(%s)", this.getColOffset()));
                        regionDataPainter.setColOffset(this.getColOffset());
                    }
                }
                else {
                    logger.debug(String.format("p setColOffset(%s)", NullUtils.defaultInteger(regionDataPainter.getColOffset()) + this.getColOffset()));
                    regionDataPainter.setColOffset(NullUtils.defaultInteger(regionDataPainter.getColOffset()) + this.getColOffset());
                    if (NullUtils.defaultInteger(regionDataPainter.getRowOffset()) < NullUtils.defaultInteger(this.getRowOffset())) {
                        logger.debug(String.format("p setRowOffset(%s)", this.getRowOffset()));
                        regionDataPainter.setRowOffset(this.getRowOffset());
                    }
                }

            }
            else {
                if (NullUtils.defaultInteger(regionDataPainter.getRowOffset()) < NullUtils.defaultInteger(this.getRowOffset())) {
                    logger.debug(String.format("p setRowOffset(%s)", this.getRowOffset()));
                    regionDataPainter.setRowOffset(this.getRowOffset());
                }
                if (NullUtils.defaultInteger(regionDataPainter.getColOffset()) < NullUtils.defaultInteger(this.getColOffset())) {
                    logger.debug(String.format("p setColOffset(%s)", this.getColOffset()));
                    regionDataPainter.setColOffset(this.getColOffset());
                }
            }

            painterContext.setLastRegionRowIndex(rowStackIndex);
            painterContext.setLastRegionColIndex(colStackIndex);
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

    public Boolean getEndNewline() {
        return endNewline;
    }

    public void setEndNewline(Boolean endNewline) {
        this.endNewline = endNewline;
    }

    public Boolean getStartNewLine() {
        return startNewLine;
    }

    public void setStartNewLine(Boolean startNewLine) {
        this.startNewLine = startNewLine;
    }

    public Integer getRowStackIndex() {
        return rowStackIndex;
    }

    public void setRowStackIndex(Integer rowStackIndex) {
        this.rowStackIndex = rowStackIndex;
    }

    public Integer getColStackIndex() {
        return colStackIndex;
    }

    public void setColStackIndex(Integer colStackIndex) {
        this.colStackIndex = colStackIndex;
    }

    @Override
    public Integer getRowOffset() {
        return rowOffset;
    }

    @Override
    public void setRowOffset(Integer rowOffset) {
        this.rowOffset = rowOffset;
    }

    @Override
    public Integer getColOffset() {
        return colOffset;
    }

    @Override
    public void setColOffset(Integer colOffset) {
        this.colOffset = colOffset;
    }
}
