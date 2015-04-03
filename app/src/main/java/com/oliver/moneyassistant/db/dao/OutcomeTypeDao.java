package com.oliver.moneyassistant.db.dao;

import android.content.Context;

import com.ab.db.orm.dao.AbDBDaoImpl;
import com.oliver.moneyassistant.db.DBSDHelper;
import com.oliver.moneyassistant.db.models.OutcomeType;

/**
 * Created by Oliver on 2015/3/21.
 */
public class OutcomeTypeDao extends AbDBDaoImpl<OutcomeType> {
    public OutcomeTypeDao(Context context) {
        super(new DBSDHelper(context),OutcomeType.class);
    }
}
