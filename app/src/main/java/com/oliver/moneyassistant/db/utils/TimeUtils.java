package com.oliver.moneyassistant.db.utils;

import com.oliver.moneyassistant.db.models.Budget;
import com.oliver.moneyassistant.wrapperclass.DayWrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.TimeZone;
import android.util.Log;
/**
 * Created by Oliver on 2015/3/13.
 */
public class TimeUtils {
    private static final String TAG = "TimeUtils";
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static String getToday(){
        long now = new Date().getTime();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(now);
        int year  = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        month++;
        int day   =c.get(Calendar.DAY_OF_MONTH);
        String today =""+year+"-"+month+"-"+day;
        return today;
    }

    private static String getYesterday(){
        long now = new Date().getTime();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(now);
        int year  = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        month++;
        int day   =c.get(Calendar.DAY_OF_MONTH)-1;
        String today =""+year+"-"+month+"-"+day;
        return today;
    }

    public static long getLastSecondOfYesterday(){
        StringBuilder sb= new StringBuilder();
        sb.append(getYesterday()).append(" 23:59:59");
        long time=0l;
        try{
            Date d=sdf.parse(sb.toString());
            time =d.getTime();
            time = adjustTimeOffsetAdd(time);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return time;
    }

    public static long getLastSecondOfToday(){
        StringBuilder sb= new StringBuilder();
        sb.append(getToday()).append(" 23:59:59");
        long time =0l;
        try{
            time = sdf.parse(sb.toString()).getTime();
            time = adjustTimeOffsetAdd(time);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return time;
    }

    public static long getNow(){
        return new Date().getTime();
    }

    public static String getTheDay(Date d){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(d);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month++;///modify
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year+"-"+month+"-"+day;
    }

    public static long getFirstSecondOfTheDay(Date d){

        StringBuilder sb = new StringBuilder(getTheDay(d));
        long l=0l;
        try {
            sb.append(" ");
            sb.append("00:00:00");
            l = sdf.parse(sb.toString()).getTime();
            l= adjustTimeOffsetAdd(l);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return l;
    }

    public static long getLastSecondOfTheDay(Date d){
        System.out.println("getTheDay: "+getTheDay(d));
        StringBuilder sb = new StringBuilder(getTheDay(d));
        sb.append(" ");
        sb.append("23:59:59");
        long l=0;
        try{
            l = sdf.parse(sb.toString()).getTime();
            //l= adjustTimeOffsetAdd(l);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return l;
    }

    public static ArrayList<DayWrapper> getDayWrappersWithinMonth(){
        ArrayList<DayWrapper> list= new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int  year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month++;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        try {
            for (int i = 1; i <= day; i++) {
                String a = year+"-"+month+"-"+i+" "+"00:00:00";
                String b =  year+"-"+month+"-"+i+" "+"23:59:59";
                long time1 = adjustTimeOffsetAdd(sdf.parse(a).getTime());
                long time2 = adjustTimeOffsetAdd(sdf.parse(b).getTime());
                list.add(new DayWrapper(time1,time2));
            }
        }catch(ParseException e){
            e.printStackTrace();
        }
        return list;
    }


    public static ArrayList<DayWrapper> getDayWrapperWithBudget(Budget budget){
        ArrayList<DayWrapper> list= new ArrayList<>();
        long startTime = budget.getStartTime();
        long endTime = budget.getEndTime();
        long during = endTime - startTime;
        long period = getOneDayPeriod();
        if((during%period==0&&during/period==1)||during/period==0){
            list.add(new DayWrapper(startTime, endTime));
            return list;
        }else if((during%period==0&&during/period==2)||during/period==1){
            long split = getLastSecondOfTheDay(new Date(startTime));
            list.add(new DayWrapper(startTime, split));
            list.add(new DayWrapper(split+1000L, endTime));
        }else{
            long daypoint = getLastSecondOfTheDay(new Date(startTime));
            list.add(new DayWrapper(startTime, daypoint));
            long daypoint2 = getLastSecondOfTheDay(new Date(endTime))-period;
            for(long i=daypoint+1000L;i<daypoint2;i=i+period){
                list.add(new DayWrapper(i, i+period-1000L));
            }
            list.add(new DayWrapper(daypoint2+1000L, endTime));
            return list;
        }
        return list;
    }

    public static long getOneDayPeriod(){
        long p = 1000*3600*24;
        return p;
    }



    public static String getTimeStringWithMilli(long l){
        sdf.getTimeZone();
        // l = adjustTimeOffsetMinus(l);
        Date d = new Date(l);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int year =c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        month++;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int h=c.get(Calendar.HOUR_OF_DAY);
        int m = c.get(Calendar.MINUTE);
        int s = c.get(Calendar.SECOND);
        return year+"-"+month+"-"+day+" "+h+":"+m+":"+s;
    }

    public static String getTimeStringWithoutSecond(long l){
        String str = getTimeStringWithMilli(l);
        int p = str.lastIndexOf(":");
        return str.substring(0,p);
    }

    private static long adjustTimeOffsetAdd(long time){
//        TimeZone tz = sdf.getTimeZone();
//        int offset = tz.getOffset(time);
//        time=time+offset;
        return time;
    }


    public static String getHourAndMinute(long l){
        TimeZone tz = sdf.getTimeZone();

        Date d = new Date(l);
        Calendar c = Calendar.getInstance();
        c.setTime(d);

        String h=addZero(c.get(Calendar.HOUR_OF_DAY));
        String m =addZero(c.get(Calendar.MINUTE));

        return h+":"+m;
    }

    private static String addZero(int field){
        if(field<10){
            return "0"+field;
        }else{
            return ""+field;
        }
    }

    public static long getFirstDayOfThisMonth(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month++;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String timeStr = year+"-"+month+"-01"+" "+"00:00:00";
        long time=-1l;
        try {
            time = sdf.parse(timeStr).getTime();
            time = adjustTimeOffsetAdd(time);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return time;
    }

    public static int getDayNumWithinMonthTillNow(){
        Calendar  calendar  = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getDaysBetweenTime(long start,long end){
        return (int)((end-start)/(1000*3600*24));
    }

    public static long getLongWithDateTimeString(String str){
        long time=-1l;
        try {
            str= str+" 00:00:00";
            time = sdf.parse(str).getTime();
            time=adjustTimeOffsetAdd(time);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return time;
    }

    public static long getLongWithTimeString(String str){
        long time=-1l;
        try {
            str=str+":00";
            time = sdf.parse(str).getTime();
            time=adjustTimeOffsetAdd(time);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return time;
    }

    public static String getYearMonthAndDay(long l){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(l));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        month++;
        return addZero(year)+"/"+addZero(month)+"/"+addZero(day);
    }


    public static String getMonthAndDaySimplified(long l){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(l));
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        month++;
        return addZero(month)+"月"+addZero(day)+"日";
    }

    public static long getYearMonthDayLong(long l){
        return  l/(1000*3600*24);
    }

    public static long getFirstSecondOfMonth(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year  = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        month++;
        String time =  year+"-"+month+"-01 00:00:00";
        long longTime =-1L;
         try{
             longTime =  sdf.parse(time).getTime();
         }catch (ParseException e){
             e.printStackTrace();
         }
        return longTime;
    }
}
