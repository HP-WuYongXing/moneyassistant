package com.oliver.moneyassistant.ui.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;

/**
 * Created by Oliver on 2015/3/20.
 */
public class DialogUtis {
    public static void showDialog(Context context, String str){
        new AlertDialog.Builder(context).setTitle("错误提示")
                .setMessage(str)
                .setNegativeButton("我知道了",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
}
