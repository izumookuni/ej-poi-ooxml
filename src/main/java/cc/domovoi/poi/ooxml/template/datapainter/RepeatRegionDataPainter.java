package cc.domovoi.poi.ooxml.template.datapainter;

import cc.domovoi.poi.ooxml.template.DataSupplier;
import cc.domovoi.poi.ooxml.template.PainterContext;
import cc.domovoi.poi.ooxml.template.datasupplier.CustomDataSupplier;
import org.apache.poi.ss.usermodel.CellStyle;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class RepeatRegionDataPainter<T> extends RegionDataPainter<Collection<T>> {

    private Iterator<T> dataIterator;

    private T innerData;

    private RegionDataPainter<T> innerDataPainter;

    private Integer dataSize;

    public RepeatRegionDataPainter(String id, String pid, Integer rowIndex, Integer colIndex, CellStyle cellStyle, DataSupplier<Object, Collection<T>> supplier) {
        super(id + ":self", pid, rowIndex, colIndex, cellStyle, true, supplier);
    }

    private String getInnerId() {
        return getId().substring(0, getId().lastIndexOf(":self"));
    }

    @Override
    public String toString() {
        return "RepeatRegionDataPainter{" +
                "id='" + getId() + '\'' +
                "pid='" + getPid() + '\'' +
                '}';
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init(PainterContext painterContext) {
        super.init(painterContext);
        innerDataPainter = new RegionDataPainter<>(getInnerId(), getId(), getRowIndex(), getColIndex(), getCellStyle(), true, CustomDataSupplier.self());
        painterContext.attachDataPainter(getInnerId(), innerDataPainter);
        this.setChildren(Collections.singletonList(innerDataPainter));
    }

    @Override
    public void beforePaint(PainterContext painterContext) {
        Collection<T> dataList = this.getSupplier().apply(painterContext.genData(this));
        dataSize = dataList.size();
        dataIterator = dataList.iterator();
        painterContext.attachDataGetter(dataPainter -> this.getId().equals(dataPainter.getPid()), () -> innerData);
    }

    @Override
    public void paint(PainterContext painterContext) {
        for (Integer i = 0; i < dataSize; i++) {
            if (dataIterator.hasNext()) {
                innerData = dataIterator.next();
            }
            else {
                innerData = null;
            }
            innerDataPainter.postPaint(painterContext);
        }
    }

    @Override
    public void afterPaint(PainterContext painterContext) {
        super.afterPaint(painterContext);
    }
}
