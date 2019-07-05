package cc.domovoi.poi.ooxml;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Rows {

    public static Cell createCell(Row row, Integer colIndex, CellStyle cellStyle) {
        Cell cell = row.createCell(colIndex);
        if (Objects.nonNull(cellStyle)) {
            cell.setCellStyle(cellStyle);
        }
        return cell;
    }

    public static Cell createCell(Row row, Integer colIndex) {
        return Rows.createCell(row, colIndex, null);
    }

    public static Cell getOrCreateCell(Row row, Integer colIndex, CellStyle cellStyle) {
        Cell cell = row.getCell(colIndex);
        if (Objects.isNull(cell)) {
            cell = row.createCell(colIndex);
            if (Objects.nonNull(cellStyle)) {
                cell.setCellStyle(cellStyle);
            }
        }
        return cell;
    }

    public static Cell getOrCreateCell(Row row, Integer colIndex) {
        return Rows.getOrCreateCell(row, colIndex, null);
    }

    public static Function<? super Row, ? extends Cell> createCell(Integer colIndex) {
        return row -> Rows.createCell(row, colIndex);
    }

    public static Function<? super Integer, ? extends Cell> createCell(Row row) {
        return colIndex -> Rows.createCell(row, colIndex);
    }

    public static Function<? super Row, ? extends Cell> getOrCreateCell(Integer colIndex) {
        return row -> Rows.getOrCreateCell(row, colIndex);
    }

    public static Function<? super Integer, ? extends Cell> getOrCreateCell(Row row) {
        return colIndex -> Rows.getOrCreateCell(row, colIndex);
    }

    public static Cell createCellWithCellStyle(Row row, Integer colIndex, Workbook workbook, Consumer<CellStyle> cellStyleConsumer) {
        CellStyle cellStyle = CellStyles.createCellStyle(workbook, cellStyleConsumer);
        return Rows.createCell(row, colIndex, cellStyle);
    }

    public static List<Cell> createDenseCellList(Row row, Integer startColIndex, Integer endColIndex) {
        return IntStream.range(startColIndex, endColIndex).mapToObj(row::createCell).collect(Collectors.toList());
    }

    public static List<Cell> createSparseCellList(Row row, int... index) {
        return IntStream.of(index).mapToObj(row::createCell).collect(Collectors.toList());
    }

    public static List<Cell> createSparseCellList(Row row, List<Integer> index) {
        return index.stream().map(row::createCell).collect(Collectors.toList());
    }

    public static Cell createCellWithFormula(Row row, Integer colIndex, String formula) {
        return null;
    }
}
