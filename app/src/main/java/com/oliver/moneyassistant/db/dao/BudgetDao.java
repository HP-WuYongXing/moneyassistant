package com.oliver.moneyassistant.db.dao;

import android.content.Context;

import com.ab.db.orm.dao.AbDBDaoImpl;
import com.oliver.moneyassistant.db.DBSDHelper;
import com.oliver.moneyassistant.db.models.Budget;

/**
 * Created by Oliver on 2015/3/15.
 */
public class BudgetDao extends AbDBDaoImpl<Budget> {
    public BudgetDao(Context context) {
        super(new DBSDHelper(context),Budget.class);
    }
}
