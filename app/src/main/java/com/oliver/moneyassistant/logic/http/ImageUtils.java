package com.oliver.moneyassistant.logic.http;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.Base64;

import com.oliver.moneyassistant.constants.ConstantsForFile;

import org.apache.http.HttpConnection;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by Oliver on 2015/3/29.
 */
public class ImageUtils {

   public static BitmapDrawable getContentPic(String url){
       URL imgUrl = null;
       BitmapDrawable bitmapDrawable = null;
       try{
           imgUrl = new URL(url);
       }catch (MalformedURLException e){
           e.printStackTrace();
       }
       try {
           if (imgUrl != null) {
               HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
               bitmapDrawable = new BitmapDrawable(conn.getInputStream());
               conn.disconnect();
           }
       }catch (IOException e){
           e.printStackTrace();
       }
       return bitmapDrawable;
   }

    public static String saveNewsPicture(String imgString)throws JSONException,IOException{
        byte[] bytes = decodeImagStr(imgString);
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            String pathDir= Environment.getExternalStorageDirectory()+ ConstantsForFile.TMP_FILE_DIRECTORY;
            String picName =new Date().getTime()+".jpg";
            File dirFile = new File(pathDir);
            if(!dirFile.exists())dirFile.mkdirs();
            File picFile = new File(pathDir,picName);
            FileOutputStream fileOutputStream = new FileOutputStream(picFile);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
            return picFile.getPath();
        }
        return null;
    }

    private static byte[] decodeImagStr(String imgStr){
        return Base64.decode(imgStr, Base64.DEFAULT);
    }
}
