package com.oliver.moneyassistant.logic.runnables;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.oliver.moneyassistant.constants.ConstantsForHome;
import com.oliver.moneyassistant.db.utils.TimeUtils;
import com.oliver.moneyassistant.db.dao.*;
import java.util.Date;
/**
 * Created by Oliver on 2015/3/20.
 */
public class GetOutcomeSumWithinToday implements Runnable{
    private Context mContext;
    private Handler mHandler;
    private Date mDate;

    public GetOutcomeSumWithinToday(Context context, Handler handler){
        this.mContext = context;
        this.mHandler = handler;

    }

    public GetOutcomeSumWithinToday(Context context,Handler handler,Date d){
        this.mContext = context;
        this.mHandler = handler;
        this.mDate = d;
    }

    @Override
    public void run() {
        long uplimit,downLimit;
        if(mDate==null) {
            uplimit = TimeUtils.getNow();
            downLimit = TimeUtils.getLastSecondOfYesterday();
        }else{
            uplimit = TimeUtils.getLastSecondOfTheDay(mDate);
            downLimit = TimeUtils.getLastSecondOfTheDay(
                    new Date(mDate.getTime()-TimeUtils.getOneDayPeriod()+1000));
        }
        OutcomeDao dao = new OutcomeDao(mContext);
        dao.startReadableDatabase();
        float sum =dao.getSum("sum(outcome_money) as sum","outcome_time<? and outcome_time>?",
                new String[]{""+uplimit,""+downLimit});
        dao.closeDatabase();
        Message msg = new Message();
        msg.what = ConstantsForHome.OUTCOME_HANDLER_FLAG;
        Bundle data = new Bundle();
        data.putFloat(ConstantsForHome.OUTCOME_SUM, sum);
        msg.setData(data);
        mHandler.sendMessage(msg);
    }
}
