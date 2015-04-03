package com.oliver.moneyassistant.db.models;

import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;
import android.util.Log;

import com.oliver.moneyassistant.constants.ConstantsForFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Oliver on 2015/3/29.
 */
public class NewsContent implements Parcelable{
    private int id;
    private String subTitle;
    private String source;
    private int titleId;
    private String time;
    private List<Paragraph> parList;
    private List<NewsPicture>picList;
    private int maxLength;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Paragraph> getParList() {
        return parList;
    }

    public void setParList(List<Paragraph> parList) {
        this.parList = parList;
    }

    public List<NewsPicture> getPicList() {
        return picList;
    }

    public void setPicList(List<NewsPicture> picList) {
        this.picList = picList;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public NewsContent(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.subTitle);
        dest.writeString(this.source);
        dest.writeInt(titleId);
        dest.writeInt(maxLength);
        dest.writeString(time);
        dest.writeList(parList);
        dest.writeList(picList);
    }

    public static final Creator CREATOR = new Creator<NewsContent>(){
        @Override
        public NewsContent createFromParcel(Parcel source) {
            NewsContent newsContent = new NewsContent();
            newsContent.id = source.readInt();
            newsContent.subTitle = source.readString();
            newsContent.source = source.readString();
            newsContent.titleId = source.readInt();
            newsContent.maxLength = source.readInt();
            newsContent.time = source.readString();
            source.readList(newsContent.parList,Paragraph.class.getClassLoader());
            source.readList(newsContent.picList,NewsPicture.class.getClassLoader());
            return newsContent;
        }

        @Override
        public NewsContent[] newArray(int size) {
            return new NewsContent[size];
        }
    };

    public static NewsContent getNewsContentFromJSON(String json){
        NewsContent content =  new NewsContent();
        try {
            JSONObject jo = new JSONObject(json);
            content.setId(jo.getInt("id"));
            content.setTime(jo.getString("time"));
            content.setSource(jo.getString("source"));
            content.setSubTitle(jo.getString("subTitle"));
            content.setMaxLength(jo.getInt("maxLength"));
            content.setTitleId(jo.getInt("titleId"));
            JSONArray JSONParArr = jo.getJSONArray("paragraphList");
            if(JSONParArr!=null) {
                List<Paragraph> parList = new ArrayList<>();
                int len = JSONParArr.length();
                for (int i = 0; i < len; i++) {
                    JSONObject jsonObject = (JSONObject) JSONParArr.get(i);
                    Paragraph par = new Paragraph();
                    par.setId(jsonObject.getInt("id"));
                    par.setContentId(jsonObject.getInt("contentId"));
                    par.setOrderNumber(jsonObject.getInt("orderNumber"));
                    par.setContent(jsonObject.getString("content"));
                    parList.add(par);
                }
                content.setParList(parList);
            }

            JSONArray JSONPicArr = jo.getJSONArray("pictureList");
            Log.d("test","JSONPicArr==null?"+(JSONPicArr==null));
            if(JSONPicArr!=null){
                List<NewsPicture> picList = new ArrayList<>();
                int len = JSONPicArr.length();
                Log.d("test","JSONPicArr len?"+len);
                for (int i = 0; i < len; i++) {
                    Log.d("test","in for i?"+i);
                    JSONObject jsonObject = (JSONObject) JSONPicArr.get(i);
                    NewsPicture pic = new NewsPicture();
                    pic.setId(jsonObject.getInt("id"));
                    pic.setContentId(jsonObject.getInt("contentId"));
                    pic.setOrderNumber(jsonObject.getInt("orderNumber"));
                    String imgString = jsonObject.getString("image");
                    String imgPath = saveNewsPicture(imgString);
                    Log.d("test","imgPath: "+imgPath);
                    pic.setImagePath(imgPath);
                    picList.add(pic);
                }
                content.setPicList(picList);
                Log.d("test","content pic list: "+content.getPicList());
            }
        }catch (JSONException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return content;
    }

    private static String saveNewsPicture(String imgString)throws JSONException,IOException{
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
       return Base64.decode(imgStr,Base64.DEFAULT);
    }

    @Override
    public String toString() {
        return "NewsContent{" +
                "id=" + id +
                ", subTitle='" + subTitle + '\'' +
                ", source='" + source + '\'' +
                ", titleId=" + titleId +
                ", time='" + time + '\'' +
                ", parList=" + parList +
                ", picList=" + picList +
                ", maxLength=" + maxLength +
                '}';
    }
}
