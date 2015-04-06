package com.oliver.moneyassistant.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.oliver.moneyassistant.R;
import com.oliver.moneyassistant.constants.ConstantsForStock;
import com.oliver.moneyassistant.db.models.NewsTitle;
import com.oliver.moneyassistant.logic.http.RequestParameters;
import com.oliver.moneyassistant.logic.runnables.AddHotPointToTitle;
import com.oliver.moneyassistant.logic.runnables.GetNewsList;
import com.oliver.moneyassistant.ui.utils.FocusNewsTitleListAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Oliver on 2015/3/27.
 */
public class StockStockNewsFragment extends Fragment
        implements AdapterView.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener{
    private String TAG = "StockStockNewsFragment";
    private View mRootView;
    protected ListView mLVStockNewsList;
    protected SwipeRefreshLayout mRefreshLayout;
    protected List<NewsTitle> mNewsList;
    protected List<NewsTitle> mReceivedNewsTitles;
    protected View mFooterViewNormal;
    protected LinearLayout mLLRefreshingFooter;
    protected TextView mTVClickRefresh;
    protected int mShowPage;
    private boolean mIsRefresh;
    private FocusNewsTitleListAdapter mAdapter;
    protected  View mListHeaderView;
    protected ViewPager mVPHeaderView;
    protected LinearLayout mLLPagerIndicator;
    protected List<View> mPagers;
    private ImageView[]mPagerTips;
    private List<NewsTitle> mHeaderNewsList;
    private TextView mTVHeaderNewsTitle;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.stock_focus_news_fragment,null);

        mFooterViewNormal = inflater.inflate(R.layout.drop_down_list_normal_footer,null);

        mListHeaderView = inflater.inflate(R.layout.drop_down_list_normal_header,null);

        mLLRefreshingFooter = (LinearLayout)mFooterViewNormal.findViewById(R.id.ll_show_refreshing);
        mTVClickRefresh = (TextView)mFooterViewNormal.findViewById(R.id.tv_click_refresh);

        mVPHeaderView = (ViewPager)mListHeaderView.findViewById(R.id.vp_focus_news_list_header);
        mLLPagerIndicator =(LinearLayout)mListHeaderView.findViewById(R.id.ll_focus_indicator_view_group);
        mTVHeaderNewsTitle =(TextView)mListHeaderView.findViewById(R.id.tv_header_news_title);
        Log.i(TAG, "OnCreateView.........");
        return mRootView;
    }

    private void initViewPager(){
        mPagers = new ArrayList<>();
        for(int i=0;i<mHeaderNewsList.size();i++){
            NewsTitle title = mHeaderNewsList.get(i);
            ImageView iv = new ImageView(this.getActivity());
            Bitmap bitmap = BitmapFactory.decodeFile(title.getThumbnail());
            iv.setImageBitmap(bitmap);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mPagers.add(iv);
        }

        mPagerTips = new ImageView[mPagers.size()];
        for(int i=0; i<mPagerTips.length; i++){
            ImageView imageView = new ImageView(this.getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(10,10);
            lp.setMargins(3,0,3,0);
            imageView.setLayoutParams(lp);
            mPagerTips[i] = imageView;
            if(i == 0){
                mPagerTips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            }else{
                mPagerTips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
            mLLPagerIndicator.addView(imageView);
        }

        mVPHeaderView.setAdapter(new PagerViewAdapter());
        mVPHeaderView.setCurrentItem(0);
        mTVHeaderNewsTitle.setText(mHeaderNewsList.get(0).getTitle());
        mVPHeaderView.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setImageBackground(position);
                mTVHeaderNewsTitle.setText(mHeaderNewsList.get(position).getTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setImageBackground(int selectedItem){
        for(int i=0; i<mPagerTips.length; i++){
            if(i == selectedItem){
                mPagerTips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            }else{
                mPagerTips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
        }
    }

    protected Handler listHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ConstantsForStock.NEWS_ITEM_LIST_HANDLER:
                    Bundle data = msg.getData();
                    mReceivedNewsTitles =
                            data.getParcelableArrayList(ConstantsForStock.NEWS_ITEM_LIST);
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
        mAdapter = new FocusNewsTitleListAdapter(mNewsList);

        mLVStockNewsList = (ListView) mRootView.findViewById(R.id.lv_stock_news_dapan);
        mRefreshLayout = (SwipeRefreshLayout)mRootView.findViewById(R.id.srl_drop_down_refresh);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        hideListViewFooter();

        mLVStockNewsList.addHeaderView(this.mListHeaderView);
        mLVStockNewsList.addFooterView(this.mFooterViewNormal);
        mLVStockNewsList.setAdapter(mAdapter);
        mLVStockNewsList.setOnItemClickListener(this);
        mIsRefresh = true;
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
        new Thread(new GetNewsList(this.getActivity(),
                listHandler,
                this.mShowPage,
                RequestParameters.NewsKind.STOCK)).start();
    }

    private void showNewsItems() {
        //Log.i(TAG, "list-------------------->size: "+list.size());
        Log.d(TAG," showNewsItems recieved news item size: "+this.mReceivedNewsTitles.size());
        mRefreshLayout.setRefreshing(false);
        if(mIsRefresh){
            mNewsList.clear();
            if (mReceivedNewsTitles != null && mReceivedNewsTitles.size() != 0) {
                getHeaderNewsTitle();//首先清除header new title
                for (NewsTitle i : mReceivedNewsTitles) {
                    mNewsList.add(i);
                }
                mAdapter.notifyDataSetChanged();
                showClickRefreshFooter();
            }
        }else{
            if (mReceivedNewsTitles != null && mReceivedNewsTitles.size() != 0) {
                for (NewsTitle i : mReceivedNewsTitles) {
                    mNewsList.add(i);
                }
                mAdapter.notifyDataSetChanged();
                showClickRefreshFooter();
            }
        }
    }

    private void getHeaderNewsTitle(){
        Log.d(TAG,"recieved news item size: "+this.mReceivedNewsTitles.size());
        mHeaderNewsList = new ArrayList<>();
        for(int i=0;i<mReceivedNewsTitles.size();i++){
            NewsTitle title = mReceivedNewsTitles.get(i);
            if(title.getNewsType()==ConstantsForStock.STOCK_NEWS_KIND_FOCUS_HEADER){
                mHeaderNewsList.add(title);
                Log.d(TAG, "get news headers: " + title.toString());
                mReceivedNewsTitles.remove(i);
                i--;
            }
        }

        if(mHeaderNewsList.size()!=0) {
            initViewPager();
        }
    }

    private void addHotPointToTitle(NewsTitle title){
        new Thread(new AddHotPointToTitle(title)).start();
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
        if(position< mNewsList.size()) {
            Intent intent = new Intent();
            intent.setClass(this.getActivity(), StockNewsContentActivity.class);
            intent.putExtra(ConstantsForStock.NEWS_ITEM, mNewsList.get(position-1));
            addHotPointToTitle(mNewsList.get(position-1));
            this.startActivity(intent);
        }else{
            /*点击了footer*/
            Log.i(TAG,"click the footer.......");
            showRefreshingFooter();
            this.mShowPage++;
            mIsRefresh=false;
            updateListData();
        }
    }

    @Override
    public void onRefresh() {
        mIsRefresh=true;
        this.mShowPage=0;
        updateListData();
    }

    private class PagerViewAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mPagers.get(position);
             final int pos =position;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(StockStockNewsFragment.this.getActivity(),
                            StockNewsContentActivity.class);
                    intent.putExtra(ConstantsForStock.NEWS_ITEM, mHeaderNewsList.get(pos));
                    addHotPointToTitle(mNewsList.get(pos));
                    StockStockNewsFragment.this.startActivity(intent);
                }
            });

            ((ViewPager)container).addView(view, 0);
            return mPagers.get(position);
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager)container).removeView(mPagers.get(position));
        }

    }
}
