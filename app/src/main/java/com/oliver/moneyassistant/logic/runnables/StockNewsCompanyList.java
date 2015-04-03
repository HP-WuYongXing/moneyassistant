package com.oliver.moneyassistant.logic.runnables;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;

import com.oliver.moneyassistant.constants.ConstantsForStock;
import com.oliver.moneyassistant.db.models.NewsTitle;
import com.oliver.moneyassistant.logic.http.CommonException;
import com.oliver.moneyassistant.logic.http.DataUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oliver on 2015/3/27.
 */
public class StockNewsCompanyList implements Runnable{

    public String TAG = "StockNewsCompanyList";
    private Context mContext;
    private Handler mHandler;

    public StockNewsCompanyList(Context context, Handler handler){
        this.mContext = context;
        this.mHandler = handler;
    }

    @Override
    public void run() {
        Log.i(TAG, "in-------------->run....");
        List<NewsTitle> list = getNewsItem();
        Bundle data = new Bundle();
        data.putParcelableArrayList(
                ConstantsForStock.STOCK_COMPANY_ITEM_LIST,(ArrayList<? extends Parcelable>)list);
        Message msg = new Message();
        msg.what = ConstantsForStock.STOCK_COMPANY_ITEM_LIST_HANDLER;
        msg.setData(data);
        mHandler.sendMessage(msg);
    }

    private List<NewsTitle> getNewsItem (){
        String url =
                "http://roll.finance.sina.com.cn/finance/zq1/ssgs/index.shtml";
        List<NewsTitle> items=null;
        try {
            items = new ArrayList<>();
            Document doc = DataUtils.doGet(url);
            Elements units = doc.getElementsByClass("list_009");
            for(int i=0;i<units.size();i++){
                Elements lists = units.get(i).getElementsByTag("li");
                for(int j=0;j<lists.size();j++){
                    Element el_a = lists.get(j).getElementsByTag("a").get(0);
                    NewsTitle item = new NewsTitle();
                        item.setNewsType(ConstantsForStock.STOCK_NEWS_KIND_FOCUS);
                    item.setTitle(el_a.text());
                    item.setLink(el_a.attr("href"));
                    Element el_span = lists.get(j).getElementsByTag("span").get(0);
                    item.setTime(el_span.text());
                    System.out.println(item.toString());
                    items.add(item);
                }
            }
        }catch (CommonException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return items;
    }
}
