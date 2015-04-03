package com.oliver.moneyassistant.wrapperclass;

import com.oliver.moneyassistant.constants.ChartSelect;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Oliver on 2015/3/24.
 */
public class ScatterChartConfig {
    public ArrayList<ArrayList<Float>> mDataSet;
    public ArrayList<String>mLineSetName;
    public ArrayList<Integer>mLineSetColor;
    public ArrayList<String>mXVals;

    public ScatterChartConfig(){}
    public ScatterChartConfig(ArrayList<ArrayList<Float>>dataset,
                          ArrayList<String>lineSetName,
                          ArrayList<Integer>lineSetColor,
                          ArrayList<String>xVals) {
        this.mDataSet = dataset;
        this.mLineSetName = lineSetName;
        this.mLineSetColor = lineSetColor;
        this.mXVals = xVals;
    }

    @Override
    public String toString() {
        return "ScatterChartConfig{" +
                ", mDataSet=" + mDataSet +
                ", mLineSetName=" + mLineSetName +
                ", mLineSetColor=" + mLineSetColor +
                ", mXVals=" + mXVals +
                '}';
    }
}
