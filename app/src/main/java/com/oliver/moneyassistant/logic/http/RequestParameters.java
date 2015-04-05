package com.oliver.moneyassistant.logic.http;

import java.util.Arrays;

/**
 * Created by Oliver on 2015/4/5.
 */

//?newskind=focus&show=title&page=  get news title
//newskind=focus&show=content&titleid= get news content
//newskind=addhot&show=null&titleid= add hot point
public class RequestParameters {
    public static final int NEWS_KIND=0;
    public static final int SHOW=1;
    public static final int PAGE=2;
    public static final int TITLE_ID=3;
    public String mBaseUrl;
    int cnt=-1;
    //newskind=?&show=?&page=?&titleid=?
    public class NewsKind{
        public static final String FOCUS="focus";
        public static final String ADD_HOT="addhot";
    }
    public class Show{
        public static final String TITLE="title";
        public static final String CONTENT="content";
    }
    private String [] arr;
    public RequestParameters(String preUrl){
        arr = new String[10];
        for(int i=0;i<arr.length;i++){
            arr[i]=null;
        }
        this.mBaseUrl = preUrl;
    }
    public void addNewsKind(String kind){
        arr[NEWS_KIND]=kind;
        if(cnt<NEWS_KIND){cnt=NEWS_KIND;}
    }
    public void addShow(String show){
        arr[SHOW]=show;
        if(cnt<SHOW){cnt=SHOW;}
    }
    public void addPage(int page){
        Integer i = Integer.valueOf(page);
        arr[PAGE]=i.toString();
        if(cnt<PAGE){cnt=PAGE;}
    }
    public void addTitleId(int id){
        Integer i = Integer.valueOf(id);
        arr[TITLE_ID] = i.toString();
        if(cnt<TITLE_ID){cnt=TITLE_ID;}
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.mBaseUrl);
        sb.append("?");
        for(int i=0;i<=cnt;i++){
            sb.append(getIndexString(i));
            sb.append("=");
            if(arr[i]==null){
                sb.append("null");
            }else{
                sb.append(arr[i]);
            }
            if(i!=cnt){
                sb.append("&");
            }
        }
        return sb.toString();
    }

    private String getIndexString(int index){
        switch (index){
            case NEWS_KIND:return "newskind";
            case SHOW:return "show";
            case PAGE:return "page";
            case TITLE_ID:return "titleid";
        }
        return null;
    }

}
