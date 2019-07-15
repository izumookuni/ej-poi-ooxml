package cc.domovoi.poi.ooxml;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Workbook;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.Consumer;

public class CellStyles {

    public static String datePattern = "yyyy-MM-dd";

    public static String timePattern = "HH:mm:ss";

    public static String dateTimePattern = "yyyy-MM-dd HH:mm:ss";

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

    public static CellStyle attachOrCreateDateCellStyle(Workbook workbook, CellStyle cellStyle, String pattern) {
        CreationHelper createHelper = workbook.getCreationHelper();
        final CellStyle innerCellStyle;
        if (Objects.nonNull(cellStyle)) {
            innerCellStyle = cellStyle;
        }
        else {
            innerCellStyle = CellStyles.createCellStyle(workbook);
        }
        innerCellStyle.setDataFormat(createHelper.createDataFormat().getFormat(pattern));
        return innerCellStyle;
    }
}
