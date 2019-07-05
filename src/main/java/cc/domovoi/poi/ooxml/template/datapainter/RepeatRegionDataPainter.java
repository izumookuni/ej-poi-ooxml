package cc.domovoi.poi.ooxml.template.datapainter;

import cc.domovoi.poi.ooxml.template.DataSupplier;
import cc.domovoi.poi.ooxml.template.PainterContext;
import org.apache.poi.ss.usermodel.CellStyle;

import java.util.Collection;
import java.util.Iterator;

public class RepeatRegionDataPainter<T> extends RegionDataPainter<Collection<T>> {

    private Iterator<T> dataIterator;

    public RepeatRegionDataPainter(String id, String pid, Integer rowIndex, Integer colIndex, CellStyle cellStyle, DataSupplier<Object, Collection<T>> supplier) {
        super(id, pid, rowIndex, colIndex, cellStyle, supplier);
    }

    @Override
    public void beforePaint(PainterContext painterContext) {
        Collection<T> dataList = this.getSupplier().apply(painterContext.genData(this));
        dataIterator = dataList.iterator();
        painterContext.attachDataGetter(dataPainter -> this.getId().equals(dataPainter.getPid()), () -> dataIterator.hasNext() ? dataIterator.next() : null);
    }

    @Override
    public void paint(PainterContext painterContext) {
        super.paint(painterContext);
    }

    @Override
    public void afterPaint(PainterContext painterContext) {
        super.afterPaint(painterContext);
    }
}
