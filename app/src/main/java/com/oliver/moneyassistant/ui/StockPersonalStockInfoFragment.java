package com.oliver.moneyassistant.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.oliver.moneyassistant.R;
/**
 * Created by Oliver on 2015/3/27.
 */
public class StockPersonalStockInfoFragment extends Fragment{

    private View mRootView;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.stock_personal_stock_info_view,null);
        return mRootView;
    }
}
