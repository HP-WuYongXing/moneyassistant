package com.oliver.moneyassistant.logic.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.oliver.moneyassistant.constants.ConstantsForHome;

/**
 * Created by Oliver on 2015/3/16.
 */
public class Config {
   public static void addUpadateMark(Context context){
       SharedPreferences preferences = context.getSharedPreferences(
               ConstantsForHome.UPDATE_FILE_NAME,
               Activity.MODE_PRIVATE);
       SharedPreferences.Editor editor= preferences.edit();
       editor.putBoolean(ConstantsForHome.UPDATE_BOOLEAN_MARK,true);
       editor.commit();
   }
    public static  boolean checkUpdateMark(Context context){
        SharedPreferences preferences = context.getSharedPreferences(ConstantsForHome.UPDATE_FILE_NAME,
                Activity.MODE_PRIVATE);
        return preferences.getBoolean(ConstantsForHome.UPDATE_BOOLEAN_MARK,true);
    }
    public static void removeUpdateMark(Context context){
        SharedPreferences preferences = context.getSharedPreferences(ConstantsForHome.UPDATE_FILE_NAME,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ConstantsForHome.UPDATE_BOOLEAN_MARK,false);
    }
}
