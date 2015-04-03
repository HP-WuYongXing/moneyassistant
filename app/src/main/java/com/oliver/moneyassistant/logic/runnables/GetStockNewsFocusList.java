package com.oliver.moneyassistant.logic.runnables;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;

import com.oliver.moneyassistant.constants.ConstantsForHttp;
import com.oliver.moneyassistant.constants.ConstantsForStock;
import com.oliver.moneyassistant.db.models.NewsTitle;
import com.oliver.moneyassistant.logic.http.DataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oliver on 2015/3/27.
 */
public class GetStockNewsFocusList implements Runnable{

    public String TAG = "StockNewsDapanList";
    private Context mContext;
    private Handler mHandler;
    private int mPage;

    public GetStockNewsFocusList(Context context, Handler handler,int page){
        this.mContext = context;
        this.mHandler = handler;
        this.mPage = page;
    }

    @Override
    public void run() {
        Log.i(TAG, "in-------------->run....");
        List<NewsTitle> list = getNewsItem();
        Bundle data = new Bundle();
        data.putParcelableArrayList(
                ConstantsForStock.STOCK_FOCUS_ITEM_LIST,(ArrayList<? extends Parcelable>)list);
        Message msg = new Message();
        msg.what = ConstantsForStock.STOCK_FOCUS_ITEM_LIST_HANDLER;
        msg.setData(data);
        mHandler.sendMessage(msg);
    }

    private List<NewsTitle> getNewsItem (){
       String url = ConstantsForHttp.FINANCE_CE_FOCUS_URL+"?newskind=focus&show=title&page="+this.mPage;
        String json = DataUtils.readJsonString(url);
        List<NewsTitle> titleList = NewsTitle.getNewsTitleFromJSON(json);
        return titleList;
    }
}
