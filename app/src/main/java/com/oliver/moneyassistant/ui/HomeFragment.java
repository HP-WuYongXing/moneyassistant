package com.oliver.moneyassistant.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;
import com.oliver.moneyassistant.constants.ConstantsForHome;
import com.oliver.moneyassistant.db.models.OutcomeItem;
import com.oliver.moneyassistant.ui.utils.MyPieChart;
import com.oliver.moneyassistant.ui.utils.calendar.RobotoCalendarView;
import com.github.mikephil.charting.charts.ScatterChart;
import com.oliver.moneyassistant.R;
import com.oliver.moneyassistant.constants.NumberOffset;
import com.oliver.moneyassistant.db.models.Budget;
import com.oliver.moneyassistant.db.utils.MoneyUtils;


import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import android.graphics.Color;
import java.util.ArrayList;
import android.os.Handler;
import android.content.Intent;

import com.oliver.moneyassistant.db.utils.TimeUtils;
import com.oliver.moneyassistant.logic.runnables.GetBudgetData;
import com.oliver.moneyassistant.logic.runnables.GetOutcomeSumWithinToday;
import com.oliver.moneyassistant.logic.runnables.IncomeSumWithinDay;
import com.oliver.moneyassistant.logic.runnables.SumListWithinMonth;
import com.oliver.moneyassistant.ui.utils.MyLineChart;
import com.oliver.moneyassistant.ui.utils.MyScatterChart;
import com.oliver.moneyassistant.ui.utils.NumberProgressBar;
import com.oliver.moneyassistant.wrapperclass.LineCharConfig;
import com.oliver.moneyassistant.wrapperclass.ScatterChartConfig;

import android.view.ViewGroup.LayoutParams;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Oliver on 2015/3/12.
 */
