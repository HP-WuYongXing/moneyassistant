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
import com.oliver.moneyassistant.logic.http.RequestParameters;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oliver on 2015/3/27.
 */
public class GetNewsList implements Runnable{

    public String TAG = "GetNewsList";
    private Context mContext;
    private Handler mHandler;
    private int mPage;
    private String mNewsType;

    public GetNewsList(Context context, Handler handler, int page, String newsType){
        this.mContext = context;
        this.mHandler = handler;
        this.mPage = page;
        this.mNewsType = newsType;
    }

    @Override
    public void run() {
        Log.i(TAG, "in-------------->run....");
        List<NewsTitle> list = getNewsItem();
        Bundle data = new Bundle();
        data.putParcelableArrayList(
                ConstantsForStock.NEWS_ITEM_LIST,
                (ArrayList<? extends Parcelable>)list);
        Message msg = new Message();
        msg.what = ConstantsForStock.NEWS_ITEM_LIST_HANDLER;
        msg.setData(data);
        mHandler.sendMessage(msg);
    }

    private List<NewsTitle> getNewsItem (){
        RequestParameters parameters = new RequestParameters(ConstantsForHttp.FINANCE_NEWS_URL);
        parameters.addNewsKind(this.mNewsType);
        parameters.addShow(RequestParameters.Show.TITLE);
        parameters.addPage(this.mPage);
        String url = parameters.toString();
        String json = DataUtils.sendHttpRequest(url);
        List<NewsTitle> titleList = NewsTitle.getNewsTitleFromJSON(json);
        return titleList;
    }
}
