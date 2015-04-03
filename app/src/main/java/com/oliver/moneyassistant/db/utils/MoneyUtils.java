package com.oliver.moneyassistant.db.utils;

import java.text.DecimalFormat;

public class MoneyUtils {

	public static String displayMoney(double num){
		DecimalFormat df = new DecimalFormat("#0.00");
        if(Math.abs(num-0)<0.01){
            return "0.00";
        }
		String d = df.format(num);
		return d;
	}
}
