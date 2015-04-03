package com.oliver.moneyassistant.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.oliver.moneyassistant.R;
import com.oliver.moneyassistant.constants.ConstantsForHome;
import com.oliver.moneyassistant.db.models.OutcomeItem;
import com.oliver.moneyassistant.db.utils.MoneyUtils;
import com.oliver.moneyassistant.db.utils.TimeUtils;
import com.oliver.moneyassistant.logic.runnables.ParcelableOutcomeList;
import com.oliver.moneyassistant.ui.utils.MyPieChart;
import com.oliver.moneyassistant.ui.utils.TimelineAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Oliver on 2015/3/17.
 */
public class OutcomeWithinDayFragment extends Fragment implements
        AdapterView.OnItemClickListener{
    private PieChart mPieChart;
    private Handler mHDParcelableOutcome;
    private List<OutcomeItem> mList;
    private ListView mLVOutcomeTimeLine;
    private TextView mTVDayOutcome;
    private View mRootView;
    private String TAG ="OutcomeWithinDayFragment";
    private OutcomeDetailsFragment mOutcomeDetailsFragment;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.outcome_within_day_view,null);
        initVariable();
        return mRootView;
    }


    private void initVariable(){
        mPieChart = (PieChart)mRootView.findViewById(R.id.pc_catagory);
        mHDParcelableOutcome = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == ConstantsForHome.PARSELABLE_OUTCOME_LIST_FLAG){
                    mList = msg.getData()
                            .getParcelableArrayList(ConstantsForHome.PARSELABLE_OUTCOME_LIST);
                    calOutcomeSum();
                    initTimeLine();
                    showPieChart();
                }
            }
        };
        mLVOutcomeTimeLine = (ListView)mRootView.findViewById(R.id.lv_timeline);
        mLVOutcomeTimeLine.setOnItemClickListener(this);
        mTVDayOutcome = (TextView)mRootView.findViewById(R.id.tv_outcome_money_within_day);
    }

    private void calOutcomeSum(){
        float sum = 0f;
        for(OutcomeItem i:mList){
            sum+=i.getOutcomeMoney();
        }
        mTVDayOutcome.setText(MoneyUtils.displayMoney(sum));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle data = new Bundle();
        data.putParcelable(ConstantsForHome.OUTCOME_ITEM_DETAILS,mList.get(position));
        FragmentManager manager = this.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if(mOutcomeDetailsFragment==null){
            mOutcomeDetailsFragment = new OutcomeDetailsFragment();
        }
        mOutcomeDetailsFragment.setArguments(data);
        transaction.replace(R.id.sub_fragment_container,mOutcomeDetailsFragment,
                OutcomeDetailsFragment.FRAGMENT_TAG);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onResume(){
        super.onResume();
        Bundle data =this.getArguments();
        long selectedDay = data.getLong(ConstantsForHome.SELECTED_DAY_LONG,-1L);
        if(selectedDay!=-1) {
            new Thread(new ParcelableOutcomeList(this.getActivity(),
                    mHDParcelableOutcome,new Date(selectedDay))).start();
        }else{
            new Thread(new ParcelableOutcomeList(this.getActivity(),
                    mHDParcelableOutcome)).start();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Toast.makeText(this.getActivity(),"home",Toast.LENGTH_LONG).show();
        switch (item.getItemId()){
            case android.R.id.home:
               // this.getActivity().finish();
                Toast.makeText(this.getActivity(),"home",Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initTimeLine(){
        mLVOutcomeTimeLine.setDividerHeight(0);
        TimelineAdapter timelineAdapter = new TimelineAdapter(this.getActivity(), getTimeLineData());
        mLVOutcomeTimeLine.setAdapter(timelineAdapter);
    }

    private List<Map<String, Object>> getTimeLineData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(OutcomeItem i:mList){
            Log.i(TAG, "outcomeItem:　"+i.toString());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("address",i.getAddress().getAddressHeader());
            map.put("money",MoneyUtils.displayMoney(i.getOutcomeMoney()));

            map.put("type_icon",i.getOutcomeType().getResId());
            map.put("type_text",i.getOutcomeType().getTypeText());
            map.put("time",TimeUtils.getTimeStringWithoutSecond(i.getOutcomeTime()));
            list.add(map);
        }
        return list;
    }

    public void showPieChart(){
        MyPieChart pieChart = new MyPieChart(mPieChart,mList);
        pieChart.initChart();
    }
}
