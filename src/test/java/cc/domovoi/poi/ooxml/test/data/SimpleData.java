package cc.domovoi.poi.ooxml.test.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SimpleData {

    private String v1;

    private Integer v2;

    private Boolean v3;

    private Date v4;

    private Double v5;

    private LocalDateTime v6;

    private LocalDate v7;

    private LocalTime v8;

    private List<SimpleData> listData;

    private SimpleData innerData;

    public SimpleData() {
        listData = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "SimpleData{" +
                "v1='" + v1 + '\'' +
                ", v2=" + v2 +
                ", v3=" + v3 +
                ", v4=" + v4 +
                ", v5=" + v5 +
                ", v6=" + v6 +
                ", v7=" + v7 +
                ", v8=" + v8 +
                ", listData=" + listData +
                ", innerData=" + innerData +
                '}';
    }

    public String getV1() {
        return v1;
    }

    public void setV1(String v1) {
        this.v1 = v1;
    }

    public Integer getV2() {
        return v2;
    }

    public void setV2(Integer v2) {
        this.v2 = v2;
    }

    public Boolean getV3() {
        return v3;
    }

    public void setV3(Boolean v3) {
        this.v3 = v3;
    }

    public Date getV4() {
        return v4;
    }

    public void setV4(Date v4) {
        this.v4 = v4;
    }

    public Double getV5() {
        return v5;
    }

    public void setV5(Double v5) {
        this.v5 = v5;
    }

    public LocalDateTime getV6() {
        return v6;
    }

    public void setV6(LocalDateTime v6) {
        this.v6 = v6;
    }

    public LocalDate getV7() {
        return v7;
    }

    public void setV7(LocalDate v7) {
        this.v7 = v7;
    }

    public LocalTime getV8() {
        return v8;
    }

    public void setV8(LocalTime v8) {
        this.v8 = v8;
    }

    public List<SimpleData> getListData() {
        return listData;
    }

    public void setListData(List<SimpleData> listData) {
        this.listData = listData;
    }

    public SimpleData getInnerData() {
        return innerData;
    }

    public void setInnerData(SimpleData innerData) {
        this.innerData = innerData;
    }
}
