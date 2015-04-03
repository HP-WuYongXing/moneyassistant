package com.oliver.moneyassistant.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.oliver.moneyassistant.R;
import com.oliver.moneyassistant.constants.ConstantsForStock;
import com.oliver.moneyassistant.db.models.NewsTitle;
import com.oliver.moneyassistant.logic.runnables.StockNewsGeguList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Oliver on 2015/3/27.
 */
public class StockGeguNewsFragment extends Fragment
        implements AdapterView.OnItemClickListener{

    private static final String TAG = "StockGeguNewsFragment";
    private View mRootView;
    protected ListView mGeguNewsList;

    protected SimpleAdapter mNewsItemAdapter;
    protected List<Map<String,String>> mNewsList;

    protected List<NewsTitle> mNewsItems;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.stock_gegu_news_fragment,null);
        return mRootView;
    }

    protected Handler listHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ConstantsForStock.STOCK_GEGU_ITEM_LIST_HANDLER:
                    Bundle data = msg.getData();
                    mNewsItems =
                            data.getParcelableArrayList(ConstantsForStock.STOCK_GEGU_ITEM_LIST);
                    showNewsItems();
                    break;
            }
        }
    };

    private void initVariable(){
        if(mNewsList==null){
            mNewsList = new ArrayList<>();
        }
        if(mNewsList.size()==0){
            updateListData();
        }
        mNewsItemAdapter  = new SimpleAdapter(this.getActivity(),mNewsList,
                R.layout.stock_news_dapan_view_cell,new String[]{"news_title","news_time"},
                new int[]{R.id.tv_stock_news_title,R.id.tv_stock_news_time});

        mGeguNewsList = (ListView) mRootView.findViewById(R.id.lv_stock_news_gegu);
        mGeguNewsList.setAdapter(mNewsItemAdapter);
        mGeguNewsList.setOnItemClickListener(this);
    }

    private void updateListData(){
        new Thread(new StockNewsGeguList(this.getActivity(), listHandler)).start();
    }

    private void showNewsItems(){
        //Log.i(TAG, "list-------------------->size: "+list.size());
        if(mNewsItems!=null&&mNewsItems.size()!=0){
            for(NewsTitle i:mNewsItems){
                Map<String,String> m =new HashMap<>();
                m.put("news_title",i.getTitle());
                m.put("news_time",i.getTime());
                mNewsList.add(m);
            }
            mNewsItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initVariable();
        Log.i(TAG, "onActivityCreated... ... ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"onResume... ... ");
    }

    @Override
    public void onItemClick(AdapterView<?> parent,
                            View view, int position, long id){
        Intent intent = new Intent();
        intent.setClass(this.getActivity(),StockNewsContentActivity.class);
        intent.putExtra(ConstantsForStock.STOCK_NEWS_ITEM,
                mNewsItems.get(position));
        this.startActivity(intent);
    }
}
