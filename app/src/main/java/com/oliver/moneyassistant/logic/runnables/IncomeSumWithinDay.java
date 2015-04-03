package com.oliver.moneyassistant.logic.runnables;

import android.content.Context;
import android.os.Handler;

import com.oliver.moneyassistant.constants.ConstantsForHome;
import com.oliver.moneyassistant.db.dao.IncomeDao;
import com.oliver.moneyassistant.db.utils.TimeUtils;
import android.os.Message;
import android.os.Bundle;

import java.util.Date;

/**
 * Created by Oliver on 2015/3/15.
 */
public class IncomeSumWithinDay implements Runnable{

    private static final String TAG="getIncomeSum";
    private IncomeDao mDao;
    private Handler mHandler;
    private Date mDate;

    public IncomeSumWithinDay(Context context,Handler handler){
        this.mHandler = handler;
        mDao = new IncomeDao(context);
    }
    public IncomeSumWithinDay(Context context,Handler handler,Date date){
        this.mHandler = handler;
        mDao = new IncomeDao(context);
        this.mDate = date;
    }

    @Override
    public void run() {
        long firstMinute;
        long lastMinute;
        if(mDate==null) {
            lastMinute = TimeUtils.getNow();
            firstMinute = TimeUtils.getLastSecondOfYesterday();
        }else{
            lastMinute = TimeUtils.getLastSecondOfTheDay(mDate);
            firstMinute = TimeUtils.getLastSecondOfTheDay(
                    new Date(mDate.getTime()-TimeUtils.getOneDayPeriod()+1000));
        }
        mDao.startReadableDatabase();
        float sum = mDao.getSum("sum(income_money) as sum","income_time<? and income_time>?",
                new String[]{""+lastMinute,""+firstMinute});
        mDao.closeDatabase();
        Message msg= new Message();
        msg.what= ConstantsForHome.INCOME_HANDLER_FLAG;
        Bundle data = new Bundle();
        data.putFloat(ConstantsForHome.INCOME_SUM,sum);
        msg.setData(data);
        mHandler.sendMessage(msg);
    }
}
