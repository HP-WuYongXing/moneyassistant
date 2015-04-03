package com.oliver.moneyassistant.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.oliver.moneyassistant.R;
import com.oliver.moneyassistant.ui.utils.StockNewsPagerAdapter;
import com.viewpagerindicator.TitlePageIndicator;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Oliver on 2015/3/27.
 */
public class StockNewsFragment extends Fragment{

    private String TAG = "StockNewsFragment";
    private View mRootView;
    @InjectView(R.id.ti_stock_news_top)
    protected TitlePageIndicator mNewsPageIndicator;

    @InjectView(R.id.vp_stock_news_pages)
    protected ViewPager mNewsPager;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.stock_news_view,null);
        Log.i(TAG, "onCreateView....");
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.inject(this,getView());
        mNewsPager.setAdapter(new StockNewsPagerAdapter(getResources(), getChildFragmentManager()));
        mNewsPageIndicator.setViewPager(mNewsPager);
        mNewsPager.setCurrentItem(0);
        Log.i(TAG, "onActivityCreated....");
    }
}
