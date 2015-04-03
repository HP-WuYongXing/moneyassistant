package com.oliver.moneyassistant.ui.utils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.oliver.moneyassistant.db.models.OutcomeItem;
import com.oliver.moneyassistant.db.utils.TypeConvertor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Oliver on 2015/3/26.
 */
public class MyPieChart {
    private PieChart mPieChart;
    private List<OutcomeItem> mOutcomeList;
    public boolean mRotate=true;
    public MyPieChart(PieChart pieChart,List<OutcomeItem> list){
        this.mPieChart = pieChart;
        mOutcomeList = list;
    }

    public void initChart(){
        mPieChart.setUsePercentValues(true);
        mPieChart.setHoleColor(Color.WHITE);
        mPieChart.setHoleRadius(60f);
        mPieChart.setDrawCenterText(true);
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setRotationAngle(0);
        mPieChart.setRotationEnabled(mRotate);
        // mPieChart.setOnChartValueSelectedListener();
        mPieChart.setCenterText("消费比例");
        setData();
        mPieChart.animateXY(1500,1500);
        Legend l = mPieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
    }

    private void setData() {
        Map<Integer,Float> typeMap = new HashMap<>();
        for(OutcomeItem i:mOutcomeList){
            int key = i.getTypeId();
            if(typeMap.containsKey(key)){
                Float sum = typeMap.get(key);
                sum =sum+i.getOutcomeMoney();
                typeMap.put(key,sum);
            }else{
                typeMap.put(key,i.getOutcomeMoney());
            }
        }

        Iterator<Integer> it  = typeMap.keySet().iterator();
        int counter = 0;
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
        while(it.hasNext()){
            Integer key = it.next();
            xVals.add(TypeConvertor.getOutcomeTypeString(key));
            yVals1.add(new Entry(typeMap.get(key),counter));
            counter++;
        }

        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3f);

        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mPieChart.setData(data);
        // undo all highlights
        mPieChart.highlightValues(null);
        mPieChart.invalidate();
    }

}
