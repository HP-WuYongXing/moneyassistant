package com.oliver.moneyassistant.ui.utils;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.oliver.moneyassistant.R;
import com.oliver.moneyassistant.db.models.OutcomeItem;

import org.w3c.dom.Text;

public class TimelineAdapter extends BaseAdapter {

	private Context context;
	private List<Map<String, Object>> list;

	public TimelineAdapter(Context context, List<Map<String, Object>> list) {
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
		ViewHolder viewHolder;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.outcome_item_list_cell, null);
			viewHolder = new ViewHolder();
			viewHolder.typeText = (TextView)convertView.findViewById(R.id.outcome_type_text);
            viewHolder.money = (TextView)convertView.findViewById(R.id.outcome_money);
            viewHolder.time =(TextView)convertView.findViewById(R.id.outcome_time);
            viewHolder.address=(TextView)convertView.findViewById(R.id.address_header);
            viewHolder.type_icon =(ImageView)convertView.findViewById(R.id.image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
        Map<String,Object> item = list.get(position);
		viewHolder.money.setText((String)item.get("money"));
        viewHolder.address.setText((String)item.get("address"));
        viewHolder.time.setText((String)item.get("time"));
        viewHolder.type_icon.setImageResource((Integer)item.get("type_icon"));
        viewHolder.typeText.setText((String)item.get("type_text"));
		return convertView;
	}

	static class ViewHolder {
		public TextView typeText;
        public TextView money;
        public TextView address;
        public TextView time;
        public ImageView type_icon;
	}
}
