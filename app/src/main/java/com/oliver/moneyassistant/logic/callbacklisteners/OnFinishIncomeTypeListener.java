package com.oliver.moneyassistant.logic.callbacklisteners;

import com.oliver.moneyassistant.db.models.IncomeType;

/**
 * Created by Oliver on 2015/3/17.
 */
public interface OnFinishIncomeTypeListener {
    public void addIncomeTypeInfo(IncomeType type);
}
