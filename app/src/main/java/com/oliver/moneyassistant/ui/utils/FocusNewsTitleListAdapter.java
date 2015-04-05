package com.oliver.moneyassistant.ui.utils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oliver.moneyassistant.R;
import com.oliver.moneyassistant.db.models.NewsTitle;

import java.util.List;

/**
 * Created by Oliver on 2015/4/4.
 */
public class FocusNewsTitleListAdapter extends BaseAdapter{
    private static final String TAG="FocusNewsTitleAdapter";

    private List<NewsTitle> mTitleList;

    public FocusNewsTitleListAdapter(List<NewsTitle> titleList){
        mTitleList = titleList;
    }

    @Override
    public int getCount() {
        return mTitleList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTitleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewHolder vh = new ViewHolder();
        if(convertView ==null){
            convertView = inflater.inflate(R.layout.stock_news_dapan_view_cell,null);
            vh.title = (TextView)convertView.findViewById(R.id.tv_stock_news_title);
            vh.hot = (TextView)convertView.findViewById(R.id.tv_stock_news_hot_degree);
            vh.time = (TextView)convertView.findViewById(R.id.tv_news_title_time);
            Log.i(TAG, "vh.hot: " + (vh.hot==null));
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();
        }
        vh.title.setText(this.mTitleList.get(position).getTitle());
        Log.i(TAG, "hot: " + this.mTitleList.get(position).getHot());
        vh.hot.setText(""+this.mTitleList.get(position).getHot());
        vh.time.setText(this.mTitleList.get(position).getTime());
        return convertView;
    }

    private class ViewHolder {
        public TextView title;
        public TextView hot;
        public TextView time;
    }
}
