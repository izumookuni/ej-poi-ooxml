package cc.domovoi.poi.ooxml;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.*;
import java.util.Optional;

public class Workbooks {

    public static Workbook create() {
        return new HSSFWorkbook();
    }

    public static void write(Workbook workbook, OutputStream outputStream) {
        try (Workbook wb = workbook; OutputStream os = outputStream) {
            wb.write(os);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void write(Workbook workbook, File file) {
        try {
            Workbooks.write(workbook, new FileOutputStream(file));
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    public static void write(Workbook workbook, String path) {
        Workbooks.write(workbook, new File(path));
    }

    public static Optional<Workbook> load(File file) {
        // Using a File object allows for lower memory consumption.
        try {
            return Optional.of(WorkbookFactory.create(file));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return Optional.empty();
        }
    }

    public static Optional<Workbook> load(InputStream inputStream) {
        //  while an InputStream requires more memory as it has to buffer the whole file.
        try (InputStream is = inputStream) {
            return Optional.of(WorkbookFactory.create(is));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return Optional.empty();
        }
    }

    public static Optional<HSSFWorkbook> loadByPOIFSFileSystem(File file) {
        try (POIFSFileSystem fs = new POIFSFileSystem(file)) {
            return Optional.of(new HSSFWorkbook(fs.getRoot(), true));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return Optional.empty();
        }
    }

    public static Optional<HSSFWorkbook> loadByPOIFSFileSystem(InputStream inputStream) {
        try (InputStream is = inputStream; POIFSFileSystem fs = new POIFSFileSystem(is)) {

            return Optional.of(new HSSFWorkbook(fs.getRoot(), true));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return Optional.empty();
        }
    }


}
