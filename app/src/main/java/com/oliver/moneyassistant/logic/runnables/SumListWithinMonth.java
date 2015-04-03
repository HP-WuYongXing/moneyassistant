package com.oliver.moneyassistant.logic.runnables;

/**
 * Created by Oliver on 2015/3/14.
 */
import android.content.Context;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

import com.oliver.moneyassistant.constants.ConstantsForHome;
import com.oliver.moneyassistant.db.models.OutcomeItem;
import com.oliver.moneyassistant.wrapperclass.DayWrapper;
import com.oliver.moneyassistant.db.dao.OutcomeDao;
import com.oliver.moneyassistant.db.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.oliver.moneyassistant.db.dao.IncomeDao;
public class SumListWithinMonth implements Runnable{
    private static final String TAG = "OutcomeSumListWithinMonth";
    private Handler mHandler;
    private OutcomeDao mOutcomeDao;
    private IncomeDao mIncomeDao;
    public SumListWithinMonth(Context context, Handler handler){
        this.mHandler = handler;
        mOutcomeDao = new OutcomeDao(context);
        mIncomeDao = new IncomeDao(context);
    }


    @Override
    public void run() {
       ArrayList<DayWrapper> list = TimeUtils.getDayWrappersWithinMonth();
        int len =list.size();
        float []outSums = new float[len];
        float []inSums = new float[len];
        mOutcomeDao.startReadableDatabase();
        mIncomeDao.startReadableDatabase();

        for(int i=0;i<len;i++){
            DayWrapper dw = list.get(i);
            float outSum = mOutcomeDao.getSum("sum(outcome_money) as sum","outcome_time<=? and outcome_time>?",new String[]{
                    dw.mLastTime+"",dw.mFirstTime+""});

            float inSum = mIncomeDao.getSum("sum(income_money) as sum","income_time<=? and income_time>?",
                    new String[]{dw.mLastTime+"",dw.mFirstTime+""});
            outSums[i]=outSum;
            inSums[i] = inSum;
        }

       long  monthDown = TimeUtils.getFirstSecondOfMonth(new Date());
       long  up =  new Date().getTime();
       List<OutcomeItem> outcomeItemList =
               mOutcomeDao.queryList("outcome_time<? and outcome_time>?",new String[]{
                       ""+up,""+monthDown
               });

        mOutcomeDao.closeDatabase();
        mIncomeDao.closeDatabase();
        Message msg = new Message();
        msg.what= ConstantsForHome.SUM_LIST_FLAG;
        Bundle args = new Bundle();
        args.putFloatArray(ConstantsForHome.OUTCOME_SUM_ARRAY,outSums);
        args.putFloatArray(ConstantsForHome.INCOME_SUM_ARRAY,inSums);
        args.putParcelableArrayList(ConstantsForHome.OUTCOME_ITEM_WITH_MONTH,
                (ArrayList<?extends Parcelable>)outcomeItemList);
        msg.setData(args);
        mHandler.sendMessage(msg);
    }

}
