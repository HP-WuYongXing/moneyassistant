package com.oliver.moneyassistant.logic.runnables;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.oliver.moneyassistant.constants.ConstantsForHome;
import com.oliver.moneyassistant.db.dao.BudgetDao;
import com.oliver.moneyassistant.db.dao.OutcomeDao;
import com.oliver.moneyassistant.db.models.Budget;
import com.oliver.moneyassistant.db.utils.TimeUtils;

import  java.util.*;

/**
 * Created by Oliver on 2015/3/14.
 */
public class GetBudgetData implements Runnable{

        private static final String TAG="getOutcomeSum";
        private OutcomeDao mOutcomeDao;
        private BudgetDao mBudgetDao;
        private Handler mHandler;
        private SQLiteDatabase db;

        public GetBudgetData(Context context, Handler handler) {
            this.mHandler = handler;
            mOutcomeDao = new OutcomeDao(context);
            mBudgetDao = new BudgetDao(context);
        }

        @Override
        public void run() {
            execute();
        }
    private void execute(){
        mBudgetDao.startReadableDatabase();
        List<Budget> budgetList;
        long now = TimeUtils.getNow();
        budgetList = mBudgetDao.queryList("start_time<? and end_time>?",new String[]{""+now,""+now});
        mBudgetDao.closeDatabase();
        Bundle args = new Bundle();
        if(budgetList!=null&&budgetList.size()!=0) {
            args.putParcelable(ConstantsForHome.BUDGET_TODAY, budgetList.get(0));
        }else{
            args.putParcelable(ConstantsForHome.BUDGET_TODAY,null);
        }
        Message msg = new Message();
        msg.what = ConstantsForHome.BUDGET_HANDLER_FLAG;
        msg.setData(args);
        mHandler.sendMessage(msg);
    }
}
