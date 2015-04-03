package com.oliver.moneyassistant.logic.runnables;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.oliver.moneyassistant.constants.ConstantsForHome;
import com.oliver.moneyassistant.db.dao.OutcomeDao;
import com.oliver.moneyassistant.db.models.Budget;
import com.oliver.moneyassistant.db.utils.TimeUtils;
import com.oliver.moneyassistant.wrapperclass.DayWrapper;

import java.util.List;


/**
 * Created by Oliver on 2015/3/24.
 */
public class GetOutcomeListWithBudget implements Runnable{
    private Context mContext;
    private Handler mHandler;
    private Budget mBudget;
    private String TAG ="GetOutcomeListWithBudget";

    public GetOutcomeListWithBudget(Context context,Handler handler,Budget budget){
        this.mContext = context;
        this.mHandler = handler;
        this.mBudget = budget;
    }

    @Override
    public void run() {
        List<DayWrapper> dayList = TimeUtils.getDayWrapperWithBudget(mBudget);
        OutcomeDao dao = new OutcomeDao(mContext);
        int len = dayList.size();
        float []sums = new float[len];
        dao.startReadableDatabase();
        for(int i=0;i<len;i++){
            DayWrapper day = dayList.get(i);
            long down = day.mFirstTime;
            long up = day.mLastTime;
            sums[i] = dao.getSum("sum(outcome_money) as sum",
                    "outcome_time<? and outcome_time>?",new String[]{""+up,""+down});
            Log.i(TAG, "up:" + TimeUtils.getTimeStringWithMilli(up));
            Log.i(TAG, "down:" + TimeUtils.getTimeStringWithMilli(down));
            Log.i(TAG,"sum: "+sums[i]);
        }
        dao.closeDatabase();
        Message msg = new Message();
        msg.what = ConstantsForHome.OUTCOME_LIST_WITHIN_BUDGET_HANDLER_FLAG;
        Bundle data = new Bundle();
        data.putFloatArray(ConstantsForHome.OUTCOME_SUM_LIST_WITHIN_BUDGET,sums);
        msg.setData(data);
        mHandler.sendMessage(msg);
    }
}
