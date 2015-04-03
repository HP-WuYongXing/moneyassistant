package com.oliver.moneyassistant.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.util.AbImageUtil;
import com.oliver.moneyassistant.R;
import com.oliver.moneyassistant.constants.ConstantsForStock;
import com.oliver.moneyassistant.db.models.NewsContent;
import com.oliver.moneyassistant.db.models.NewsPicture;
import com.oliver.moneyassistant.db.models.NewsTitle;
import com.oliver.moneyassistant.db.models.Paragraph;
import com.oliver.moneyassistant.logic.http.ImageUtils;
import com.oliver.moneyassistant.logic.runnables.GetStockNewsContent;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StockNewsContentActivity extends ActionBarActivity {

    private static final String TAG ="StockNewsContent";
    private NewsTitle mNewsItem;

   @InjectView(R.id.tv_news_title)
   protected TextView mTVNewsTitle;

   @InjectView(R.id.tv_news_time)
   protected TextView mTVNewsTime;

   @InjectView(R.id.tv_news_source)
   protected TextView mTVNewsSource;

   @InjectView(R.id.ll_news_content)
   protected LinearLayout mLLNewsContent;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ConstantsForStock.STOCK_NEWS_CONTENT_HANDLER:
                    showNewsContent(msg.getData());
                    Log.i(TAG, "get msg... ...");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_news_content_activity);
        ButterKnife.inject(this);
        this.mNewsItem = this.getIntent().getParcelableExtra(ConstantsForStock.STOCK_NEWS_ITEM);

        getNewsContent();
    }

    private void getNewsContent(){
             Log.d(TAG,"get news content thread started..."+this.mNewsItem);
            new Thread(new GetStockNewsContent(this, mHandler, this.mNewsItem))
                    .start();
    }

    private void showNewsContent(Bundle data){
        NewsContent content = data.getParcelable(ConstantsForStock.STOCK_NEWS_CONTENT);
        Log.i(TAG, "content: "+content.toString());
        this.mTVNewsTitle.setText(content.getSubTitle());
        this.mTVNewsTime.setText(content.getTime());
        this.mTVNewsSource.setText(content.getSource());
        int maxlength = content.getMaxLength();
        Log.i(TAG, "content maxlength: "+maxlength);
        List<Paragraph> parList = content.getParList();
        List<NewsPicture> picList = content.getPicList();
        if(parList!=null&&parList.size()!=0){
            Log.i(TAG, "parList length: "+parList.size());
        }
        if(picList!=null&&picList.size()!=0){
            Log.i(TAG, "picList length: "+picList.size());
        }
        for(int i=0;i<maxlength;i++){
            if(parList!=null&&parList.size()!=0){
                for(Paragraph p:parList){
                    if(i==p.getOrderNumber()){
                        addTextView(p.getContent());
                    }
                }
            }
            if(picList!=null&&picList.size()!=0){
                for(NewsPicture p:picList){
                    if(i==p.getOrderNumber()){
                        addImageView(p.getImagePath());
                    }
                }
            }
        }
    }

    private void addTextView(String str){
        Log.i(TAG, "add text view: "+str);
        TextView textView = new TextView(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(lp);
        textView.setText(str);
        mLLNewsContent.addView(textView);
    }

    private void addImageView(String path){
        Log.i(TAG, "add imagae view: "+path);
        ImageView imageView = new ImageView(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        imageView.setImageBitmap(bitmap);
        mLLNewsContent.addView(imageView);
    }

    private void getNewsImage(String url){

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stock_news_content_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class ImageTask extends AsyncTask<String,String,BitmapDrawable>{

        @Override
        protected BitmapDrawable doInBackground(String... params) {
           return ImageUtils.getContentPic(params[0]);
        }

        @Override
        protected void onPostExecute(BitmapDrawable bitmapDrawable) {
        }
    }
}
