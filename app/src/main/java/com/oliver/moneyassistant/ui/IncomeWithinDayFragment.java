package com.oliver.moneyassistant.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.oliver.moneyassistant.R;
import com.oliver.moneyassistant.constants.ConstantsForHome;
import com.oliver.moneyassistant.db.models.IncomeItem;
import com.oliver.moneyassistant.logic.runnables.GetIncomeItemList;
import com.oliver.moneyassistant.ui.utils.IncomeListViewAdapter;

import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.util.Log;

/**
 * Created by Oliver on 2015/3/21.
 */
public class IncomeWithinDayFragment extends Fragment implements AdapterView.OnItemClickListener{

    private String TAG="IncomeWithinDayFragment";
    private View mRootView;
    private ListView mLVIncomeList;
    private List<IncomeItem> mIncomeItemList;
    private IncomeListViewAdapter mIncomeListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.income_within_day_view,null);
        initVariable();
        return mRootView;
    }

    private void initVariable(){
        mLVIncomeList = (ListView)mRootView.findViewById(R.id.lv_income_within_day_list);
        mLVIncomeList.setOnItemClickListener(this);
        mIncomeItemList = new ArrayList<IncomeItem>();

        mIncomeListAdapter = new IncomeListViewAdapter(this.getActivity(),mIncomeItemList);
        mLVIncomeList.setAdapter(mIncomeListAdapter);

        Bundle data= this.getArguments();
        long selected = data.getLong(ConstantsForHome.SELECTED_DAY_LONG,-1L);
        if(selected!=-1L){
            new Thread(
                    new GetIncomeItemList(this.getActivity(),
                    mHDGetIncomeList,new Date(selected))).start();
        }else{
            new Thread(new GetIncomeItemList(this.getActivity(),mHDGetIncomeList)).start();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private Handler mHDGetIncomeList = new Handler(){

        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case ConstantsForHome.INCOME_LIST_HANDLER_FLAG:
                   Log.i(TAG,"handle message... ...");
                   Bundle data = msg.getData();
                   List<IncomeItem> list = data.getParcelableArrayList(ConstantsForHome.INCOME_WITHIN_DAY_LIST);
                   Log.i(TAG,"mIncomeItemList.size():　"+mIncomeItemList.toString());
                   addIncomeItem(list);
           }
        }
    };

    private void addIncomeItem(List<IncomeItem> list){
        mIncomeItemList.clear();
        for(IncomeItem i:list){
            mIncomeItemList.add(i);
        }
        mIncomeListAdapter.notifyDataSetChanged();
    }

}
