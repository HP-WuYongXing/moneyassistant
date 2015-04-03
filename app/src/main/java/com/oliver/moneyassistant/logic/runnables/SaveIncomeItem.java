package com.oliver.moneyassistant.logic.runnables;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.oliver.moneyassistant.constants.ConstantsForHome;
import com.oliver.moneyassistant.db.dao.IncomeDao;
import com.oliver.moneyassistant.db.models.IncomeItem;

/**
 * Created by Oliver on 2015/3/23.
 */
public class SaveIncomeItem implements Runnable{

    private Context mContext;
    private Handler mHandler;
    private IncomeItem mIncomeItem;
    private static final String TAG="SaveIncomeItem";

    public SaveIncomeItem (Context context,Handler handler,IncomeItem item){
        this.mContext = context;
        this.mHandler = handler;
        this.mIncomeItem = item;
    }

    @Override
    public void run() {
        IncomeDao dao = new IncomeDao(mContext);
        dao.startWritableDatabase(false);
        Log.i(TAG, "item: " + this.mIncomeItem);
        long id = dao.insert(this.mIncomeItem);
        dao.closeDatabase();
        boolean success = false;
        if(id!=-1){
            success = true;
        }
        Message msg = new Message();
        msg.what = ConstantsForHome.INCOME_SAVE_HANDLER_FLAG;
        Bundle data = new Bundle();
        data.putBoolean(ConstantsForHome.INCOME_SAVE_BOOLEAN,success);
        msg.setData(data);
        mHandler.sendMessage(msg);
    }
}
