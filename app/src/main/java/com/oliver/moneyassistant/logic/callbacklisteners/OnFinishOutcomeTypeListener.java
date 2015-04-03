package com.oliver.moneyassistant.logic.callbacklisteners;

import com.oliver.moneyassistant.db.models.OutcomeType;

/**
 * Created by Oliver on 2015/3/17.
 */
public interface OnFinishOutcomeTypeListener {
    public void addOutcomeTypeInfo(OutcomeType type);
}
