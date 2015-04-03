package com.oliver.moneyassistant.logic.runnables;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

import com.oliver.moneyassistant.constants.ConstantsForHome;
import com.oliver.moneyassistant.db.dao.BudgetDao;
import com.oliver.moneyassistant.db.models.Budget;
import com.oliver.moneyassistant.db.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oliver on 2015/3/24.
 */
public class GetBudgetList implements Runnable{
    private Context mContext;
    private Handler mHandler;
    public GetBudgetList(Context context,Handler handler){
        this.mContext = context;
        this.mHandler = handler;
    }

    @Override
    public void run() {
        long down = TimeUtils.getLastSecondOfYesterday();
        long up = TimeUtils.getNow();
        BudgetDao dao = new BudgetDao(this.mContext);
        dao.startReadableDatabase();
        List<Budget> list = dao.queryList("start_time<=? and end_time>=?",
                new String[]{down+"",up+""});
        dao.closeDatabase();
        Bundle data = new Bundle();
        data.putParcelableArrayList(ConstantsForHome.BUDGET_TODAY_LIST,
                (ArrayList<?extends Parcelable>)list);
        Message msg =new Message();
        msg.what = ConstantsForHome.BUDGET_LIST_HANDLER_FLAG;
        msg.setData(data);
        mHandler.sendMessage(msg);
    }
}
