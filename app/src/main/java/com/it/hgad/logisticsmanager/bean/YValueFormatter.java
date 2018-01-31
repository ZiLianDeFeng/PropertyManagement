package com.it.hgad.logisticsmanager.bean;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

/**
 * Created by Administrator on 2017/5/16.
 */
public class YValueFormatter implements IAxisValueFormatter {

    private List<String> mValues;

    public YValueFormatter() {
    }

    public YValueFormatter(List<String> values) {
        this.mValues = values;
    }

    public void setmValues(List<String> mValues) {
        this.mValues = mValues;
    }

    private static final String TAG = "YValueFormatter";

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if (mValues.size()!=0) {
            return mValues.get((int) value / 10 % mValues.size());
        }
        return null;
    }
}
