package cc.domovoi.poi.ooxml.test;

import cc.domovoi.poi.ooxml.CellStyles;
import cc.domovoi.poi.ooxml.Cells;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.junit.Assert;
import org.junit.Test;

import java.time.format.DateTimeFormatter;
import java.util.Date;

public class OOXMLTest {

    @Test
    public void test1() throws Exception {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet();
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        wb.close();
    }

    @Test
    public void testCellIndexUtils() throws Exception {
        Assert.assertEquals("A", Cells.convertNumToColString(0));
        Assert.assertEquals("B", Cells.convertNumToColString(1));
        Assert.assertEquals("Z", Cells.convertNumToColString(25));
        Assert.assertEquals("AA", Cells.convertNumToColString(26));
    }

    @Test
    public void testLocalDateValue() throws Exception {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet();
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        CellStyle cellStyle = CellStyles.createDateCellStyle(wb, "yyyy-MM-dd HH:mm:ss");
        cell.setCellStyle(cellStyle);
        cell.setCellValue(new Date());
        System.out.println("1: " + cell.getNumericCellValue());
        System.out.println("2: " + cell.getDateCellValue());
    }
}
