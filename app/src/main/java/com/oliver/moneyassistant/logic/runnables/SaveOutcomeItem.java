package com.oliver.moneyassistant.logic.runnables;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.oliver.moneyassistant.constants.ConstantsForHome;
import com.oliver.moneyassistant.db.dao.OutcomeDao;
import com.oliver.moneyassistant.db.models.OutcomeItem;

/**
 * Created by Oliver on 2015/3/23.
 */
public class SaveOutcomeItem implements Runnable{
    private Context mContext;
    private Handler mHandler;
    private OutcomeItem mOutcomeItem;

    public SaveOutcomeItem(Context context,Handler handler,OutcomeItem outcomeItem){
        this.mContext = context;
        this.mHandler = handler;
        this.mOutcomeItem = outcomeItem;
    }

    @Override
    public void run() {
        OutcomeDao dao = new OutcomeDao(this.mContext);
        dao.startWritableDatabase(false);
        long id = dao.insert(this.mOutcomeItem);
        dao.closeDatabase();
        boolean success = false;
        if(id!=-1){
            success = true;
        }
        Bundle data = new Bundle();
        data.putBoolean(ConstantsForHome.OUTCOME_SAVE_BOOLEAN,success);
        Message msg = new Message();
        msg.what = ConstantsForHome.OUTCOME_SAVE_HANDLER_FLAG;
        msg.setData(data);
        this.mHandler.sendMessage(msg);
    }
}
