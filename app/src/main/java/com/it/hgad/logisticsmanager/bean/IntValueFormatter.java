package com.it.hgad.logisticsmanager.bean;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

/**
 * Created by Administrator on 2017/5/17.
 */
public class IntValueFormatter implements IValueFormatter {

    private List<String> mValues;

    public IntValueFormatter() {
    }

    public IntValueFormatter(List<String> mValues) {
        this.mValues = mValues;
    }

    public void setmValues(List<String> mValues) {
        this.mValues = mValues;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        if (mValues.size()!=0) {
            return mValues.get((int) value / 10 % mValues.size());
        }
        return null;
    }
}
