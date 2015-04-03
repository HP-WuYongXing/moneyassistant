package com.oliver.moneyassistant.logic.runnables;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;

import com.oliver.moneyassistant.constants.ConstantsForHome;
import com.oliver.moneyassistant.db.dao.IncomeDao;
import com.oliver.moneyassistant.db.dao.IncomeTypeDao;
import com.oliver.moneyassistant.db.models.IncomeItem;
import com.oliver.moneyassistant.db.models.IncomeType;
import com.oliver.moneyassistant.db.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Oliver on 2015/3/21.
 */

public class GetIncomeItemList implements Runnable {
    private static final String TAG = "GetIncomeItemList";
    private Context mContext;
    private Handler mHandler;
    private Date mDate;

    public GetIncomeItemList(Context context,Handler handler){
        this.mContext = context;
        this.mHandler = handler;
    }

    public GetIncomeItemList(Context context,Handler handler,Date date){
        this.mContext = context;
        this.mHandler = handler;
        this.mDate = date;
    }

    @Override
    public void run() {
        long up,down;
        if(mDate!=null){
            up = TimeUtils.getLastSecondOfTheDay(mDate);
            down = up-TimeUtils.getOneDayPeriod()+1000;
        }else{
            up = TimeUtils.getNow();
            down = TimeUtils.getLastSecondOfYesterday();
        }

        Log.i(TAG,"in run... ... ... ...");
        IncomeDao incomeDao = new IncomeDao(this.mContext);
        incomeDao.startReadableDatabase();

        List<IncomeItem> incomeItemList = incomeDao.queryList("income_time<? and income_time>?",
                new String[]{""+up,""+down});
        Log.i(TAG,incomeItemList.toString());
        IncomeTypeDao incomeTypeDao = new IncomeTypeDao(this.mContext);
        incomeTypeDao.startReadableDatabase();
        for(int i=0;i<incomeItemList.size();i++){
           List<IncomeType> typeList =
                   incomeTypeDao.queryList("type_id=?",
                           new String[]{""+incomeItemList.get(i).getTypeId()});
            Log.i(TAG,typeList.toString());
            IncomeItem item =incomeItemList.get(i);
            item.setIncomeType(typeList.get(0));
        }

        incomeTypeDao.closeDatabase();
        incomeDao.closeDatabase();

        Message msg = new Message();
        msg.what= ConstantsForHome.INCOME_LIST_HANDLER_FLAG;
        Bundle data = new Bundle();
        data.putParcelableArrayList(ConstantsForHome.INCOME_WITHIN_DAY_LIST,
                (ArrayList<?extends Parcelable>)incomeItemList);
        msg.setData(data);
        mHandler.sendMessage(msg);
    }
}
