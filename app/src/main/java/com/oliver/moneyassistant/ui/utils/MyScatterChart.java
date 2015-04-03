package com.oliver.moneyassistant.ui.utils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.oliver.moneyassistant.wrapperclass.ScatterChartConfig;

import java.util.ArrayList;

/**
 * Created by Oliver on 2015/3/24.
 */
public class MyScatterChart {
    private ScatterChart mScatterChart;
    private ScatterChartConfig mConfig;

    public MyScatterChart(ScatterChart scatterChart, ScatterChartConfig config){
        this.mScatterChart = scatterChart;
        this.mConfig = config;
    }

    public void initScatterChart(){
        mScatterChart.setDrawGridBackground(false);
        mScatterChart.setTouchEnabled(true);
        mScatterChart.setHighlightEnabled(true);

        // enable scaling and dragging
        mScatterChart.setDragEnabled(true);
        mScatterChart.setScaleEnabled(true);

        mScatterChart.setMaxVisibleValueCount(200);
        mScatterChart.setPinchZoom(true);


        YAxis yAxis = mScatterChart.getAxisLeft();
        XAxis xAxis = mScatterChart.getXAxis();
        mScatterChart.getAxisRight().setEnabled(false);
        xAxis.setDrawAxisLine(false);
        ArrayList<String> xVals = mConfig.mXVals;
        ScatterData data = new ScatterData(xVals);
        int cnt=0;
        for(ArrayList<Float> yVals:mConfig.mDataSet){
            ArrayList<Entry> scatterVals = new ArrayList<>();
            int len= yVals.size();
            for(int i=0;i<len;i++){
                scatterVals.add(new Entry(yVals.get(i),i));
            }
            ScatterDataSet dataSet =
                    new ScatterDataSet(scatterVals,mConfig.mLineSetName.get(cnt));
            dataSet.setScatterShape(ScatterChart.ScatterShape.TRIANGLE);
            dataSet.setScatterShapeSize(8f);
            dataSet.setColor(mConfig.mLineSetColor.get(cnt));
            data.addDataSet(dataSet);
            cnt++;
        }
        mScatterChart.setData(data);
        mScatterChart.invalidate();

//        ArrayList<Entry> yValsForOut = new ArrayList<>();
//        for(int i=0;i<mConfig.mOutValueArray.length;i++){
//            float val = mConfig.mOutValueArray[i];
//            yValsForOut.add(new Entry(val,i));
//        }
//
//        ArrayList<Entry> yValsForIn = new ArrayList<>();
//        for(int i=0;i<mConfig.mInValueArray.length;i++){
//            float val = mConfig.mInValueArray[i];
//            yValsForIn.add(new Entry(val,i));
//        }
//
//        ScatterDataSet dataSetForOut = new ScatterDataSet(yValsForOut,"支出");
//        dataSetForOut.setScatterShape(ScatterChart.ScatterShape.TRIANGLE);
//        dataSetForOut.setScatterShapeSize(8f);
//        dataSetForOut.setColor(Color.RED);
//
//
//        ScatterDataSet dataSetForIn = new ScatterDataSet(yValsForIn,"收入");
//        dataSetForIn.setScatterShape(ScatterChart.ScatterShape.SQUARE);
//        dataSetForIn.setScatterShapeSize(6f);
//        dataSetForIn.setColor(Color.BLUE);
//
//
//        ScatterData data = new ScatterData(xVals);
//        data.addDataSet(dataSetForIn);
//        data.addDataSet(dataSetForOut);
//
//        mScatterChart.setData(data);
//        mScatterChart.invalidate();
//
//        mScatterChart.setData(data);
//        mScatterChart.invalidate();

        Legend l= mScatterChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
    }
}
