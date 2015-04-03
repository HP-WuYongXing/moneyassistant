package com.oliver.moneyassistant.logic.runnables;

import android.content.Context;
import android.os.Handler;

import com.oliver.moneyassistant.constants.ConstantsForHome;
import com.oliver.moneyassistant.db.dao.*;
import com.oliver.moneyassistant.db.models.OutcomeItem;
import com.oliver.moneyassistant.db.models.OutcomeType;
import com.oliver.moneyassistant.db.utils.TimeUtils;

import java.util.*;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;

/**
 * Created by Oliver on 2015/3/16.
 */
public class ParcelableOutcomeList implements Runnable{
    private OutcomeDao mOutcomeDao;
    private Handler mHandler;
    private OutcomeTypeDao mTypeDao;
    private Date mDate;

    public ParcelableOutcomeList(Context context, Handler handler){
        this.mHandler = handler;
        mOutcomeDao = new OutcomeDao(context);
        mTypeDao = new OutcomeTypeDao(context);
    }

    public ParcelableOutcomeList(Context context, Handler handler,Date date){
        this.mHandler = handler;
        mOutcomeDao = new OutcomeDao(context);
        mTypeDao = new OutcomeTypeDao(context);
        this.mDate = date;
    }


    @Override
    public void run(){
        long upLimit;
        long downLimit;
        if(mDate==null){
            upLimit = TimeUtils.getLastSecondOfToday();
            downLimit = TimeUtils.getLastSecondOfYesterday();
        }else{
            upLimit = TimeUtils.getLastSecondOfTheDay(mDate);
            downLimit = upLimit-TimeUtils.getOneDayPeriod()+1000;
        }
        mOutcomeDao.startReadableDatabase();
        List<OutcomeItem> list = mOutcomeDao.queryList("outcome_time>? and outcome_time<?",
                new String[]{""+downLimit,""+upLimit});
        mOutcomeDao.closeDatabase();

        mTypeDao.startReadableDatabase();
        for(OutcomeItem item:list){
            int typeId = item.getTypeId();
           List<OutcomeType> typeList =  mTypeDao.queryList("type_id=?",new String[]{""+typeId});
           item.setOutcomeType(typeList.get(0));
        }
        mTypeDao.closeDatabase();
        /*List<ParcelableOutcome> outcomeList = new ArrayList<>();
        for(OutcomeItem i:list){
            ParcelableOutcome p = new ParcelableOutcome();
            p._id = i.get_id();
            p.outcomeType =i.getTypeId();
            p.outcomeTime = i.getOutcomeTime();
            p.outcomeMoney = i.getOutcomeMoney();
            p.addressHeader = i.getAddress().getAddressHeader();
            p.describe = i.getOutcomeDescribe();
            outcomeList.add(p);
        }*/
        Bundle args = new Bundle();
        args.putParcelableArrayList(ConstantsForHome.PARSELABLE_OUTCOME_LIST,
                (ArrayList<? extends Parcelable>)list);
        Message msg = new Message();
        msg.what = ConstantsForHome.PARSELABLE_OUTCOME_LIST_FLAG;
        msg.setData(args);
        mHandler.sendMessage(msg);
    }
}
