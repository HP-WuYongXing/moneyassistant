package com.oliver.moneyassistant.db.dao;

import android.content.Context;

import com.oliver.moneyassistant.db.DBSDHelper;
import com.oliver.moneyassistant.db.models.OutcomeItem;
import com.ab.db.orm.dao.AbDBDaoImpl;

public class OutcomeDao extends AbDBDaoImpl<OutcomeItem> {

	public OutcomeDao(Context context) {
		super(new DBSDHelper(context),OutcomeItem.class);
	}

}
