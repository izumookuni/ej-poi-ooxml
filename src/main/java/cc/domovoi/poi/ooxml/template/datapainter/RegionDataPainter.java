package cc.domovoi.poi.ooxml.template.datapainter;

import cc.domovoi.poi.ooxml.template.DataPainter;
import cc.domovoi.poi.ooxml.template.PainterContext;
import org.apache.poi.ss.usermodel.CellStyle;

import java.util.List;
import java.util.UUID;

public class RegionDataPainter implements DataPainter {

    private String id;

    private String pid;

    private Integer rowIndex;

    private Integer colIndex;

    private CellStyle cellStyle;

    private List<DataPainter> children;

    public RegionDataPainter(String id, String pid, Integer rowIndex, Integer colIndex, CellStyle cellStyle, List<DataPainter> children) {
        this.id = id;
        this.pid = pid;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.cellStyle = cellStyle;
        this.children = children;
    }

    public RegionDataPainter(String pid, Integer rowIndex, Integer colIndex, CellStyle cellStyle, List<DataPainter> children) {
        this.id = UUID.randomUUID().toString();
        this.pid = pid;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.cellStyle = cellStyle;
        this.children = children;
    }

    public String getId() {
        return id;
    }

    public String getPid() {
        return pid;
    }

    public Integer getRowIndex() {
        return rowIndex;
    }

    public Integer getColIndex() {
        return colIndex;
    }

    public CellStyle getCellStyle() {
        return cellStyle;
    }

    public List<DataPainter> getChildren() {
        return children;
    }

    @Override
    public void init(PainterContext painterContext) {
        painterContext.attachDataPainter(id, this);
    }

    @Override
    public void paint(PainterContext painterContext) {

    }
}
