package com.oliver.moneyassistant.logic.runnables;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.oliver.moneyassistant.constants.ConstantsForHome;
import com.oliver.moneyassistant.db.dao.BudgetDao;
import com.oliver.moneyassistant.db.models.Budget;

import java.util.List;

/**
 * Created by Oliver on 2015/3/22.
 */
public class CheckBudgetTime implements Runnable{

    private Context mContext;
    private Handler mHandler;
    private Budget mBudget;

    public CheckBudgetTime(Context context, Handler handler, Budget budget){
        this.mContext = context;
        this.mHandler = handler;
        this.mBudget = budget;
    }

    @Override
    public void run() {
        /*查重*/
        long startTime = mBudget.getStartTime();
        long endTime = mBudget.getEndTime();

        BudgetDao dao = new BudgetDao(mContext);
        dao.startReadableDatabase();
        int count = dao.queryCount("(start_time>? and start_time<?) or (end_time>? and end_time<?)",
                new String[]{startTime+"",endTime+"",startTime+"",endTime+""});
        List<Budget> bList = null;
        if(count!=0){
           bList = dao.queryList("(start_time>? and start_time<?) or (end_time>? and end_time<?)",
                new String[]{startTime+"",endTime+"",startTime+"",endTime+""});
        }
        dao.closeDatabase();
        long duplicateId =-1L;
        if(bList!=null&&bList.size()!=0){
           duplicateId = bList.get(0).get_id();
        }
        Message msg = new Message();
        msg.what = ConstantsForHome.BUDGET_DUPLICATED_HANDLER_FLAG;
        Bundle data= new Bundle();
        data.putLong(ConstantsForHome.BUDGET_DUPLICATED_ID,duplicateId);
        msg.setData(data);
        mHandler.sendMessage(msg);
    }
}
