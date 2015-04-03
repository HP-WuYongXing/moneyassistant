package com.oliver.moneyassistant.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.oliver.moneyassistant.R;
import com.viewpagerindicator.TitlePageIndicator;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Oliver on 2015/3/27.
 */
public class StockInfoFragment extends Fragment{

    private View mRootView;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.stock_info_view,null);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
