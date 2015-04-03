package com.oliver.moneyassistant.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.oliver.moneyassistant.R;
import com.oliver.moneyassistant.constants.ConstantsForStock;
import com.oliver.moneyassistant.db.models.NewsTitle;
import com.oliver.moneyassistant.logic.runnables.GetStockNewsFocusList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Oliver on 2015/3/27.
 */
public class StockFocusNewsFragment extends Fragment
        implements AdapterView.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener{
    private String TAG = "StockDapanNewsFragment";

    private View mRootView;
    protected ListView mLVFocuseNewsList;
    protected SwipeRefreshLayout mRefreshLayout;
    protected SimpleAdapter mNewsItemAdapter;
    protected List<Map<String,String>> mNewsList;

    protected List<NewsTitle> mNewsItems;

    protected View mFooterViewNormal;

    protected LinearLayout mLLRefreshingFooter;
    protected TextView mTVClickRefresh;

    protected int mShowPage;

    private boolean isDropDown;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.stock_focus_news_fragment,null);

        mFooterViewNormal = inflater.inflate(R.layout.drop_down_list_normal_footer,null);
        mLLRefreshingFooter = (LinearLayout)mFooterViewNormal.findViewById(R.id.ll_show_refreshing);
        mTVClickRefresh = (TextView)mFooterViewNormal.findViewById(R.id.tv_click_refresh);

        Log.i(TAG, "OnCreateView.........");
        return mRootView;
    }

    protected Handler listHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ConstantsForStock.STOCK_FOCUS_ITEM_LIST_HANDLER:
                    Bundle data = msg.getData();
                    mNewsItems =
                            data.getParcelableArrayList(ConstantsForStock.STOCK_FOCUS_ITEM_LIST);
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
        mLVFocuseNewsList = (ListView) mRootView.findViewById(R.id.lv_stock_news_dapan);
        mRefreshLayout = (SwipeRefreshLayout)mRootView.findViewById(R.id.srl_drop_down_refresh);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        hideListViewFooter();
     //   mLVFocuseNewsList.addFooterView(this.mFooterViewNormal);
        mLVFocuseNewsList.addFooterView(this.mFooterViewNormal);
        mLVFocuseNewsList.setAdapter(mNewsItemAdapter);
        mLVFocuseNewsList.setOnItemClickListener(this);
        isDropDown = false;
        mShowPage=0;
    }

    private void hideListViewFooter(){
        mLLRefreshingFooter.setVisibility(View.GONE);
        mTVClickRefresh.setVisibility(View.GONE);
    }

    private void showRefreshingFooter(){
        mLLRefreshingFooter.setVisibility(View.VISIBLE);
        mTVClickRefresh.setVisibility(View.GONE);
    }

    private void showClickRefreshFooter(){
        mLLRefreshingFooter.setVisibility(View.GONE);
        mTVClickRefresh.setVisibility(View.VISIBLE);
    }

    private void updateListData(){
        new Thread(new GetStockNewsFocusList(this.getActivity(), listHandler,this.mShowPage)).start();
    }

    private void showNewsItems() {
        //Log.i(TAG, "list-------------------->size: "+list.size());
        mRefreshLayout.setRefreshing(false);
        if(this.mShowPage==0) {
            mNewsList.clear();
        }
        if (mNewsItems != null && mNewsItems.size() != 0) {
            for (NewsTitle i : mNewsItems) {
                Map<String, String> m = new HashMap<>();
                m.put("news_title", i.getTitle());
                m.put("news_time", i.getTime());
                mNewsList.add(m);
            }
            mNewsItemAdapter.notifyDataSetChanged();
            showClickRefreshFooter();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initVariable();
        Log.i(TAG,"onActivityCreated... ... ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"onResume... ... ");
    }

    @Override
    public void onItemClick(AdapterView<?> parent,
                            View view, int position, long id){
        if(position<mNewsItems.size()) {
            Intent intent = new Intent();
            intent.setClass(this.getActivity(), StockNewsContentActivity.class);
            intent.putExtra(ConstantsForStock.STOCK_NEWS_ITEM, mNewsItems.get(position));
            this.startActivity(intent);
        }else{
            /*点击了footer*/
            Log.i(TAG,"click the footer.......");
            showRefreshingFooter();
            this.mShowPage++;
            updateListData();
        }
    }

    @Override
    public void onRefresh() {
        updateListData();
    }
}
