package com.oliver.moneyassistant.db.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oliver on 2015/3/27.
 */
public class NewsTitle implements Parcelable {

    private int id;
    private String title;
    private String link;
    private String time;
    private String content;
    private int newsType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNewsType() {
        return newsType;
    }

    public void setNewsType(int newsType) {
        this.newsType = newsType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.link);
        dest.writeString(this.time);
        dest.writeString(this.content);
        dest.writeInt(this.newsType);
    }

    public NewsTitle(){}
    public static final Creator CREATOR = new Creator<NewsTitle>() {
        @Override
        public NewsTitle createFromParcel(Parcel source) {
            NewsTitle item = new NewsTitle();
            item.id =source.readInt();
            item.title = source.readString();
            item.link = source.readString();
            item.time = source.readString();
            item.content = source.readString();
            item.newsType = source.readInt();
            return item;
        }

        @Override
        public NewsTitle[] newArray(int size) {
            return new NewsTitle[size];
        }

    };

    @Override
    public String toString() {
        return "NewsTitle{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", time='" + time + '\'' +
                ", content='" + content + '\'' +
                ", newsType=" + newsType +
                '}';
    }

    public static List<NewsTitle> getNewsTitleFromJSON(String json){
        List<NewsTitle> list=new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            int len = jsonArray.length();
            System.out.println("jsonArray len:"+len);
            for(int i=0;i<len;i++){
                JSONObject jo =(JSONObject) jsonArray.get(i);
                NewsTitle item = new NewsTitle();
                item.setId(jo.getInt("id"));
                item.setNewsType(jo.getInt("type"));
                item.setLink(jo.getString("link"));
                item.setTime(jo.getString("time"));
                item.setTitle(jo.getString("title"));
                list.add(item);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return list;
    }
}
