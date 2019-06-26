package cc.domovoi.poi.ooxml;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.function.Function;

public class Sheets {

    public static Row createRow(Sheet sheet, Integer rowIndex) {
        return sheet.createRow(rowIndex);
    }

    public static Function<? super Sheet, ? extends Row> createRow(Integer rowIndex) {
        return sheet -> Sheets.createRow(sheet, rowIndex);
    }
}
