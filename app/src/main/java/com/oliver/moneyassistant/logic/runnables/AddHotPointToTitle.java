package com.oliver.moneyassistant.logic.runnables;

import android.util.Log;

import com.oliver.moneyassistant.constants.ConstantsForHttp;
import com.oliver.moneyassistant.db.models.NewsTitle;
import com.oliver.moneyassistant.logic.http.DataUtils;
import com.oliver.moneyassistant.logic.http.RequestParameters;

/**
 * Created by Oliver on 2015/4/4.
 */
public class AddHotPointToTitle implements Runnable{

    private static final String TAG = "AddHotPointToTitle";
    private NewsTitle mTitle;
    public AddHotPointToTitle(NewsTitle title){
       this.mTitle = title;
    }

    @Override
    public void run() {
        int titleId = this.mTitle.getId();
        RequestParameters parameters = new RequestParameters(ConstantsForHttp.FINANCE_CE_FOCUS_URL);
        parameters.addNewsKind(RequestParameters.NewsKind.ADD_HOT);
        parameters.addTitleId(titleId);
        String url = parameters.toString();
        String response = DataUtils.sendHttpRequest(url);
        Log.i(TAG, "response:" + response);
    }
}
