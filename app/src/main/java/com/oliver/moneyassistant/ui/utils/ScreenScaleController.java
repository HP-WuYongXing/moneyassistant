package com.oliver.moneyassistant.ui.utils;
import android.content.Context;
import android.view.ViewGroup;

/**
 * Created by Oliver on 2015/3/14.
 */
public class ScreenScaleController {
    private Context context;
    public ScreenScaleController(Context context){
        this.context  =context;
    }
    public void changeViewFontSize(ViewGroup parent,int height,int width){
        int adjustedFontSize = getAdjustedFontSize();
    }
    private int getAdjustedFontSize(){
        return 0;
    }
}
