package com.oliver.moneyassistant.logic.runnables;

import android.os.Bundle;
import android.os.Handler;
import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.oliver.moneyassistant.constants.ConstantsForHttp;
import com.oliver.moneyassistant.constants.ConstantsForStock;
import com.oliver.moneyassistant.db.models.NewsContent;
import com.oliver.moneyassistant.db.models.NewsTitle;
import com.oliver.moneyassistant.logic.http.DataUtils;
import com.oliver.moneyassistant.logic.http.RequestParameters;

/**
 * Created by Oliver on 2015/3/28.
 */
public class GetStockNewsContent implements Runnable{

    private String TAG = "GetStockNewsContent";
    private Context mContext;
    private Handler mHandler;
    private NewsTitle mNewsItem;

    public GetStockNewsContent(Context context,
                               Handler handler,
                               NewsTitle newsItem){
        this.mContext = context;
        this.mHandler = handler;
        this.mNewsItem = newsItem;
    }

    @Override
    public void run() {
        switch (mNewsItem.getNewsType()){
            case ConstantsForStock.STOCK_NEWS_KIND_FOCUS:
                getNewsContent();break;
            case ConstantsForStock.STOCK_NEWS_KIND_STOCK:
                getNewsContent();break;
            case ConstantsForStock.STOCK_NEWS_KIND_COMPANY:getCompanyNews();break;
            case ConstantsForStock.STOCK_NEWS_KIND_FOCUS_HEADER:
                getNewsContent();break;
        }
    }

    private void getNewsContent(){
        RequestParameters parameters = new RequestParameters(ConstantsForHttp.FINANCE_NEWS_URL);
        parameters.addNewsKind(getNewsStringType(this.mNewsItem.getNewsType()));
        parameters.addShow(RequestParameters.Show.CONTENT);
        parameters.addTitleId(this.mNewsItem.getId());
        String url = parameters.toString();
        Log.i(TAG, "url:-------->" + url);
        String json = DataUtils.sendHttpRequest(url);
        NewsContent content = NewsContent.getNewsContentFromJSON(json);
        Bundle data = new Bundle();
        data.putParcelable(ConstantsForStock.STOCK_NEWS_CONTENT,content);
        Message msg = new Message();
        msg.what = ConstantsForStock.STOCK_NEWS_CONTENT_HANDLER;
        msg.setData(data);
        mHandler.sendMessage(msg);
    }

    private String getNewsStringType(int newsType){
        switch (newsType){
            case ConstantsForStock.STOCK_NEWS_KIND_FOCUS:
                return RequestParameters.NewsKind.FOCUS;
            case ConstantsForStock.STOCK_NEWS_KIND_STOCK:
                return RequestParameters.NewsKind.STOCK;
            default: return  RequestParameters.NewsKind.FOCUS;
        }
    }
    private void getGeguNews(){

    }
    private void getCompanyNews(){}
}
