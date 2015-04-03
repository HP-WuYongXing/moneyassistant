package com.oliver.moneyassistant.test;

import android.test.InstrumentationTestCase;

import com.oliver.moneyassistant.constants.ConstantsForHttp;
import com.oliver.moneyassistant.db.models.NewsContent;
import com.oliver.moneyassistant.db.models.NewsPicture;
import com.oliver.moneyassistant.db.models.NewsTitle;
import com.oliver.moneyassistant.db.models.Paragraph;
import com.oliver.moneyassistant.logic.http.DataUtils;

import java.util.List;

/**
 * Created by Oliver on 2015/3/31.
 */
public class ExampleTest extends InstrumentationTestCase {

    public void testReadJSON()throws Exception{
       String js= DataUtils.readJsonString("http://172.27.35.1:8080/Spider/test");
      // System.out.println("json.lenght: "+js.length());
      System.out.println("content:"+js);
      List<NewsTitle> titleList = NewsTitle.getNewsTitleFromJSON(js);
      System.out.println(titleList.size());
    }

    public void testReadNewsContent(){
        StringBuilder sb = new StringBuilder();
        sb.append(ConstantsForHttp.FINANCE_CE_FOCUS_URL);
        sb.append("?newskind=focus&show=content&titleid=2008");
        String js = DataUtils.readJsonString(sb.toString());
        NewsContent content =  NewsContent.getNewsContentFromJSON(js);

        for(Paragraph p:content.getParList()){
            System.out.println("news content: "+p.getContent());
        }
        if(content.getPicList()!=null){
            for(NewsPicture p:content.getPicList()){
                System.out.println("news picture: "+p.getImagePath());
            }
        }
    }
}
