package cc.domovoi.poi.ooxml.template.datapainter;

import cc.domovoi.poi.ooxml.template.DataSupplier;
import cc.domovoi.poi.ooxml.template.PainterContext;
import org.apache.poi.ss.usermodel.CellStyle;

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

    }

    @Override
    public void afterPaint(PainterContext painterContext) {

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
