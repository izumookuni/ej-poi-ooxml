package cc.domovoi.poi.ooxml.test;

import cc.domovoi.poi.ooxml.template.DataSupplier;
import cc.domovoi.poi.ooxml.template.datasupplier.SimplePropertyDataSupplier;
import cc.domovoi.poi.ooxml.test.data.SimpleData;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

import static cc.domovoi.poi.ooxml.template.PainterApplication.*;

public class PainterApplicationTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private SimpleData data;

    private Random random = new Random();

    @Before
    public void initData() {
        data = new SimpleData();
        data.setV1("hello");
        data.setV2(42);
        data.setV3(true);
        data.setV4(new Date());
        data.setV5(3.14);
        data.setV6(LocalDateTime.of(2018, 12, 12, 3, 4, 22));
        data.setV7(LocalDate.of(2017, 4, 2));
        data.setV8(LocalTime.of(12, 44, 32));
        SimpleData innerData = new SimpleData();
        innerData.setV2(666);
        innerData.setV5(2.33);
        innerData.setV1("world!");
        data.setInnerData(innerData);
        SimpleData listData1 = new SimpleData();
        listData1.setV1("l1");
        listData1.setV5(32.5);
        listData1.setV2(7);
        SimpleData listData2 = new SimpleData();
        listData2.setV1("l2");
        listData2.setV5(41.2);
        listData2.setV2(2);
        SimpleData listData3 = new SimpleData();
        listData3.setV1("l3");
        listData3.setV5(12.4);
        listData3.setV2(5);
        data.setListData(Arrays.asList(listData1, listData2, listData3));

    }

    @Test
    public void testPainterApplication() throws Exception {
        create();
        sheet("Hello");
        data(data);
        rowHeight(18.55);
        colWidth(22);

        font("bold", f -> {
            f.setBold(true);
        });

        style("s1", s -> s.setFont(font("bold")));

        cell("1", 0, 0, 1, 1, style("s1"), str("hello world!"));
        cell("2", 0, 1, 1, 1, null, num(42.66));
        cell("3", 0, 2, 3, 1, null, numProperty("v2"));
        cell("4", 1, 0, 1, 1, null, strProperty("v1"));
        cell("5", 1, 1, 1, 1, null, dateProperty("v4"));
        cell("6", 1, 2, 1, 1, null, boolProperty("v3"));
        cell("7", 2, 0, 1, 1, null, localDateTimeProperty("v6"));
        cell("8", 2, 1, 1, 1, null, localDateProperty("v7"));
        cell("9", 2, 2, 1, 1, null, localTimeProperty("v8"));
        cell("10", 3, 0, 1, 2, null, numProperty("innerData", "v2"));

        region("r1", 5, 0, null, true, property("innerData"));
        cell("11", "r1", 0, 0, 1, 1, null, numProperty("v5"));
        cell("12", "r1", 1, 1, 1, 1, null, strProperty("v1"));

        region("r2", null, null, null, true, property("innerData"));
        cell("13", "r2", 3, 2, 1, 1, null, numProperty("v5"));
        cell("14", "r2", 4, 0, 1, 1, null, strProperty("v1"));

        repeat("r3", null, null, null, property("listData"));
        cell("15", "r3", 0, 0, 1, 1, null, strProperty("v1"));
        cell("16", "r3", 0, 1, 1, 1, null, strProperty("v1").grouped(numProperty("v5").then(String.class, Number::toString)));
        cell("17", "r3", 1, 2, 1, 1, null, numProperty("v5").then(Number.class, i -> Objects.nonNull(i) ? i.doubleValue() + 1 : null));
        cell("18", "r3", 0, 3, 1, 1, null, numProperty("v2").outCondition(n -> ((Number) n).intValue() > 3));
        cell("19", "r3", 2, 0, 1, 1, null, numAutoInc());

        postPaint();
        write("src/test/resources/test.xlsx");
    }

    @Test
    public void testGroupedDataSupplier() throws Exception {

        SimplePropertyDataSupplier<String> stringDataSupplier1 = strProperty("v1");
        logger.debug("stringDataSupplier: " + stringDataSupplier1.dataType());

        SimplePropertyDataSupplier<Number> numberDataSupplier = numProperty("v5");
        logger.debug("numberDataSupplier: " + numberDataSupplier.dataType());

        SimplePropertyDataSupplier<String> stringDataSupplier2 = numberDataSupplier.then(String.class, Number::toString);
        logger.debug("stringDataSupplier2: " + stringDataSupplier2.dataType());

        DataSupplier<Object, String> stringDataSupplier = strProperty("v1").grouped(stringDataSupplier2);
        logger.debug("stringDataSupplier: " + stringDataSupplier.dataType());
    }
}
