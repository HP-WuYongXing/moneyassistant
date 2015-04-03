package com.oliver.moneyassistant.logic.runnables;

import android.os.Bundle;
import android.os.Handler;
import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.ab.util.AbStrUtil;
import com.oliver.moneyassistant.constants.ConstantsForHttp;
import com.oliver.moneyassistant.constants.ConstantsForStock;
import com.oliver.moneyassistant.db.models.NewsContent;
import com.oliver.moneyassistant.db.models.NewsTitle;
import com.oliver.moneyassistant.logic.http.CommonException;
import com.oliver.moneyassistant.logic.http.DataUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
            case ConstantsForStock.STOCK_NEWS_KIND_FOCUS:getFocusNews();break;
            case ConstantsForStock.STOCK_NEWS_KIND_GEGU:getGeguNews();break;
            case ConstantsForStock.STOCK_NEWS_KIND_COMPANY:getCompanyNews();break;
        }

    }
    private void getFocusNews(){
        StringBuilder sb = new StringBuilder();
        sb.append(ConstantsForHttp.FINANCE_CE_FOCUS_URL);
        sb.append("?newskind=focus&show=content&titleid="+this.mNewsItem.getId());
        String json = DataUtils.readJsonString(sb.toString());
        NewsContent content = NewsContent.getNewsContentFromJSON(json);
        Bundle data = new Bundle();
        data.putParcelable(ConstantsForStock.STOCK_NEWS_CONTENT,content);
        Message msg = new Message();
        msg.what = ConstantsForStock.STOCK_NEWS_CONTENT_HANDLER;
        msg.setData(data);
        mHandler.sendMessage(msg);
    }
    private void getGeguNews(){

    }
    private void getCompanyNews(){}
}
