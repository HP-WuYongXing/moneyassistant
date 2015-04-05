package com.oliver.moneyassistant.ui.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import com.oliver.moneyassistant.R;
import com.oliver.moneyassistant.ui.StockCompanyOnMarkedFragment;
import com.oliver.moneyassistant.ui.StockFocusNewsFragment;
import com.oliver.moneyassistant.ui.StockGeguNewsFragment;

/**
 * Created by Oliver on 2015/3/27.
 */
public class StockNewsPagerAdapter extends FragmentPagerAdapter{
    private final Resources resources;

    /**
     * Create pager adapter
     *
     * @param resources
     * @param fragmentManager
     */
    public StockNewsPagerAdapter(final Resources resources, final FragmentManager fragmentManager) {
        super(fragmentManager);
        this.resources = resources;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(final int position) {
        final Fragment result;
        switch (position) {
            case 0:
                result = new StockFocusNewsFragment();
                break;
            case 1:
                result = new StockGeguNewsFragment();
                break;
            case 2:
                result = new StockCompanyOnMarkedFragment();
                break;
            default:
                result = null;
                break;
        }
        if (result != null) {
            result.setArguments(new Bundle()); //TODO do we need this?
        }
        return result;
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        switch (position) {
            case 0:
                return resources.getString(R.string.focus);
            case 1:
                return resources.getString(R.string.stocks);
            case 2:
                return resources.getString(R.string.company_on_marked);
            default:
                return null;
        }
    }
}
