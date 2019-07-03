package cc.domovoi.poi.ooxml.template.cellvalue;

import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;

public class CellValueSetters {

    private static String zoneOffsetId = "+8";

    public static void setZoneOffsetId(String zoneOffsetId) {
        CellValueSetters.zoneOffsetId = zoneOffsetId;
    }

    public static CellValueSetter forClass(Class<?> clazz) {
        switch (clazz.getName()) {
            case "String": return new StringCellValueSetter();
            case "Double": return new DoubleCellValueSetter();
            case "Integer": return new IntegerCellValueSetter();
            case "Boolean": return new BooleanCellValueSetter();
            case "Date": return new DateCellValueSetter();
            case "LocalDateTime": return new LocalDateTimeCellValueSetter(zoneOffsetId);
            case "LocalDate": return new LocalDateCellValueSetter(zoneOffsetId);
            case "LocalTime": return new LocalTimeCellValueSetter(zoneOffsetId);
            default: throw new RuntimeException(String.format("no support class %s", clazz.getName()));
        }
    }

    public static StringCellValueSetter stringCellValueSetter = new StringCellValueSetter();
    public static DoubleCellValueSetter doubleCellValueSetter = new DoubleCellValueSetter();
    public static IntegerCellValueSetter integerCellValueSetter = new IntegerCellValueSetter();
    public static BooleanCellValueSetter booleanCellValueSetter = new BooleanCellValueSetter();
    public static DateCellValueSetter dateCellValueSetter = new DateCellValueSetter();

    public static LocalDateTimeCellValueSetter localDateTimeCellValueSetter(String zoneOffsetId) {
        return new LocalDateTimeCellValueSetter(zoneOffsetId);
    }

    public static LocalDateCellValueSetter localDateCellValueSetter(String zoneOffsetId) {
        return new LocalDateCellValueSetter(zoneOffsetId);
    }

    public static LocalTimeCellValueSetter localTimeCellValueSetter(String zoneOffsetId) {
        return new LocalTimeCellValueSetter(zoneOffsetId);
    }


    // static

    public static class StringCellValueSetter implements CellValueSetter<String> {
        @Override
        public void setCellValue(Object o, Cell cell) {
            cell.setCellValue((String) o);
        }
    }

    public static class DoubleCellValueSetter implements CellValueSetter<Double> {
        @Override
        public void setCellValue(Object o, Cell cell) {
            cell.setCellValue((Double) o);
        }
    }

    public static class IntegerCellValueSetter implements CellValueSetter<Integer> {
        @Override
        public void setCellValue(Object o, Cell cell) {
            cell.setCellValue((Double) o);
        }
    }

    public static class BooleanCellValueSetter implements CellValueSetter<Boolean> {
        @Override
        public void setCellValue(Object o, Cell cell) {
            cell.setCellValue((Boolean) o);
        }
    }

    public static class DateCellValueSetter implements CellValueSetter<Date> {
        @Override
        public void setCellValue(Object o, Cell cell) {
            cell.setCellValue((Date) o);
        }
    }

    public static class LocalDateTimeCellValueSetter implements CellValueSetter<LocalDateTime> {

        private String zoneOffsetId;

        @Override
        public void setCellValue(Object o, Cell cell) {
            cell.setCellValue(Date.from(((LocalDateTime) o).toInstant(ZoneOffset.of(zoneOffsetId))));
        }

        public LocalDateTimeCellValueSetter(String zoneOffsetId) {
            this.zoneOffsetId = zoneOffsetId;
        }
    }

    public static class LocalDateCellValueSetter implements CellValueSetter<LocalDate> {

        private String zoneOffsetId;

        @Override
        public void setCellValue(Object o, Cell cell) {
            cell.setCellValue(Date.from(((LocalDate) o).atStartOfDay().toInstant(ZoneOffset.of(zoneOffsetId))));
        }

        public LocalDateCellValueSetter(String zoneOffsetId) {
            this.zoneOffsetId = zoneOffsetId;
        }
    }

    public static class LocalTimeCellValueSetter implements CellValueSetter<LocalTime> {

        private String zoneOffsetId;

        @Override
        public void setCellValue(Object o, Cell cell) {
            cell.setCellValue(Date.from(((LocalTime) o).atDate(LocalDate.MIN).toInstant(ZoneOffset.of(zoneOffsetId))));
        }

        public LocalTimeCellValueSetter(String zoneOffsetId) {
            this.zoneOffsetId = zoneOffsetId;
        }
    }

}
