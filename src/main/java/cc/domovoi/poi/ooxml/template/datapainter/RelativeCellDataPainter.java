package cc.domovoi.poi.ooxml.template.datapainter;

import cc.domovoi.poi.ooxml.template.DataSupplier;
import cc.domovoi.poi.ooxml.template.PainterContext;
import cc.domovoi.poi.ooxml.utils.NullUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

import java.util.Objects;

public class RelativeCellDataPainter<T> extends CellDataPainter<T> {

    private String regionId;

    private Integer relativeRowIndex;

    private Integer relativeColIndex;

    public RelativeCellDataPainter(String id, String regionId, Integer relativeRowIndex, Integer relativeColIndex, Integer width, Integer height, CellStyle cellStyle, DataSupplier<Object, T> supplier) {
        super(id, null, null, width, height, cellStyle, supplier);
        this.regionId = regionId;
        this.relativeRowIndex = relativeRowIndex;
        this.relativeColIndex = relativeColIndex;
    }


    @Override
    public String toString() {
        return "RelativeCellDataPainter{" +
                "id='" + getId() + '\'' +
                "regionId='" + regionId + '\'' +
                '}';
    }

    @Override
    public Integer getRowOffset() {
        return (Objects.nonNull(relativeRowIndex) ? relativeRowIndex : 0) + (Objects.nonNull(this.getHeight()) ? this.getHeight() : 0);
    }

    public Integer getColOffset() {
        return (Objects.nonNull(relativeColIndex) ? relativeColIndex : 0) + (Objects.nonNull(this.getWidth()) ? this.getWidth() : 0);
    }

    @Override
    public String getPid() {
        return regionId;
    }

    @Override
    public void init(PainterContext painterContext) {
        super.init(painterContext);
        if (regionId != null) {
            ((RegionDataPainter<?>) painterContext.getDataPainterMap().get(regionId)).addChild(this);
        }
    }

    @Override
    public void beforePaint(PainterContext painterContext) {

    }

    @Override
    public void paint(PainterContext painterContext) {
        T data = getSupplier().apply(painterContext.genData(this));
        Integer rowIndex = painterContext.getLastRegionRowIndex();
        Integer colIndex = painterContext.getLastRegionColIndex();
        Cell cell = detectCell(painterContext, rowIndex + this.relativeRowIndex + 1, colIndex + this.relativeColIndex + 1);
        logger.debug(String.format("detectCell(%s,%s)", rowIndex + this.relativeRowIndex + 1, colIndex + this.relativeColIndex + 1));
        innerPaint(cell, data, painterContext);
        logger.debug(String.format("insertData: %s", data));
    }

    @Override
    public void afterPaint(PainterContext painterContext) {
        painterContext.setLastRowIndex(painterContext.getLastRegionRowIndex() + getRowOffset());
        painterContext.setLastColIndex(painterContext.getLastRegionColIndex() + getColOffset());

        RegionDataPainter<?> regionDataPainter = (RegionDataPainter<?>) painterContext.getDataPainterMap().get(getPid());
        if (NullUtils.defaultInteger(regionDataPainter.getRowOffset()) < NullUtils.defaultInteger(this.getRowOffset())) {
            regionDataPainter.setRowOffset(this.getRowOffset());
            logger.debug(String.format("%s setRowOffset(%s)", regionDataPainter.getId(), this.getRowOffset()));
        }
        if (NullUtils.defaultInteger(regionDataPainter.getRowOffset()) < NullUtils.defaultInteger(this.getColOffset())) {
            regionDataPainter.setColOffset(this.getColOffset());
            logger.debug(String.format("%s getColOffset(%s)", regionDataPainter.getId(), this.getColOffset()));
        }
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public Integer getRelativeRowIndex() {
        return relativeRowIndex;
    }

    public void setRelativeRowIndex(Integer relativeRowIndex) {
        this.relativeRowIndex = relativeRowIndex;
    }

    public Integer getRelativeColIndex() {
        return relativeColIndex;
    }

    public void setRelativeColIndex(Integer relativeColIndex) {
        this.relativeColIndex = relativeColIndex;
    }
}
