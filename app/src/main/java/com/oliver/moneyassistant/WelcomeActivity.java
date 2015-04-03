package com.oliver.moneyassistant;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import java.util.List;

import android.widget.ProgressBar;

import com.oliver.moneyassistant.constants.ConstantsForHome;
import com.oliver.moneyassistant.db.dao.IncomeTypeDao;
import com.oliver.moneyassistant.db.dao.OutcomeTypeDao;
import com.oliver.moneyassistant.db.models.IncomeType;
import com.oliver.moneyassistant.db.models.OutcomeItem;
import com.oliver.moneyassistant.db.models.OutcomeType;

/**
 * Created by Oliver on 2015/3/12.
 */
public class WelcomeActivity extends Activity {
    private String TAG = "WelcomeActivity";
    private ProgressBar mProgressBar;
    private List<OutcomeItem> mList;
    private static final int PROGRESS_MAX_VALUE=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.welcome_view);
        mProgressBar = (ProgressBar)this.findViewById(R.id.pb_progressbar_welcome);
        new MyAsyncTask().execute(100);
    }

    private  boolean checkLoadMark(){
        SharedPreferences preferences = this.getSharedPreferences(ConstantsForHome.SHARED_PREFERENCE_NAME,
                Activity.MODE_PRIVATE);
        return preferences.getBoolean(ConstantsForHome.LOAD_ONCE_KEY,false);
    }

    private  void setLoadMark(){
        SharedPreferences preferences = this.getSharedPreferences(ConstantsForHome.SHARED_PREFERENCE_NAME,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ConstantsForHome.LOAD_ONCE_KEY,true);
        editor.commit();
    }

    private class MyAsyncTask extends AsyncTask<Integer,Integer,String>{
        @Override
        protected String doInBackground(Integer... params) {
          /*初始化类型数据库*/
          /*outcomeType*/
          if(!checkLoadMark()) {

              OutcomeTypeDao outcomeTypeDao = new OutcomeTypeDao(WelcomeActivity.this);
              outcomeTypeDao.startWritableDatabase(false);
              String outcomeTypeArr[] = WelcomeActivity.this.getResources()
                      .getStringArray(R.array.outcome_type);
              for (int i = 0; i < outcomeTypeArr.length; i++) {
                  OutcomeType outcomeType = new OutcomeType();
                  outcomeType.setResId(R.drawable.ic_launcher);
                  outcomeType.setTypeText(outcomeTypeArr[i]);
                  outcomeType.setTypeId(i);
                  outcomeTypeDao.insert(outcomeType);
              }
              outcomeTypeDao.closeDatabase();

         /*incomeType*/
              IncomeTypeDao incomeTypeDao = new IncomeTypeDao(WelcomeActivity.this);
              incomeTypeDao.startWritableDatabase(false);
              String incomeTypeArr[] = WelcomeActivity.this.getResources()
                      .getStringArray(R.array.income_type);
              for (int i = 0; i < incomeTypeArr.length; i++) {
                  IncomeType incomeType = new IncomeType();
                  incomeType.setResId(R.drawable.ic_launcher);
                  incomeType.setTypeId(i);
                  incomeType.setTypeText(incomeTypeArr[i]);
                  incomeTypeDao.insert(incomeType);
              }
              incomeTypeDao.closeDatabase();
              setLoadMark();
          }

            try{
                for(int i=0;i<=100;i+=10){
                    publishProgress(i);
                        Thread.sleep(100);
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            return "yes";
        }

        @Override
        protected void onPreExecute() {
            mProgressBar.setProgress(0);
            mProgressBar.setMax(PROGRESS_MAX_VALUE);
        }

        @Override
        protected void onPostExecute(String s) {
            Intent intent = new Intent();
            intent.setClass(WelcomeActivity.this, MainActivity.class);
            WelcomeActivity.this.startActivity(intent);
            WelcomeActivity.this.finish();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
          mProgressBar.setProgress(values[0]);
        }
    }
}
