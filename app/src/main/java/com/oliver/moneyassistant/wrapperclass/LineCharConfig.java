package com.oliver.moneyassistant.wrapperclass;

import android.os.Bundle;

import com.oliver.moneyassistant.constants.ChartSelect;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.ArrayList;
/**
 * Created by Oliver on 2015/3/14.
 */

public class LineCharConfig {
    public ArrayList<ArrayList<Float>> mDataSet;
    public ArrayList<String>mLineSetName;
    public ArrayList<Integer>mLineSetColor;
    public ArrayList<String>mXVals;

    public LineCharConfig(){}

    public LineCharConfig(ArrayList<ArrayList<Float>>dataSet,
                          ArrayList<String>lineSetName,
                          ArrayList<Integer>lineSetColor,
                          ArrayList<String>xVals) {
        this.mDataSet = dataSet;
        this.mLineSetName = lineSetName;
        this.mLineSetColor = lineSetColor;
        this.mXVals = xVals;
    }

    @Override
    public String toString() {
        return "LineCharConfig{" +
                "mDataSet=" + mDataSet +
                ", mLineSetName=" + mLineSetName +
                ", mLineSetColor=" + mLineSetColor +
                ", mXVals=" + mXVals +
                '}';
    }
}
