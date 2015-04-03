package com.oliver.moneyassistant.ui.utils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import java.util.List;
import android.widget.TextView;
import android.widget.ImageView;
import com.oliver.moneyassistant.db.models.IncomeItem;
import com.oliver.moneyassistant.R;
import com.oliver.moneyassistant.db.utils.MoneyUtils;
import com.oliver.moneyassistant.db.utils.TimeUtils;

/**
 * Created by Oliver on 2015/3/21.
 */
public class IncomeListViewAdapter extends BaseAdapter{
    private String TAG= "IncomeListViewAdapter";
    private Context mContext;
    private List<IncomeItem> mList;

    public IncomeListViewAdapter(Context context,List<IncomeItem> list){
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        Log.i(TAG, "get count: " + mList.size());
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(convertView==null){
            convertView = inflater.inflate(R.layout.income_list_cell_view,null);
            vh.time=(TextView)convertView.findViewById(R.id.tv_income_time);
            vh.typeIcon =(ImageView)convertView.findViewById(R.id.iv_type_icon);
            vh.typeText = (TextView)convertView.findViewById(R.id.tv_type_text);
            vh.incomeMoney = (TextView)convertView.findViewById(R.id.tv_income_money_text);
            vh.incomeDescribe =(TextView)convertView.findViewById(R.id.tv_income_describe_text);
            convertView.setTag(vh);
        }else{
            vh=(ViewHolder)convertView.getTag();
        }

        IncomeItem item = mList.get(position);
        vh.time.setText( TimeUtils.getTimeStringWithoutSecond(item.getIncomeTime()));
        vh.typeIcon.setImageResource(item.getIncomeType().getResId());
        vh.typeText.setText(item.getIncomeType().getTypeText());
        String str =MoneyUtils.displayMoney(item.getIncomeMoney());
        vh.incomeMoney.setText(str);
        vh.incomeDescribe.setText(item.getIncomeDescribe());
        return convertView;
    }

    private class ViewHolder{
        public TextView time;
        public ImageView typeIcon;
        public TextView typeText;
        public TextView incomeMoney;
        public TextView incomeDescribe;
    }
}
