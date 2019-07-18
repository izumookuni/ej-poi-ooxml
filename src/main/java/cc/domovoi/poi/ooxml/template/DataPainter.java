package cc.domovoi.poi.ooxml.template;

import java.util.Objects;

public interface DataPainter<T> {

    String getId();

    String getPid();

    Integer getRowOffset();

    void setRowOffset(Integer rowOffset);

    Integer getColOffset();

    void setColOffset(Integer colOffset);

    default Boolean root() {
        return Objects.isNull(getPid());
    }

    void init(PainterContext painterContext);

    default void postPaint(PainterContext painterContext) {
        beforePaint(painterContext);
        paint(painterContext);
        afterPaint(painterContext);
    }

    void beforePaint(PainterContext painterContext);

    void paint(PainterContext painterContext);

    void afterPaint(PainterContext painterContext);
}
