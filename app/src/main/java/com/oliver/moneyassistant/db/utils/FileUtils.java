package com.oliver.moneyassistant.db.utils;

import android.content.Context;
/**
 * Created by Oliver on 2015/3/13.
 */
public class FileUtils {
    public static String getDatabaseFilePath(Context context,String dbFileName){
        String path= context.getFilesDir().getPath()+"/"+dbFileName;
        return path;
    }

}
