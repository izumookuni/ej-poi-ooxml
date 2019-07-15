package cc.domovoi.poi.ooxml.test;

import cc.domovoi.poi.ooxml.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class OOXMLTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

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
        logger.debug("1: " + cell.getNumericCellValue());
        logger.debug("2: " + cell.getDateCellValue());
    }

    @Test
    public void testCreateWorkbook() throws Exception {
        Workbook wb = Workbooks.create();

        Sheet sheet = Workbooks.createSheet(wb);
        Cell cell1 = Sheets.createRow(0).andThen(Rows.createCell(0)).apply(sheet);
        Cell cell2 = Sheets.createRow(1).andThen(Rows.createCell(0)).apply(sheet);
        Cell cell3 = Sheets.createRow(2).andThen(Rows.createCell(0)).apply(sheet);

        cell1.setCellValue(1);
        cell2.setCellValue(2);
        cell3.setCellFormula(String.format("SUM(%s:%s)", cell1.getAddress(), cell2.getAddress()));


        Workbooks.write(wb, new File(String.format("src/test/resources/%s.xlsx", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }
}
