package cc.domovoi.poi.ooxml.template.datasupplier;

import cc.domovoi.lambda.function.PartialFunctions;
import cc.domovoi.poi.ooxml.template.DataSupplier;
import cc.domovoi.poi.ooxml.template.datatype.DataType;
import org.jooq.lambda.function.Function2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GroupedDataSupplier<I, O> implements DataSupplier<I, O> {

    @SuppressWarnings("unchecked")
    private static <T2> T2 collectFunction0(Stream<T2> dataStream, Class<T2> dataType) {
        return PartialFunctions.of(dS -> Objects.isNull(dataType) || DataType.of(dataType) instanceof DataType.ObjectType, (Stream<T2> dS) -> (T2) dS.map(T2::toString).collect(Collectors.joining(" ")))
                .orElseOf(dS -> DataType.of(dataType) instanceof DataType.NumberType, dS -> (T2) (Double) dS.mapToDouble(data -> ((Number) data).doubleValue()).sum())
                .orElseOf(dS -> DataType.of(dataType) instanceof DataType.StringType, dS -> (T2) dS.map(data -> (String) data).collect(Collectors.joining(" ")))
                .orElseOf(dS -> DataType.of(dataType) instanceof DataType.BooleanType, dS -> (T2) dS.map(data -> (Boolean) data).reduce(Boolean::logicalAnd))
                .orElseOf(dS -> DataType.of(dataType) instanceof DataType.DoubleType, dS -> (T2) dS.map(data -> (Date) data).max(Comparator.nullsLast(Comparator.naturalOrder())).orElseGet(Date::new))
                .orElseOf(dS -> DataType.of(dataType) instanceof DataType.LocalDateTimeType, dS -> (T2) dS.map(data -> (LocalDateTime) data).max(Comparator.nullsLast(Comparator.naturalOrder())).orElseGet(LocalDateTime::now))
                .orElseOf(dS -> DataType.of(dataType) instanceof DataType.LocalDateType, dS -> (T2) dS.map(data -> (LocalDate) data).max(Comparator.nullsLast(Comparator.naturalOrder())).orElseGet(LocalDate::now))
                .orElseOf(dS -> DataType.of(dataType) instanceof DataType.LocalTimeType, dS -> (T2) dS.map(data -> (LocalTime) data).max(Comparator.nullsLast(Comparator.naturalOrder())).orElseGet(LocalTime::now))
                .orElseOf(dS -> DataType.of(dataType) instanceof DataType.LocalTimeType, dS -> (T2) dS.map(data -> (LocalTime) data).max(Comparator.nullsLast(Comparator.naturalOrder())).orElseGet(LocalTime::now))
                .end().apply(dataStream);
    }

    private Integer idx = 0;

    private Class<O> dataType;

    private Integer collectType;

    private Map<Integer, DataSupplier<I, O>> dataSupplierMap = new LinkedHashMap<>();

    private Function<? super Stream<O>, ? extends O> collectFunction1 = dataStream -> GroupedDataSupplier.collectFunction0(dataStream, dataType);

    private Function2<? super Map<Integer, DataSupplier<I, O>>, ? super I, ? extends O> collectFunction2;

    public GroupedDataSupplier(DataSupplier<I, O> dataSupplier) {
        this.dataType = null;
        this.collectType = 1;
        this.dataSupplierMap.put(idx++, dataSupplier);
    }

    public GroupedDataSupplier(Class<O> dataType, DataSupplier<I, O> dataSupplier) {
        this.dataType = dataType;
        this.collectType = 1;
        this.dataSupplierMap.put(idx++, dataSupplier);
    }

    public GroupedDataSupplier<I, O> collect(Function2<? super Map<Integer, DataSupplier<I, O>>, ? super I, ? extends O> collectFunction) {
        this.collectFunction2 = collectFunction;
        this.collectType = 2;
        return this;
    }

    public GroupedDataSupplier<I, O> add(DataSupplier<I, O> dataSupplier) {
        if (Objects.isNull(this.dataType)) {
            this.dataType = dataSupplier.dataType();
        }
        this.dataSupplierMap.put(idx++, dataSupplier);
        return this;
    }

    public GroupedDataSupplier<I, O> addGroup(GroupedDataSupplier<I, O> groupedDataSupplier) {
        groupedDataSupplier.dataSupplierMap.values().forEach(dataSupplier -> this.dataSupplierMap.put(idx++, dataSupplier));
        return this;
    }

    @Override
    public Class<O> dataType() {
        return dataType;
    }

    @SuppressWarnings("unchecked")
    @Override
    public O apply(Object o) {
        switch (collectType) {
            case 1: return collectFunction1.apply(dataSupplierMap.values().stream().map(f -> f.apply((I) o)));
            case 2: return collectFunction2.apply(dataSupplierMap, (I) o);
            default: throw new RuntimeException("no supported collectType");
        }
    }
}
