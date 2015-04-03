package com.oliver.moneyassistant.db.utils;

/**
 * Created by Oliver on 2015/3/16.
 */
public class TypeConvertor {
    public static String getOutcomeTypeString(int code){
        String str ="";
        switch (code){
            case 0:str ="一般";break;
            case 1:str ="饮食";break;
            case 2:str ="交通";break;
            case 3:str ="教育";break;
            case 4:str ="约会";break;
            case 5:str ="化妆";break;
            case 6:str ="数码";break;
            case 7:str ="衣着";break;
            case 8:str ="家具";break;
            case 9:str ="娱乐";break;
        }
       return str;
    }

    /*
    <item>工资</item>
        <item>债券</item>
        <item>股票</item>
        <item>房租</item>
        <item>赠送</item>
        <item>买卖</item>
    * */
    public static String getIncomeTypeString(int code){
        String str= "";
        switch (code){
            case 0:str="工资";break;
            case 1:str="股票";break;
            case 2:str="房租";break;
            case 3:str="赠送";break;
            case 4:str="买卖";break;
            case 5:str="其他";break;
        }
        return str;
    }
}
