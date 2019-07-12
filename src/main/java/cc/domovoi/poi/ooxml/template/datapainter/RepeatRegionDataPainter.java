package cc.domovoi.poi.ooxml.template.datapainter;

import cc.domovoi.poi.ooxml.template.DataSupplier;
import cc.domovoi.poi.ooxml.template.PainterContext;
import org.apache.poi.ss.usermodel.CellStyle;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class RepeatRegionDataPainter<T> extends RegionDataPainter<Collection<T>> {

    private Iterator<T> dataIterator;

    private RegionDataPainter<T> innerDataPainter;

    private Integer dataSize;

    public RepeatRegionDataPainter(String id, String pid, Integer rowIndex, Integer colIndex, CellStyle cellStyle, DataSupplier<Object, Collection<T>> supplier) {
        super(id, pid, rowIndex, colIndex, cellStyle, supplier);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init(PainterContext painterContext) {
        super.init(painterContext);
        innerDataPainter = new RegionDataPainter<>(getId(), getPid(), getRowIndex(), getColIndex(), getCellStyle(), o -> (T) o);
        painterContext.attachDataPainter(getId(), innerDataPainter);
        this.setChildren(Collections.singletonList(innerDataPainter));
    }

    @Override
    public void beforePaint(PainterContext painterContext) {
        Collection<T> dataList = this.getSupplier().apply(painterContext.genData(this));
        dataSize = dataList.size();
        dataIterator = dataList.iterator();
        painterContext.attachDataGetter(dataPainter -> this.getId().equals(dataPainter.getId()), () -> dataIterator.hasNext() ? dataIterator.next() : null);
    }

    @Override
    public void paint(PainterContext painterContext) {
        for (Integer i = 0; i < dataSize; i++) {
            innerDataPainter.postPaint(painterContext);
        }
    }

    @Override
    public void afterPaint(PainterContext painterContext) {
        super.afterPaint(painterContext);
    }
}
