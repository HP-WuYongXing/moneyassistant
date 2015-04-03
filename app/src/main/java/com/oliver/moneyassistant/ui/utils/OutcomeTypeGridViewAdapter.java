package com.oliver.moneyassistant.ui.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.oliver.moneyassistant.R;
import com.oliver.moneyassistant.db.models.OutcomeType;

import java.util.List;
import java.util.Map;

/**
 * Created by Oliver on 2015/3/17.
 */
public class OutcomeTypeGridViewAdapter extends BaseAdapter{

    private Context context;
    private List<OutcomeType> list;

    public OutcomeTypeGridViewAdapter(Context context, List<OutcomeType> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = new ViewHolder();
        if(convertView==null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.type_item_cell,null);
            vh.iv = (ImageView)convertView.findViewById(R.id.iv_img);
            vh.tv = (TextView)convertView.findViewById(R.id.tv_text);
            convertView.setTag(vh);
        }else{
            vh=(ViewHolder)convertView.getTag();
        }

        OutcomeType type = list.get(position);
        String text = type.getTypeText();
        Drawable img= this.context.getResources().getDrawable(type.getResId());
        vh.tv.setText(text);
        vh.iv.setImageDrawable(img);
        return convertView;
    }
    class ViewHolder{
        public ImageView iv;
        public TextView tv;
    }
}
