package com.oliver.moneyassistant.logic.runnables;

/**
 * Created by Oliver on 2015/3/15.
 */
import com.oliver.moneyassistant.constants.ConstantsForHome;
import com.oliver.moneyassistant.db.dao.IncomeDao;
import com.oliver.moneyassistant.db.utils.TimeUtils;
import com.oliver.moneyassistant.wrapperclass.DayWrapper;
import java.util.ArrayList;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class IncomeSumListWithinMonth implements Runnable {
   private static final String TAG = "IncomeSumListWithinMonth";
   private IncomeDao mDao;
   private Context mContext;
   private Handler mHandler;
   public IncomeSumListWithinMonth(Context context,Handler handler,IncomeDao dao){
       this.mContext = context;
       this.mHandler = handler;
       this.mDao = dao;
   }
    @Override
    public void run() {
        ArrayList<DayWrapper> dayWrappers = TimeUtils.getDayWrappersWithinMonth();
        int len = dayWrappers.size();
        float sums[] = new float[len];
        this.mDao.startReadableDatabase();
        for(int i=0;i<len;i++){
            DayWrapper d = dayWrappers.get(i);
            float sum = mDao.getSum("sum(income_money) as sum","income_time<? and income_time>?",
                    new String[]{d.mLastTime+"",+d.mFirstTime+""});
            sums[i] = sum;
        }
        mDao.closeDatabase();
        Message msg = new Message();
        msg.what = ConstantsForHome.INCOME_SUM_LIST_FLAG;
        Bundle data = new Bundle();
        data.putFloatArray(ConstantsForHome.INCOME_SUM_ARRAY,sums);
        msg.setData(data);
        mHandler.sendMessage(msg);
    }
}
