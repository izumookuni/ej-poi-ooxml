package cc.domovoi.poi.ooxml;

import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.Objects;
import java.util.function.Function;

public class Sheets {

    public static Row createRow(Sheet sheet, Integer rowIndex) {
        return sheet.createRow(rowIndex);
    }

    public static Function<? super Sheet, ? extends Row> createRow(Integer rowIndex) {
        return sheet -> Sheets.createRow(sheet, rowIndex);
    }

    public static Function<? super Integer, ? extends Row> createRow(Sheet sheet) {
        return rowIndex -> Sheets.createRow(sheet, rowIndex);
    }

    public static Row getOrCreateRow(Sheet sheet, Integer rowIndex) {
        Row row = sheet.getRow(rowIndex);
        if (Objects.isNull(row)) {
            row = sheet.createRow(rowIndex);
        }
        return row;
    }

    public static Function<? super Sheet, ? extends Row> getOrCreateRow(Integer rowIndex) {
        return sheet -> Sheets.getOrCreateRow(sheet, rowIndex);
    }

    public static Function<? super Integer, ? extends Row> getOrCreateRow(Sheet sheet) {
        return rowIndex -> Sheets.getOrCreateRow(sheet, rowIndex);
    }

    public static Integer addMergedRegion(Sheet sheet, Integer firstRowIndex, Integer lastRowIndex, Integer firstColIndex, Integer lastColIndex) {
        return sheet.addMergedRegion(new CellRangeAddress(firstRowIndex, lastRowIndex, firstColIndex, lastColIndex));
    }

    public static Integer addMergedRegion(Sheet sheet, String ref) {
        return sheet.addMergedRegion(CellRangeAddress.valueOf(ref));
    }

    public static Sheet fitToOnePage(Sheet sheet) {
        PrintSetup ps = sheet.getPrintSetup();
        sheet.setAutobreaks(true);
        ps.setFitHeight((short) 1);
        ps.setFitWidth((short) 1);
        return sheet;
    }

    public static Integer getSheetIndex(Sheet sheet, Workbook workbook) {
        return workbook.getSheetIndex(sheet);
    }

    public static Sheet setPrintArea(Sheet sheet, String ref, Workbook workbook) {
        workbook.setPrintArea(Sheets.getSheetIndex(sheet, workbook), ref);
        return sheet;
    }

    public static Sheet setPrintArea(Sheet sheet, Integer startRowIndex, Integer endRowIndex, Integer startColIndex, Integer endColIndex, Workbook workbook) {
        workbook.setPrintArea(Sheets.getSheetIndex(sheet, workbook), startColIndex, endColIndex, startRowIndex, endRowIndex);
        return sheet;
    }
}
