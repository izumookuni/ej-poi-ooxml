package cc.domovoi.poi.ooxml.template.cellvalue;

import org.apache.poi.ss.usermodel.Cell;

public interface CellValueSetter<T> {

    void setCellValue(Object o, Cell cell);
}
