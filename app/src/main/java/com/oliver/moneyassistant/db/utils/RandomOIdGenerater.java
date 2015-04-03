package com.oliver.moneyassistant.db.utils;

public class RandomOIdGenerater {

	public static String getRandomId(){
		double d =  Math.random();
		return Integer.valueOf((int)(d*100000)).toString();
	}

    public static int getRandomOutcomeType(){
        double d = Math.random();
        return (int)(d*10);//0-9的随机数
    }

    public static long getRandomeTimeOfThisMonth(){
        double d = Math.random();
        long nowTme = TimeUtils.getNow();
        long preTime = TimeUtils.getFirstDayOfThisMonth();
        long result = preTime+(long)((nowTme-preTime)*d);
        return result;
    }

    public static float getRandomMoneyOfDay(){
        double d= Math.random();
        return (float)((200.0f*d)+100.0f);
    }

    public static int getRandomIncomeType(){
        double d = Math.random();
        return (int)(d*6);
    }

}
