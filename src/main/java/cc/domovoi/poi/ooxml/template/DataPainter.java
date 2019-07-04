package cc.domovoi.poi.ooxml.template;

public interface DataPainter {

    void init(PainterContext painterContext);

    void beforePaint(PainterContext painterContext);

    void paint(PainterContext painterContext);

    void afterPaint(PainterContext painterContext);
}
