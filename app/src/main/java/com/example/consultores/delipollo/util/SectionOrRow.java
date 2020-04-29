package com.example.consultores.delipollo.util;

/**
 * Created by consultores on 21/08/2019.
 */

public class SectionOrRow {
    private comprasProd obj;
    private String section;
    private boolean isRow;

    public static SectionOrRow createRow( comprasProd row) {
        SectionOrRow ret = new SectionOrRow();
        ret.obj = row;
        ret.isRow = true;
        return ret;
    }

    public static SectionOrRow createSection(String section) {
        SectionOrRow ret = new SectionOrRow();
        ret.section = section;
        ret.isRow = false;
        return ret;
    }

    public comprasProd getRow() {
        return obj;
    }

    public String getSection() {
        return section;
    }

    public boolean isRow() {
        return isRow;
    }
}
