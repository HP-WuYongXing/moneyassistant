package com.oliver.moneyassistant.ui.utils;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.oliver.moneyassistant.R;
import com.oliver.moneyassistant.db.models.Budget;
import com.oliver.moneyassistant.db.utils.MoneyUtils;
import com.oliver.moneyassistant.db.utils.TimeUtils;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Oliver on 2015/3/23.
 */
public class BudgetDetailsListAdapter extends BaseAdapter{
    private List<Budget> mBudgetDetailsList;

    public BudgetDetailsListAdapter(List<Budget> list){
        this.mBudgetDetailsList = list;
    }

    @Override
    public int getCount() {
        return this.mBudgetDetailsList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mBudgetDetailsList.get(position);
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
            convertView = inflater.inflate(R.layout.show_budget_details_list_cell,null);
            vh.makeTime =(TextView)convertView.findViewById(R.id.tv_budget_make_time);
            vh.startTime = (TextView)convertView.findViewById(R.id.tv_budget_start_time);
            vh.endTime =(TextView)convertView.findViewById(R.id.tv_budget_end_time);
            vh.money = (TextView)convertView.findViewById(R.id.tv_cell_budget_money);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();
        }

        Budget budget = this.mBudgetDetailsList.get(position);
        vh.makeTime.setText(TimeUtils.getTimeStringWithoutSecond(budget.getBudgetTime()));
        vh.startTime.setText(TimeUtils.getTimeStringWithoutSecond(budget.getStartTime()));
        vh.endTime.setText(TimeUtils.getTimeStringWithoutSecond(budget.getEndTime()));
        vh.money.setText(MoneyUtils.displayMoney(budget.getMoney()));
        return convertView;
    }
    private class ViewHolder{
        public TextView makeTime;
        public TextView startTime;
        public TextView endTime;
        public TextView money;
    }
}
