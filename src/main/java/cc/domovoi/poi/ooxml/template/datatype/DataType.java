package cc.domovoi.poi.ooxml.template.datatype;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface DataType<T> {

    Class<T> dataType();

    @SuppressWarnings("unchecked")
    static <T2> DataType<T2> of(Class<T2> clazz) {
        switch (clazz.getSimpleName()) {
            case "Integer": return (DataType<T2>) new IntegerType();
            case "Double": return (DataType<T2>) new DoubleType();
            case "Boolean": return (DataType<T2>) new BooleanType();
            case "String": return (DataType<T2>) new StringType();
            case "Date": return (DataType<T2>) new DateType();
            case "LocalDateTime": return (DataType<T2>) new LocalDateTimeType();
            case "LocalDate": return (DataType<T2>) new LocalDateType();
            case "LocalTime": return (DataType<T2>) new LocalTimeType();
            case "Object": return (DataType<T2>) new ObjectType();
            default: throw new RuntimeException(String.format("no support class %s", clazz.getName()));
        }
    }

    class ObjectType implements DataType<Object> {
        @Override
        public Class<Object> dataType() {
            return Object.class;
        }
    }

    class NumberType implements DataType<Number> {
        @Override
        public Class<Number> dataType() {
            return Number.class;
        }
    }

    class IntegerType implements DataType<Integer> {
        @Override
        public Class<Integer> dataType() {
            return Integer.class;
        }
    }

    class DoubleType implements DataType<Double> {
        @Override
        public Class<Double> dataType() {
            return Double.class;
        }
    }

    class BooleanType implements DataType<Boolean> {
        @Override
        public Class<Boolean> dataType() {
            return Boolean.class;
        }
    }

    class StringType implements DataType<String> {
        @Override
        public Class<String> dataType() {
            return String.class;
        }
    }

    class DateType implements DataType<Date> {
        @Override
        public Class<Date> dataType() {
            return Date.class;
        }
    }

    class LocalDateTimeType implements DataType<LocalDateTime> {
        @Override
        public Class<LocalDateTime> dataType() {
            return LocalDateTime.class;
        }
    }

    class LocalDateType implements DataType<LocalDate> {
        @Override
        public Class<LocalDate> dataType() {
            return LocalDate.class;
        }
    }

    class LocalTimeType implements DataType<LocalTime> {
        @Override
        public Class<LocalTime> dataType() {
            return LocalTime.class;
        }
    }
}
