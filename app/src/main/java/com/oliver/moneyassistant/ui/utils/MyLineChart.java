package com.oliver.moneyassistant.ui.utils;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import com.oliver.moneyassistant.constants.NumberOffset;
import com.oliver.moneyassistant.wrapperclass.LineCharConfig;
import java.util.ArrayList;

/**
 * Created by Oliver on 2015/3/24.
 */
public class MyLineChart {

    private LineChart mLineChart;
    private LineCharConfig mConfig;
    private static final String TAG ="MyLineChart";

    public MyLineChart(LineChart lineChart,LineCharConfig config){
        this.mLineChart = lineChart;
        this.mConfig = config;
        Log.i(TAG,"mConfig: "+this.mConfig.toString());
    }

    public void initLineChart(){
        // no description text
        mLineChart.setDescription("");
        mLineChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable value highlighting
        mLineChart.setHighlightEnabled(true);

        // enable touch gestures
        mLineChart.setTouchEnabled(true);

        // enable scaling and dragging
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mLineChart.setPinchZoom(true);

        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        // MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

        // set the marker to the chart
        //   mChart.setMarkerView(mv);

        // enable/disable highlight indicators (the lines that indicate the
        // highlighted Entry)
        mLineChart.setHighlightIndicatorEnabled(false);

        // add data
        setData(mConfig);
        mLineChart.animateX(2500);
//	        mChart.setVisibleYRange(30, AxisDependency.LEFT);

//	        // restrain the maximum scale-out factor
//	        mChart.setScaleMinima(3f, 3f);
        //
//	        // center the view to a specific position inside the chart
//	        mChart.centerViewPort(10, 50, AxisDependency.LEFT);

        // get the legend (only possible after setting data)
        Legend l = mLineChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);

        // // dont forget to refresh the drawing
        // mChart.invalidate();
    }
    private void setData(LineCharConfig wrapper) {

        ArrayList<String> xVals = wrapper.mXVals;
        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        int cnt= 0;
        for(ArrayList<Float> lineSet:wrapper.mDataSet){
            ArrayList<Entry> yList = new ArrayList<>();
            int count=0;
            for(Float val:lineSet){
                if(val<NumberOffset.FLOAT_OFFSET)val=0f;
                yList.add(new Entry(val,count++));
            }
            LineDataSet set = new LineDataSet(yList,wrapper.mLineSetName.get(cnt));
            set.setLineWidth(1f);
            set.setCircleSize(3f);
            set.setDrawCircleHole(false);
            set.setValueTextSize(9f);
            set.setFillAlpha(65);
            int color = wrapper.mLineSetColor.get(cnt);
            set.setColor(color);
            set.setCircleColor(color);
            set.setFillColor(color);
            dataSets.add(set);
            cnt++;
        }

        LineData data = new LineData(xVals, dataSets);
//        Log.i(TAG, "wrapper:" + wrapper);
//
//        for(int i=1;i<=wrapper.mOutValueArray.length;i++){
//            xVals.add((i) + "日");
//        }
//
//        ArrayList<Entry> yValsForOut = new ArrayList<>();
//
//        for(int i=0;i<wrapper.mOutValueArray.length;i++){
//            float val = wrapper.mOutValueArray[i];
//            if(val< NumberOffset.FLOAT_OFFSET)val = 0f;
//            yValsForOut.add(new Entry(val, i));
//        }
//
//        ArrayList<Entry> yValsForIn = new ArrayList<>();
//        for(int i=0;i<wrapper.mInValueArray.length;i++){
//            float val =wrapper.mInValueArray[i];
//            if(val<NumberOffset.FLOAT_OFFSET)val= 0f;
//            yValsForIn.add(new Entry(val,i));
//        }
//
//
//
//        LineDataSet set1 = new LineDataSet(yValsForOut, "支出");
//        set1.setColor(Color.RED);
//        set1.setCircleColor(Color.RED);
//        set1.setLineWidth(1f);
//        set1.setCircleSize(3f);
//        set1.setDrawCircleHole(false);
//        set1.setValueTextSize(9f);
//        set1.setFillAlpha(65);
//        set1.setFillColor(Color.RED);
//
//
//        LineDataSet set2 = new LineDataSet(yValsForIn,"收入");
//        set2.setColor(Color.BLUE);
//        set2.setCircleColor(Color.BLUE);
//        set2.setLineWidth(1f);
//        set2.setCircleSize(3f);
//        set2.setDrawCircleHole(true);
//        set2.setValueTextSize(9f);
//        set2.setFillAlpha(65);
//        set2.setFillColor(Color.BLUE);
//
//
//        ArrayList<LineDataSet> dataSets = new ArrayList<>();
//        dataSets.add(set1); // add the datasets
//        dataSets.add(set2);
//        // create a data object with the datasets
//        LineData data = new LineData(xVals, dataSets);

        LimitLine ll1 = new LimitLine(130f, "计划支出");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.POS_RIGHT);
        ll1.setTextSize(10f);

        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.addLimitLine(ll1);
        leftAxis.setStartAtZero(false);
        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.setData(data);
    }
}
