package com.oliver.moneyassistant.ui.utils;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;

import com.ab.util.AbFileUtil;
import com.ab.util.AbImageUtil;
import com.oliver.moneyassistant.R;
import android.widget.ImageView;
import java.util.*;
import java.io.File;
import android.util.Log;

/**
 * Created by Oliver on 2015/3/19.
 */
public class OutcomePicAdapter extends BaseAdapter{
    private Context mContext;
    private List<String> mList;
    private int mWidth;
    private int mHeight;
    private String TAG = "OutcomePicAdapter";

    public OutcomePicAdapter(Context context,List<String>list,int width,int height){
            this.mContext =context;
            this.mList= list;
            this.mWidth = width;
            this.mHeight = height;
    }

    public void addItem(int position, String path){
        this.mList.add(position,path);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        Log.i(TAG,"getcount: "+mList.size());
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
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewHolder vh = new ViewHolder();
        if(convertView==null){
            convertView = inflater.inflate(R.layout.outcome_picture_view_cell,null);
            vh.iv = (ImageView)convertView.findViewById(R.id.img);
            convertView.setTag(vh);
        }else{
            vh=(ViewHolder)convertView.getTag();
        }

        String path = mList.get(position);
        if(path.indexOf("/")==-1){
            int resId = Integer.valueOf(path);
            vh.iv.setImageResource(resId);
        }else{
            Bitmap bitmap = AbFileUtil.getBitmapFromSD(new File(path), AbImageUtil.SCALEIMG,mWidth,mHeight);
            vh.iv.setImageBitmap(bitmap);
        }
        vh.iv.setAdjustViewBounds(true);
        return convertView;
    }

    private class ViewHolder{
        public ImageView iv;
    }
}
