package com.oliver.moneyassistant.logic.http;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import org.apache.http.HttpConnection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
}
