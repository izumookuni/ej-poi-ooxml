package cc.domovoi.poi.ooxml;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * 工作簿工具类
 */
public class Workbooks {

    /**
     * 创建工作簿（xlsx格式）
     * @return 新工作簿
     */
    public static XSSFWorkbook create() {
        return new XSSFWorkbook();
    }

    /**
     * 创建工作簿（xls格式）
     * @return 新工作簿
     */
    public static HSSFWorkbook createLegacy() { return new HSSFWorkbook();}

    /**
     * 导出工作簿
     * @param workbook 工作簿
     * @param outputStream 输出流
     */
    public static void write(Workbook workbook, OutputStream outputStream) {
        try (Workbook wb = workbook; OutputStream os = outputStream) {
            wb.write(os);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * 导出工作簿
     * @param workbook 工作簿
     * @param file 文件
     */
    public static void write(Workbook workbook, File file) {
        try {
            Workbooks.write(workbook, new FileOutputStream(file));
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    /**
     * 导出工作簿
     * @param workbook 工作簿
     * @param path 路径
     */
    public static void write(Workbook workbook, String path) {
        Workbooks.write(workbook, new File(path));
    }

    /**
     * 读取工作簿
     * @param path 路径
     * @return 工作簿
     */
    public static Optional<Workbook> load(String path) {
        return Workbooks.load(new File(path));
    }

    /**
     * 读取工作簿
     * @param file 文件
     * @return 工作簿
     */
    public static Optional<Workbook> load(File file) {
        // Using a File object allows for lower memory consumption.
        try {
            return Optional.of(WorkbookFactory.create(file));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 读取工作簿
     * @param inputStream 输入流
     * @return 工作簿
     */
    public static Optional<Workbook> load(InputStream inputStream) {
        //  while an InputStream requires more memory as it has to buffer the whole file.
        try (InputStream is = inputStream) {
            return Optional.of(WorkbookFactory.create(is));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 读取工作簿
     * @param file 文件
     * @return 工作簿
     */
    public static Optional<HSSFWorkbook> loadByPOIFSFileSystem(File file) {
        try (POIFSFileSystem fs = new POIFSFileSystem(file)) {
            return Optional.of(new HSSFWorkbook(fs.getRoot(), true));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 读取工作簿
     * @param inputStream 输入流
     * @return 工作簿
     */
    public static Optional<HSSFWorkbook> loadByPOIFSFileSystem(InputStream inputStream) {
        try (InputStream is = inputStream; POIFSFileSystem fs = new POIFSFileSystem(is)) {

            return Optional.of(new HSSFWorkbook(fs.getRoot(), true));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 创建表
     * @param workbook 工作簿
     * @param sheetName 表名称
     * @return 表
     */
    public static Sheet createSheet(Workbook workbook, String sheetName) {
        return workbook.createSheet(sheetName);
    }

    /**
     * 创建将工作簿转换成表的函数
     * @param sheetName 表名称
     * @return 将工作簿转换成表的函数
     */
    public static Function<? super Workbook, ? extends Sheet> createSheet(String sheetName) {
        return workbook -> workbook.createSheet(sheetName);
    }

    /**
     * 创建表（默认名称）
     * @param workbook 工作簿
     * @return 表
     */
    public static Sheet createSheet(Workbook workbook) {
        return workbook.createSheet();
    }

    /**
     * 创建将工作簿转换成表（默认名称）的函数
     * @return 将工作簿转换成表（默认名称）的函数
     */
    public static Function<? super Workbook, ? extends Sheet> createSheet() {
        return Workbook::createSheet;
    }

    /**
     * 获取或创建表
     * 先判断指定名称的表是否存在，存在则获取，不存在则创建
     * @param workbook 工作簿
     * @param name 表名称
     * @return 表
     */
    public static Sheet getOrCreateSheet(Workbook workbook, String name) {
        Sheet sheet = workbook.getSheet(name);
        if (Objects.isNull(sheet)) {
            sheet = workbook.createSheet(name);
        }
        return sheet;
    }

    /**
     * 创建获取或创建表的函数
     * 先判断指定名称的表是否存在，存在则获取，不存在则创建
     * @param name 表名称
     * @return 获取或创建表的函数
     */
    public static Function<? super Workbook, ? extends Sheet> getOrCreateSheet(String name) {
        return workbook -> Workbooks.getOrCreateSheet(workbook, name);
    }

    /**
     * 创建获取或创建表的函数
     * 先判断指定名称的表是否存在，存在则获取，不存在则创建
     * @param workbook 工作簿
     * @return 获取或创建表的函数
     */
    public static Function<? super String, ? extends Sheet> getOrCreateSheet(Workbook workbook) {
        return name -> Workbooks.getOrCreateSheet(workbook, name);
    }
}