public class HomeFragment extends Fragment
        implements View.OnClickListener,
        ViewPager.OnPageChangeListener {

    private static final String TAG = "HomeFragment";
    private View mRootView;
    private LayoutInflater mInflater;
    private TextView mTvIncomeSumOfDay;
    private TextView mTvOutcomeSumOfDay;
    private TextView mTvBudgetMoney;
    private LineChart mLineChart;
    private ScatterChart mScatterChart;
    private PieChart mPieChart;
    private LinearLayout mLLCheckOutcome;

    private float [] mOutcomeSums;
    private float []mIncomeSums;
    private LinearLayout mLLNoTodayBudget;
    private LinearLayout mLLShowBudgetMoney;
    private LinearLayout mLLCheckIncome;
    private LinearLayout mLLNoBudgetPanel;
    private LinearLayout mLLShowBudgetPanel;
    private TextView mTVShowFamousWords;
    private TextView mTVCheckBudget;
    private NumberProgressBar mPbBudgetProgress;
    private TextView mTVBudgetFromDay;
    private TextView mTVBudgetToDay;
    private ViewPager mVPChartAndCalendar;
    private ImageView[]mPagerTips;
    private View[]mPagers;
    private ViewGroup mVGPagerIndicators;
    private static final int PAGER_LENGTH=3;
    private RobotoCalendarView mCalendar;
    private int mCurrentMonthIndex=0;
    private List<OutcomeItem> mOutcomeItemList;
    private TextView mTVTodayIncomeIntr;
    private TextView mTVTodayOutcomeIntr;
    public long mSelectedDay=-1L;
    private long mCurrentDay;
    private static final String TODAY_INTR="今日";
    private static final String COMSUME="消费";
    private static final String OBTAIN="收入";


    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView=inflater.inflate(R.layout.home_view, null);
        mInflater = inflater;
        initVariable();
        initViewPager();
        updateData();
        return mRootView;
    }

    private void initViewPager(){

        mPagers = new View[PAGER_LENGTH];
        mVPChartAndCalendar = (ViewPager)mRootView.findViewById(R.id.vp_calendar_chart_card);
        mVGPagerIndicators =(ViewGroup)mRootView.findViewById(R.id.pager_indicator_view_group);
        View lineChartView = mInflater.inflate(R.layout.home_chart_pager_item_cell,null);
        mLineChart = (LineChart)lineChartView.findViewById(R.id.lc_expenditure_line_chart);
        mScatterChart = (ScatterChart)lineChartView.findViewById(R.id.sc_expenditure_scatter_chart);
        mPagers[2] = lineChartView;

        View calendarView = mInflater.inflate(R.layout.home_calendar_pager_item_cell,null);
        mCalendar = (RobotoCalendarView)calendarView.findViewById(R.id.cv_calendar);
        initCalendar();


        mPagers[0] = calendarView;
        View pieChartView = mInflater.inflate(R.layout.home_pie_chart_pager_item_cell,null);
        mPieChart = (PieChart)pieChartView.findViewById(R.id.pc_outcome_within_month_catagory);

        mPagers[1] = pieChartView;
        mPagerTips = new ImageView[PAGER_LENGTH];
        for(int i=0; i<mPagerTips.length; i++){
            ImageView imageView = new ImageView(this.getActivity());
            imageView.setLayoutParams(new LayoutParams(10,10));
            mPagerTips[i] = imageView;
            if(i == 0){
                mPagerTips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            }else{
                mPagerTips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
            mVGPagerIndicators.addView(imageView);
        }

        mVPChartAndCalendar.setAdapter(new PagerViewAdapter());
        mVPChartAndCalendar.setOnPageChangeListener(this);
        mVPChartAndCalendar.setCurrentItem(0);
    }

    private void showPieChart(){
        MyPieChart pieChart = new MyPieChart(mPieChart,mOutcomeItemList);
        pieChart.mRotate= false;
        pieChart.initChart();
    }



    private void initCalendar(){
        // Initialize the RobotoCalendarPicker with the current index and date
        Calendar calendar  = Calendar.getInstance(Locale.getDefault());
        // Mark current day
        mCalendar.markDayAsCurrentDay(calendar.getTime());
        mCalendar.setRobotoCalendarListener(new RobotoCalendarView.RobotoCalendarListener(){
            @Override
            public void onDateSelected(Date date) {
                mCalendar.markDayAsSelectedDay(date);
                mSelectedDay = date.getTime();
                setDayIntr();
            }

            @Override
            public void onRightButtonClick() {
                mCurrentMonthIndex++;
                updateCalendar();
            }

            @Override
            public void onLeftButtonClick() {
                mCurrentMonthIndex--;
                updateCalendar();
            }
        });
    }

    private void updateCalendar() {
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
        currentCalendar.add(Calendar.MONTH, mCurrentMonthIndex);
        mCalendar.initializeCalendar(currentCalendar);
        if(mCurrentMonthIndex==0){
            mCalendar.markDayAsCurrentDay(new Date());
        }
    }

    private void initVariable(){
        mLLNoBudgetPanel = (LinearLayout)mRootView.findViewById(R.id.ll_no_budget_panel);
        mLLShowBudgetPanel = (LinearLayout)mRootView.findViewById(R.id.ll_show_budget_panel);
        mTVShowFamousWords = (TextView)mRootView.findViewById(R.id.tv_show_famous_words);
        mTVCheckBudget = (TextView)mRootView.findViewById(R.id.tv_check_budget);
        mTVTodayIncomeIntr =(TextView)mRootView.findViewById(R.id.tv_intr_today_income);
        mTVTodayOutcomeIntr = (TextView)mRootView.findViewById(R.id.tv_intr_today_outcome);

        mTvOutcomeSumOfDay = (TextView)mRootView.findViewById(R.id.tv_today_outcome);
        mTvIncomeSumOfDay = (TextView)mRootView.findViewById(R.id.tv_today_income_money);
        mLLCheckOutcome = (LinearLayout)mRootView.findViewById(R.id.ll_check_outcome);
       // mLineChart =(LineChart)mRootView.findViewById(R.id.lc_expenditure_line_chart);
        mLLNoTodayBudget = (LinearLayout)mRootView.findViewById(R.id.ll_no_today_budget);
        mLLNoTodayBudget.setOnClickListener(this);
        mLLCheckOutcome.setOnClickListener(this);


     //   mScatterChart = (ScatterChart)mRootView.findViewById(R.id.sc_expenditure_scatter_chart);
        mTvBudgetMoney = (TextView)mRootView.findViewById(R.id.tv_budget_money);


        mLLShowBudgetMoney = (LinearLayout)mRootView.findViewById(R.id.ll_show_budget_money);
        mLLShowBudgetMoney.setOnClickListener(this);

        mLLCheckIncome = (LinearLayout)mRootView.findViewById(R.id.ll_check_today_income);
        mLLCheckIncome.setOnClickListener(this);

        mTVBudgetFromDay = (TextView)mRootView.findViewById(R.id.tv_budget_from_day);
        mTVBudgetToDay = (TextView)mRootView.findViewById(R.id.tv_budget_to_day);

        mPbBudgetProgress = (NumberProgressBar)mRootView.findViewById(R.id.pb_show_budget_progress);

        mCurrentDay =TimeUtils.getYearMonthDayLong(new Date().getTime());
        mSelectedDay = mCurrentDay;

        mPbBudgetProgress.setMax(100);
        mPbBudgetProgress.setProgress(0);
        mSelectedDay = new Date().getTime();
    }


    public void setDayIntr(){
        if(TimeUtils.getYearMonthDayLong(mSelectedDay) ==mCurrentDay){
            mTVTodayOutcomeIntr.setText(TODAY_INTR+COMSUME);
            mTVTodayIncomeIntr.setText(TODAY_INTR+OBTAIN);
        }else{
            mTVTodayOutcomeIntr.setText(TimeUtils.getMonthAndDaySimplified(mSelectedDay)+COMSUME);
            mTVTodayIncomeIntr.setText(TimeUtils.getMonthAndDaySimplified(mSelectedDay)+OBTAIN);
        }
        updateOutcomeIncome();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_check_outcome:checkTodayOutcome();break;
            case R.id.ll_check_today_income:checkIncome();break;
            case R.id.ll_no_today_budget:addBudget();break;
            case R.id.ll_show_budget_money:showBudgetDetails();break;
        }
    }

    private void showBudgetDetails(){
       Intent intent = new Intent();
       intent.putExtra(ConstantsForHome.CHOOSE_FRAGMENT_FLAG,
                ConstantsForHome.SHOW_BUDGET_DETAIL_FRAGMENT);
       intent.setClass(HomeFragment.this.getActivity(),HomeSubActivity.class);
       startActivity(intent);

    }

    private void checkIncome(){
        Intent intent = new Intent();
        intent.putExtra(ConstantsForHome.CHOOSE_FRAGMENT_FLAG,
                ConstantsForHome.INCOME_WITHIN_DAY_FRAGMENT);
        intent.putExtra(ConstantsForHome.SELECTED_DAY_LONG,mSelectedDay);
        intent.setClass(HomeFragment.this.getActivity(),HomeSubActivity.class);
        startActivity(intent);
    }

    private void addBudget(){
        Intent intent = new Intent();
        intent.putExtra(ConstantsForHome.CHOOSE_FRAGMENT_FLAG,
                ConstantsForHome.ADD_BUDGET_ITEM_FRAGMENT);
        intent.setClass(HomeFragment.this.getActivity(),HomeSubActivity.class);
        startActivity(intent);
    }

    private void checkTodayOutcome(){
        Intent intent = new Intent();
        intent.putExtra(ConstantsForHome.CHOOSE_FRAGMENT_FLAG,
                ConstantsForHome.OUTCOME_WITHIN_DAY_FRAGMENT);
        intent.putExtra(ConstantsForHome.SELECTED_DAY_LONG,mSelectedDay);
        intent.setClass(HomeFragment.this.getActivity(),HomeSubActivity.class);
        startActivity(intent);
    }

    private void addOutcomeItem(){
        Intent intent = new Intent();
        intent.putExtra(ConstantsForHome.CHOOSE_FRAGMENT_FLAG,
                ConstantsForHome.ADD_OUTCOME_ITEM_FRAGMENT);
        intent.setClass(HomeFragment.this.getActivity(),HomeSubActivity.class);
        startActivity(intent);
    }

    private void addIncomeItem(){
        Intent intent = new Intent();
        intent.putExtra(ConstantsForHome.CHOOSE_FRAGMENT_FLAG,
                ConstantsForHome.ADD_INCOME_ITEM_FRAGMENT);
        intent.setClass(HomeFragment.this.getActivity(),HomeSubActivity.class);
        startActivity(intent);
    }


    private  Handler mHdGetSum =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ConstantsForHome.OUTCOME_HANDLER_FLAG:
                    float outcomeSum = msg.getData().getFloat(ConstantsForHome.OUTCOME_SUM);
                    mTvOutcomeSumOfDay.setText(MoneyUtils.displayMoney(outcomeSum));
                    /*查询预算*/
                    new Thread(new GetBudgetData(HomeFragment.this.getActivity(),this)).start();
                    break;
                case ConstantsForHome.INCOME_HANDLER_FLAG:
                    float incomeSum = msg.getData().getFloat(ConstantsForHome.INCOME_SUM);
                    mTvIncomeSumOfDay.setText(MoneyUtils.displayMoney(incomeSum));
                    break;
                case ConstantsForHome.BUDGET_HANDLER_FLAG:
                    Budget b = msg.getData().getParcelable(ConstantsForHome.BUDGET_TODAY);

                    showBudget(b);
            }
        }
    };


    private  Handler mHdGetSumList =  new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what== ConstantsForHome.SUM_LIST_FLAG){
                mOutcomeSums = msg.getData().getFloatArray(ConstantsForHome.OUTCOME_SUM_ARRAY);
                mIncomeSums = msg.getData().getFloatArray(ConstantsForHome.INCOME_SUM_ARRAY);
                mOutcomeItemList =msg.getData().getParcelableArrayList(
                        ConstantsForHome.OUTCOME_ITEM_WITH_MONTH);
                showChart();
                showPieChart();
            }
        }
    };


    private void showChart(){
        Log.i(TAG,Arrays.toString(mOutcomeSums));
        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;

        for(int i=0;i< mOutcomeSums.length;i++){
            if(min> mOutcomeSums[i]){
                min = mOutcomeSums[i];
            }
            if(max< mOutcomeSums[i]){
                max = mOutcomeSums[i];
            }
            Log.i(TAG,"size -->"+mIncomeSums.length);
            Log.i(TAG,"in for max: "+max);
        }

        for(int i=0;i< mIncomeSums.length;i++){
            if(min> mIncomeSums[i]){
                min = mIncomeSums[i];
            }
            if(max< mIncomeSums[i]){
                max = mIncomeSums[i];
            }
            Log.i(TAG,"size -->"+mIncomeSums.length);
            Log.i(TAG,"in for max: "+max);
        }

        if(max<NumberOffset.FLOAT_OFFSET)return;

        if(mIncomeSums.length<4&&mOutcomeSums.length>0){

            showScatterChart(
                    max,
                    min,
                    1,
                    mOutcomeSums.length,
                    mOutcomeSums,
                    mIncomeSums);
        }else{

            showLineChart(
                    max,
                    min,
                    1,
                    mOutcomeSums.length,
                    mOutcomeSums,
                    mIncomeSums);

        }

        // mIncomeSums[0]=100.0f;

    }

    private void showLineChart(float maxVOnY, float minVOnY,int maxVOnX, int minVOnX,
                               float[] outArray,float[]inArray){
        LineCharConfig config = new LineCharConfig();
        ArrayList<ArrayList<Float>> dataSet = new ArrayList<>();
        ArrayList<Float> lineSetOut = new ArrayList<>();
        ArrayList<String> lineSetName = new ArrayList<>();
        ArrayList<Integer> lineSetColor = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();

        for(int i=1;i<=outArray.length;i++){
            xVals.add(i+"日");
        }

        for(int i=0;i<outArray.length;i++){
            lineSetOut.add(outArray[i]);
        }
        lineSetName.add("支出");
        lineSetColor.add(Color.RED);

        ArrayList<Float> lineSetIn = new ArrayList<>();
        for(int i=0;i<inArray.length;i++){
            lineSetIn.add(inArray[i]);
        }
        lineSetName.add("收入");
        lineSetColor.add(Color.BLUE);

        dataSet.add(lineSetOut);
        dataSet.add(lineSetIn);
        config.mXVals = xVals;
        config.mDataSet = dataSet;
        config.mLineSetName = lineSetName;
        config.mLineSetColor = lineSetColor;
        mLineChart.setVisibility(View.VISIBLE);
        mScatterChart.setVisibility(View.GONE);
        MyLineChart lineChart = new MyLineChart(mLineChart,config);
        lineChart.initLineChart();
        //initLineChart(config);
    }

    private void showScatterChart(float maxVOnY, float minVOnY,
                                  int maxVOnX, int minVOnX,
                                  float[] outArray,float[]inArray){

        ScatterChartConfig config= new ScatterChartConfig();
        ArrayList<ArrayList<Float>> dataSet = new ArrayList<>();
        ArrayList<Float> lineSetOut = new ArrayList<>();
        ArrayList<String> lineSetName = new ArrayList<>();
        ArrayList<Integer> lineSetColor = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();

        for(int i=1;i<=outArray.length;i++){
            xVals.add(i+"日");
        }

        for(int i=0;i<outArray.length;i++){
            lineSetOut.add(outArray[i]);
        }
        lineSetName.add("支出");
        lineSetColor.add(Color.RED);

        ArrayList<Float> lineSetIn = new ArrayList<>();
        for(int i=0;i<inArray.length;i++){
            lineSetIn.add(inArray[i]);
        }
        lineSetName.add("收入");
        lineSetColor.add(Color.BLUE);

        dataSet.add(lineSetOut);
        dataSet.add(lineSetIn);
        config.mXVals = xVals;
        config.mDataSet = dataSet;
        config.mLineSetName = lineSetName;
        config.mLineSetColor = lineSetColor;

        mLineChart.setVisibility(View.GONE);
        mScatterChart.setVisibility(View.VISIBLE);
        MyScatterChart scatterChart = new MyScatterChart(mScatterChart,config);
        scatterChart.initScatterChart();
    }

    private void showBudget(Budget budget){//bug:当没有预算的时候，无数据
        if(budget==null){
            mLLShowBudgetPanel.setVisibility(View.GONE);
            mLLNoBudgetPanel.setVisibility(View.VISIBLE);
             return;
        }
        float budgetmoney =
        budget.getMoney()/ TimeUtils.getDaysBetweenTime(budget.getStartTime(),budget.getEndTime());
        mTvBudgetMoney.setText(MoneyUtils.displayMoney(budgetmoney));

        mLLShowBudgetPanel.setVisibility(View.VISIBLE);
        mLLNoBudgetPanel.setVisibility(View.GONE);

        /*计算progress的值*/
        String from = TimeUtils.getYearMonthAndDay(budget.getStartTime());
        Log.i(TAG,"from:  "+from);

        String to = TimeUtils.getYearMonthAndDay(budget.getEndTime());
        Log.i(TAG,"to:  "+to);


        mTVBudgetFromDay.setText(from);
        mTVBudgetToDay.setText(to);

        long field = budget.getEndTime()-budget.getStartTime();
        long progress = 100*(TimeUtils.getNow()-budget.getStartTime())/field;
        int value = (int)progress;
        mPbBudgetProgress.setProgress(value);
        String showText = TimeUtils.getYearMonthAndDay(TimeUtils.getNow());
        mPbBudgetProgress.setShowText(showText);
    }


    private void updateData(){

        new Thread(new GetOutcomeSumWithinToday(this.getActivity(),mHdGetSum)).start();
        new Thread(new IncomeSumWithinDay(this.getActivity(),mHdGetSum)).start();
        new Thread(new SumListWithinMonth(this.getActivity(),mHdGetSumList)).start();

    }

    private void updateOutcomeIncome(){
        new Thread(new GetOutcomeSumWithinToday(this.getActivity(),mHdGetSum,new Date(mSelectedDay))).start();
        new Thread(new IncomeSumWithinDay(this.getActivity(),mHdGetSum,new Date(mSelectedDay))).start();
    }



    @Override
    public void onResume() {
        super.onResume();
        checkNewOutcomeItem();
    }

    /*is there any new outcome items*/
    private void checkNewOutcomeItem(){
        SharedPreferences preferences =  this.getActivity()
                .getSharedPreferences(ConstantsForHome.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
        boolean update= preferences.getBoolean(ConstantsForHome.SHARED_UPDATE_KEY,false);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ConstantsForHome.SHARED_UPDATE_KEY,false);
        editor.commit();
        if(update){
            updateData();
        }
    }


    /*view pager 接口的方法*/

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setImageBackground(position%mPagerTips.length);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setImageBackground(int selectedItem){
        for(int i=0; i<mPagerTips.length; i++){
            if(i == selectedItem){
                mPagerTips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            }else{
                mPagerTips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
        }
    }

    private class PagerViewAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return mPagerTips.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager)container).addView(mPagers[position % mPagers.length],0);
            return mPagers[position%mPagers.length];
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager)container).removeView(mPagers[position % PAGER_LENGTH]);
        }
    }
}
