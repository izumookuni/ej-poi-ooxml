package cc.domovoi.poi.ooxml.utils;

import java.util.Objects;

public class NullUtils {

    public static Integer defaultInteger(Integer value) {
        return Objects.nonNull(value) ? value : 0;
    }

    public static Double defaultDouble(Double value) {
        return Objects.nonNull(value) ? value : 0.0;
    }
}
