package cc.domovoi.poi.ooxml;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.function.Consumer;

public class Fonts {

    public static Font createFont(Workbook workbook) {
        return Fonts.createFont(workbook, font -> {});
    }

    public static Font createFont(Workbook workbook, Consumer<Font> fontConsumer) {
        Font font = workbook.createFont();
        fontConsumer.accept(font);
        return font;
    }
}
