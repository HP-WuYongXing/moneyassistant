package com.oliver.moneyassistant.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.oliver.moneyassistant.R;
import com.oliver.moneyassistant.constants.ConstantsForHome;
import com.oliver.moneyassistant.db.models.Budget;
import com.oliver.moneyassistant.db.utils.TimeUtils;
import com.oliver.moneyassistant.logic.runnables.GetBudgetList;
import com.oliver.moneyassistant.logic.runnables.GetOutcomeListWithBudget;
import com.oliver.moneyassistant.ui.utils.BudgetDetailsListAdapter;
import com.oliver.moneyassistant.ui.utils.MyLineChart;
import com.oliver.moneyassistant.ui.utils.MyScatterChart;
import com.oliver.moneyassistant.wrapperclass.DayWrapper;

import com.oliver.moneyassistant.wrapperclass.LineCharConfig;
import com.oliver.moneyassistant.wrapperclass.ScatterChartConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oliver on 2015/3/23.
 */

public class ShowBudgetDetailsFragment extends Fragment{

    public static final String FRAGMENT_TAG ="ShowBudgetDetailsFragment";
    private View mRootView;
    private ListView mLVBudgetDetails;
    private BudgetDetailsListAdapter mDetailsListAdapter;
    private List<Budget> mBudgetList;
    private Budget mSelectedBudget;
    private LineChart mLineChart;
    private ScatterChart mScatterChart;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.show_budget_details,null);
        initVariable();
        return mRootView;
    }

    private Handler mGetBudgetListHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ConstantsForHome.BUDGET_LIST_HANDLER_FLAG:
                    setData(msg.getData());
                    break;
                case ConstantsForHome.OUTCOME_LIST_WITHIN_BUDGET_HANDLER_FLAG:
                    float[] sums = msg.getData().getFloatArray(
                            ConstantsForHome.OUTCOME_SUM_LIST_WITHIN_BUDGET);
                    showChart(sums);
                    break;
            }
        }
    };

    private void getChartData(){
        new Thread(new GetOutcomeListWithBudget(this.getActivity(),
                mGetBudgetListHandler,
                mSelectedBudget)).start();
    }

    private void showChart(float[] sums){
        int len = sums.length;
        if(len<5&&len>0){
            mLineChart.setVisibility(View.GONE);
            mScatterChart.setVisibility(View.VISIBLE);
            showScatterChart(sums);
        }else{
            showLineChart(sums);
            mLineChart.setVisibility(View.VISIBLE);
            mScatterChart.setVisibility(View.GONE);
        }
    }

    private void showScatterChart(float [] sums){
        ScatterChartConfig config = new ScatterChartConfig();
        config.mXVals = getXValues();
        ArrayList<String>lineName = new ArrayList<>();
        lineName.add("支出");
        config.mLineSetName = lineName;
        ArrayList<Integer> color = new ArrayList<>();
        color.add(Color.RED);
        config.mLineSetColor = color;
        ArrayList<Float> line1 = new ArrayList<>();
        for(int i=0;i<sums.length;i++){
            line1.add(sums[i]);
        }
        ArrayList<ArrayList<Float>>dataset = new ArrayList<>();
        dataset.add(line1);
        config.mDataSet = dataset;
        MyScatterChart scatterChart = new MyScatterChart(mScatterChart,config);
        scatterChart.initScatterChart();
    }

    private ArrayList<String> getXValues(){
       ArrayList<DayWrapper> list =  TimeUtils.getDayWrapperWithBudget(mSelectedBudget);
       ArrayList<String> xVals = new ArrayList<>();
       for(DayWrapper wrapper:list){
           long l = (wrapper.mFirstTime + wrapper.mLastTime)/2;
           String str = TimeUtils.getYearMonthAndDay(l);
           xVals.add(str.substring(5));
       }
       return xVals;
    }

    private void showLineChart(float [] sums){
        LineCharConfig config = new LineCharConfig();
        config.mXVals = getXValues();
        ArrayList<String>lineName = new ArrayList<>();
        lineName.add("支出");
        config.mLineSetName = lineName;
        ArrayList<Integer> color = new ArrayList<>();
        color.add(Color.RED);
        config.mLineSetColor = color;
        ArrayList<Float> line1 = new ArrayList<>();
        for(int i=0;i<sums.length;i++){
            line1.add(sums[i]);
        }
        ArrayList<ArrayList<Float>>dataset = new ArrayList<>();
        dataset.add(line1);
        config.mDataSet = dataset;
        MyLineChart lineChart = new MyLineChart(mLineChart,config);
        lineChart.initLineChart();
    }

    private void setData(Bundle data){
        List<Budget> list= data.getParcelableArrayList(ConstantsForHome.BUDGET_TODAY_LIST);
        if(list.size()==0)return;

        for(Budget b:list){
            mBudgetList.add(b);
        }
        mSelectedBudget = list.get(0);
        mDetailsListAdapter.notifyDataSetChanged();
        getChartData();
    }

    private void initVariable(){
        mLVBudgetDetails = (ListView)mRootView.findViewById(R.id.lv_show_budget_list);
        mBudgetList = new ArrayList<>();
        mDetailsListAdapter = new BudgetDetailsListAdapter(mBudgetList);
        mLVBudgetDetails.setAdapter(mDetailsListAdapter);
        mLineChart = (LineChart)mRootView.findViewById(R.id.lc_budget_details_chart);
        mScatterChart = (ScatterChart)mRootView.findViewById(R.id.sc_budget_details_chart);
    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread(new GetBudgetList(this.getActivity(),mGetBudgetListHandler)).start();

    }
}
