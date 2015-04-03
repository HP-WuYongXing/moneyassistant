package com.oliver.moneyassistant.logic.runnables;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.oliver.moneyassistant.constants.ConstantsForHome;
import com.oliver.moneyassistant.db.dao.BudgetDao;
import com.oliver.moneyassistant.db.models.Budget;

/**
 * Created by Oliver on 2015/3/22.
 */
public class SaveBudget implements Runnable{
    private Context mContext;
    private Handler mHandler;
    private Budget mBudget;
    private long duplicatedId;

    public SaveBudget(Context context,Handler handler,Budget budget,long id){
        this.mContext = context;
        this.mHandler =handler;
        this.mBudget = budget;
        duplicatedId = id;
    }

    @Override
    public void run() {
        BudgetDao dao = new BudgetDao(this.mContext);
        dao.startWritableDatabase(false);
        if(duplicatedId!=-1){
            /*删除重复预算*/
            dao.delete((int)duplicatedId);
        }
        long id = dao.insert(mBudget);
        dao.closeDatabase();
        boolean saveResult = false;
        if(id!=-1){
            saveResult = true;
        }
        Message msg = new Message();
        msg.what = ConstantsForHome.BUDGET_SAVE_HANDLER_FLAG;
        Bundle data = new Bundle();
        data.putBoolean(ConstantsForHome.BUDGET_SAVE_BOOLEAN,saveResult);
        msg.setData(data);
        mHandler.sendMessage(msg);
    }
}
