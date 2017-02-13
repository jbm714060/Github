package com.bmj.greader.data;

import com.blankj.utilcode.utils.ConstUtils;
import com.blankj.utilcode.utils.TimeUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/12/5 0005.
 */
public class RepoUpdatedTime {

    /**
     *
     * @param date 日期样式："2015-12-05T00:59:15Z"
     * @return
     */
    public static String getUpdatedTimeString(String date){
        if(date == null)
            return "Updated on unknown date";

        Pattern p = Pattern.compile("[A-Z]");
        Matcher m = p.matcher(date);
        String data = m.replaceAll(" ").trim();
        Pattern pp = Pattern.compile("-|:| ");
        String[] timeValue = pp.split(data);
        Integer[] dataValues = new Integer[timeValue.length];
        for(int i = 0;i<timeValue.length;i++){
            dataValues[i] = Integer.valueOf(timeValue[i]);
        }

        long ss = TimeUtils.getTimeSpanByNow(data, ConstUtils.TimeUnit.SEC,"yyyy-MM-dd HH:mm:ss");
        long second = ss%60;
        long min = ss/60%60;
        long hour = ss/3600%24;
        long day = ss/(3600*24)%365;
        long year = ss/(3600*24*365);

        StringBuilder builder = new StringBuilder("Updated ");
        if(year > 0)
            builder.append("on ").append(dataValues[2]).append(" ").append(
                    getMonthString(Integer.valueOf(dataValues[1]))).append(" ")
                    .append(dataValues[0]);
        else if(day > 30)
            builder.append("on ").append(dataValues[2]).append(" ").
                    append(getMonthString(Integer.valueOf(dataValues[1])));
        else if(day > 0)
            builder.append(day+" days ago");
        else if(hour > 0) {
            if(hour==1)
                builder.append("an hour ago");
            else
                builder.append(hour + " hours ago");
        }
        else if(min > 0) {
            if(min == 1)
                builder.append("a minute ago");
            else
                builder.append(min + " minutes ago");
        }
        else if(second > 10)
            builder.append(second+" seconds ago");
        else
            builder.append("just now");

        return builder.toString();
    }

    public static String getMonthString(int month){
        switch (month){
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
            default:
                return "Error";
        }
    }
}
