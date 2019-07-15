package cc.domovoi.poi.ooxml.template.cellvalue;

import org.apache.poi.ss.usermodel.Cell;

import java.math.BigDecimal;
import java.math.BigInteger;
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

    public static String zoneOffsetId() {
        return CellValueSetters.zoneOffsetId;
    }

    public static CellValueSetter<?> forClass(Class<?> clazz) {
        switch (clazz.getSimpleName()) {
            case "String": return stringCellValueSetter;
            case "Number": return numberCellValueSetter;
//            case "Double": return doubleCellValueSetter;
//            case "Integer": return integerCellValueSetter;
            case "Boolean": return booleanCellValueSetter;
            case "Date": return dateCellValueSetter;
            case "LocalDateTime": return localDateTimeCellValueSetter(zoneOffsetId);
            case "LocalDate": return localDateCellValueSetter(zoneOffsetId);
            case "LocalTime": return localTimeCellValueSetter(zoneOffsetId);
            default: throw new RuntimeException(String.format("no support class %s", clazz.getName()));
        }
    }

    public static StringCellValueSetter stringCellValueSetter = new StringCellValueSetter();
    public static NumberCellValueSetter numberCellValueSetter = new NumberCellValueSetter();
//    public static DoubleCellValueSetter doubleCellValueSetter = new DoubleCellValueSetter();
//    public static IntegerCellValueSetter integerCellValueSetter = new IntegerCellValueSetter();
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

    public static class NumberCellValueSetter implements CellValueSetter<Number> {
        @Override
        public void setCellValue(Object o, Cell cell) {
            if (o instanceof Double) {
                cell.setCellValue((double) o);
            }
            else if (o instanceof Integer) {
                cell.setCellValue((int) o);
            }
            else if (o instanceof Long) {
                cell.setCellValue((long) o);
            }
            else if (o instanceof Short) {
                cell.setCellValue((short) o);
            }
            else if (o instanceof Byte) {
                cell.setCellValue((byte) o);
            }
            else if (o instanceof Float) {
                cell.setCellValue((float) o);
            }
            else if (o instanceof BigDecimal) {
                cell.setCellValue(((BigDecimal) o).doubleValue());
            }
            else if (o instanceof BigInteger) {
                cell.setCellValue(((BigInteger) o).intValue());
            }
            else {
                throw new RuntimeException(String.format("no support class %s", o.getClass().getName()));
            }
        }
    }

//    public static class DoubleCellValueSetter implements CellValueSetter<Double> {
//        @Override
//        public void setCellValue(Object o, Cell cell) {
//            cell.setCellValue((Double) o);
//        }
//    }
//
//    public static class IntegerCellValueSetter implements CellValueSetter<Integer> {
//        @Override
//        public void setCellValue(Object o, Cell cell) {
//            cell.setCellValue((Double) o);
//        }
//    }

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
            cell.setCellValue(Date.from(((LocalTime) o).atDate(LocalDate.now()).toInstant(ZoneOffset.of(zoneOffsetId))));
        }

        public LocalTimeCellValueSetter(String zoneOffsetId) {
            this.zoneOffsetId = zoneOffsetId;
        }
    }

}
