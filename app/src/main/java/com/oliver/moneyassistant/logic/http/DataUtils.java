package com.oliver.moneyassistant.logic.http;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Oliver on 2015/3/27.
 */

public class DataUtils{

      private static final String TAG="DataUtils";

        public static Document doGet(String urlStr) throws CommonException{

            Document doc =null;
            try {
                doc = Jsoup.connect(urlStr)
                        .header("User-Agent",
                                "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2"
                        ).get();
            }catch (IOException e){
                e.printStackTrace();
            }
            return doc;
           /* StringBuffer sb = new StringBuffer();
            HttpURLConnection conn =null;
            try{
                URL url = new URL(urlStr);
                conn= (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Log.i(TAG, "response_code: " + conn.getResponseCode());
                if(conn.getResponseCode() == 200){
                    InputStream is = conn.getInputStream();
                    int len = 0;
                    byte[] buf = new byte[1024];
                    while((len = is.read(buf)) != -1){
                        sb.append(new String(buf, 0, len, "gb2312"));
                    }
                    is.close();

                }else{
                    throw new CommonException("访问网络失败00");
                }

            } catch (Exception e) {
                e.printStackTrace();
                throw new CommonException("访问网络失败11");
            }finally {
                conn.disconnect();
            }
            return sb.toString();*/
        }

    public static String sendHttpRequest(String urlStr){
        StringBuffer sb  = new StringBuffer();
        HttpURLConnection conn=null;
        try{
            URL url = new URL(urlStr);
            conn =(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);
            if(conn.getResponseCode()==200){
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len=0;
                while(true){
                    len = is.read(buffer);
                    if(len==-1){
                        break;
                    }else {
                        outputStream.write(buffer);
                    }
                }
                byte[] result = outputStream.toByteArray();
                String str = new String(result, 0, result.length, "gbk");
                sb.append(str);
                outputStream.close();
                is.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(conn!=null) {
                conn.disconnect();
            }
        }
        return sb.toString();
    }

}
