package com.oliver.moneyassistant.db.dao;

import android.content.Context;

import com.ab.db.orm.dao.AbDBDaoImpl;
import com.oliver.moneyassistant.db.DBSDHelper;
import com.oliver.moneyassistant.db.models.IncomeType;

/**
 * Created by Oliver on 2015/3/21.
 */
public class IncomeTypeDao extends AbDBDaoImpl<IncomeType> {
    public IncomeTypeDao(Context context) {
        super(new DBSDHelper(context),IncomeType.class);
    }
}
