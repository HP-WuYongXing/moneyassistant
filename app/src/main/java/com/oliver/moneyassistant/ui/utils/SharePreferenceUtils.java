package com.oliver.moneyassistant.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.oliver.moneyassistant.constants.ConstantsForHome;

/**
 * Created by Oliver on 2015/3/22.
 */
public class SharePreferenceUtils {

    public static void setUpdateMark(Context context){
        SharedPreferences preferences =
                context
                        .getSharedPreferences(ConstantsForHome.SHARED_PREFERENCE_NAME,
                                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor =  preferences.edit();
        editor.putBoolean(ConstantsForHome.SHARED_UPDATE_KEY,true);
        editor.apply();
    }


}
