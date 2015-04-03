package com.oliver.moneyassistant.db.dao;

import com.ab.db.orm.dao.AbDBDaoImpl;
import com.oliver.moneyassistant.db.DBSDHelper;
import com.oliver.moneyassistant.db.models.IncomeItem;
import com.oliver.moneyassistant.db.models.OutcomeItem;

import android.content.Context;
/**
 * Created by Oliver on 2015/3/15.
 */
public class IncomeDao extends AbDBDaoImpl<IncomeItem> {
    public IncomeDao(Context context) {
        super(new DBSDHelper(context),IncomeItem.class);
    }
}
