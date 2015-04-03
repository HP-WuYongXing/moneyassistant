package com.oliver.moneyassistant.logic.callbacklisteners;

import com.oliver.moneyassistant.db.models.ItemAddress;

public interface OnFinishOutcomeAddressListener {
	public void addOutomeAddrInfo(ItemAddress addr);
}
