package cc.domovoi.poi.ooxml;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Workbook;

import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class CellStyles {

    public static CellStyle createCellStyle(Workbook workbook) {
        return CellStyles.createCellStyle(workbook, cellStyle -> {});
    }

    public static CellStyle createCellStyle(Workbook workbook, Consumer<CellStyle> cellStyleConsumer) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyleConsumer.accept(cellStyle);
        return cellStyle;
    }

    public static CellStyle createDateCellStyle(Workbook workbook, String pattern) {
        CreationHelper createHelper = workbook.getCreationHelper();
        return CellStyles.createCellStyle(workbook, cellStyle -> cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(pattern)));
    }
}
