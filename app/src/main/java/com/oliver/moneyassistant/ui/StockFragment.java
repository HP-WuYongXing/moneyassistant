package com.oliver.moneyassistant.ui;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oliver.moneyassistant.R;

/**
 * Created by Oliver on 2015/3/27.
 */
public class StockFragment extends Fragment implements View.OnClickListener{

    private View mRootView;
    private TextView mTVStockNews;
    private TextView mTVPersonalStockInfo;
    private TextView mTVStockInfo;
    private StockInfoFragment mStockInfoFragment;
    private StockNewsFragment mStockNewsFragment;
    private StockPersonalStockInfoFragment mStockPersonalStockInfoFragment;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.stock_view,null);
        initVariables();
        return mRootView;
    }

    private void initVariables(){
        this.mTVStockNews = (TextView)mRootView.findViewById(R.id.tv_stock_news_info);
        this.mTVStockNews.setOnClickListener(this);
        this.mTVStockInfo = (TextView)mRootView.findViewById(R.id.tv_stock_info);
        this.mTVStockInfo.setOnClickListener(this);
        this.mTVPersonalStockInfo = (TextView)mRootView.findViewById(R.id.tv_personal_stock_info);
        this.mTVPersonalStockInfo.setOnClickListener(this);
        gotoPersonalStock();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_stock_info:gotoStockInfo();break;
            case R.id.tv_stock_news_info:gotoStockNews();break;
            case R.id.tv_personal_stock_info:gotoPersonalStock();break;
        }
    }



    private void gotoStockInfo(){
        FragmentManager manager =getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if(this.mStockInfoFragment==null){
            this.mStockInfoFragment = new StockInfoFragment();
        }
        if(!this.mStockInfoFragment.isVisible()) {
            transaction.replace(R.id.stock_fragment_container, this.mStockInfoFragment);
            transaction.commit();
        }
    }

    private void gotoStockNews(){
        FragmentManager manager =getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if(this.mStockNewsFragment==null){
            this.mStockNewsFragment = new StockNewsFragment();
        }
        if(!this.mStockNewsFragment.isVisible()) {
            transaction.replace(R.id.stock_fragment_container, this.mStockNewsFragment);
            transaction.commit();
        }
    }

    private void gotoPersonalStock() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (this.mStockPersonalStockInfoFragment == null) {
            this.mStockPersonalStockInfoFragment = new StockPersonalStockInfoFragment();
        }
        if (!this.mStockPersonalStockInfoFragment.isVisible()) {
            transaction.replace(R.id.stock_fragment_container, this.mStockPersonalStockInfoFragment);
            transaction.commit();

        }
    }
}
