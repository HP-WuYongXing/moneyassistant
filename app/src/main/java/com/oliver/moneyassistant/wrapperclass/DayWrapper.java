package com.oliver.moneyassistant.wrapperclass;

/**
 * Created by Oliver on 2015/3/14.
 */
import com.oliver.moneyassistant.db.utils.TimeUtils;

import java.util.Date;
public class DayWrapper {
    public long mFirstTime;
    public long mLastTime;

    public DayWrapper(long firstTime, long lastTime) {
        this.mFirstTime = firstTime;
        mLastTime = lastTime;
    }

    public DayWrapper(Date d){
         this.mFirstTime = TimeUtils.getFirstSecondOfTheDay(d);
         this.mLastTime = TimeUtils.getLastSecondOfTheDay(d);
    }

}
