package cc.domovoi.poi.ooxml.template;

import cc.domovoi.poi.ooxml.template.datastamp.DataStamp;
import cc.domovoi.poi.ooxml.template.datastamp.DataStampImpl;
import cc.domovoi.poi.ooxml.template.datastamp.SimpleDataStampImpl;
import org.apache.poi.ss.usermodel.Font;

import java.util.function.Consumer;

public class FontStamp extends DataStampImpl<Font> {

    public static DataStamp<Font> stamp(String id) {
        return new SimpleDataStampImpl<>(id, PainterContext::getFontMap);
    }

    private Consumer<? super Font> consumer;

    public FontStamp(String id, Consumer<? super Font> consumer) {
        super(id, PainterContext::getFontMap);
        this.consumer = consumer;
    }

    @Override
    public Font genData(PainterContext painterContext) {
        Font font = painterContext.getWorkbook().createFont();
        consumer.accept(font);
        getDataMap().apply(painterContext).put(getId(), font);
        return font;
    }
}
