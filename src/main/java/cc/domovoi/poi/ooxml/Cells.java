package cc.domovoi.poi.ooxml;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jooq.lambda.tuple.Tuple2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cells {

    public static String convertNumToRowString(Integer row) {
        return String.valueOf(row + 1);
    }

    public static Integer convertRowStringToIndex(String ref) {
        return Integer.parseInt(ref) - 1;
    }

    public static String convertNumToColString(Integer col) {
        // From CellReference.convertNumToColString
        int excelColNum = col + 1;

        StringBuilder colRef = new StringBuilder(2);
        int colRemain = excelColNum;

        while(colRemain > 0) {
            int thisPart = colRemain % 26;
            if(thisPart == 0) { thisPart = 26; }
            colRemain = (colRemain - thisPart) / 26;

            // The letter A is at 65
            char colChar = (char)(thisPart+64);
            colRef.insert(0, colChar);
        }

        return colRef.toString();
    }

    public static Integer convertColStringToIndex(String ref) {
        // From CellReference.convertColStringToIndex
        int retval=0;
        char[] refs = ref.toUpperCase(Locale.ROOT).toCharArray();
        for (int k=0; k<refs.length; k++) {
            char thechar = refs[k];
            if (thechar == '$') {
                if (k != 0) {
                    throw new IllegalArgumentException("Bad col ref format '" + ref + "'");
                }
                continue;
            }

            // Character is uppercase letter, find relative value to A
            retval = (retval * 26) + (thechar - 'A' + 1);
        }
        return retval-1;
    }

    public static String mapCellIndex(Integer rowIndex, Integer colIndex) {
        return Cells.convertNumToColString(colIndex) + Cells.convertNumToRowString(rowIndex);
    }

    public static Tuple2<Integer, Integer> unmapCellIndex(String ref) {
        Pattern pattern = Pattern.compile("^([A-Za-z]+)([0-9]+)$");
        Matcher matcher = pattern.matcher(ref);
        if (matcher.matches()) {
            String colString = matcher.group(1);
            String rowString = matcher.group(2);
            return new Tuple2<>(Cells.convertRowStringToIndex(rowString), Cells.convertColStringToIndex(colString));
        }
        else {
            throw new RuntimeException(String.format("ref [%s] parse error", ref));
        }
    }

    public static Optional<CellStyle> getCellStyle(Cell cell) {
        return Optional.ofNullable(cell.getCellStyle());
    }

    public static Optional<Font> getFont(Cell cell, Workbook workbook) {
        return Cells.getCellStyle(cell).map(CellStyle::getFontIndexAsInt).map(workbook::getFontAt);
    }

    public static Optional<LocalDate> getLocalDateValue(Cell cell) {
        try {
            return Optional.ofNullable(LocalDate.from(cell.getDateCellValue().toInstant()));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static Optional<LocalDateTime> getLocalDateTimeValue(Cell cell) {
        try {
            return Optional.of(LocalDateTime.from(cell.getDateCellValue().toInstant()));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static Cell setCellValue(Cell cell, LocalDateTime value, ZoneOffset zoneOffset) {
        cell.setCellValue(Date.from(value.toInstant(zoneOffset)));
        return cell;
    }

    public static Cell setCellValue(Cell cell, LocalDateTime value, String offsetId) {
        cell.setCellValue(Date.from(value.toInstant(ZoneOffset.of(offsetId))));
        return cell;
    }

    public static String getValueAsString(Cell cell) {
        // Todo: ...
        return "";
    }

    public static Cell addMergedRegionAsCell(Sheet sheet, Integer firstRowIndex, Integer lastRowIndex, Integer firstColIndex, Integer lastColIndex) {
        Cell cell = sheet.createRow(firstRowIndex).createCell(firstColIndex);
        Sheets.addMergedRegion(sheet, firstRowIndex, lastRowIndex, firstColIndex, lastColIndex);
        return cell;
    }

    public static Cell addMergedRegionAsCell(Sheet sheet, String ref) {
        CellRangeAddress cellRangeAddress = CellRangeAddress.valueOf(ref);
        Cell cell = sheet.createRow(cellRangeAddress.getFirstRow()).createCell(cellRangeAddress.getFirstColumn());
        Sheets.addMergedRegion(sheet, ref);
        return cell;
    }

    public static Cell setCellFormula(Cell cell, String formula) {
        cell.setCellFormula(formula);
        return cell;
    }

}
