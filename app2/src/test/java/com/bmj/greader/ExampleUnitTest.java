package com.bmj.greader;

import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.utils.ConstUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.blankj.utilcode.utils.TimeUtils;
import com.bmj.greader.common.util.StringUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
        String time = "2015-12-05T00:59:15Z";

        Pattern p = Pattern.compile("[A-Z]");
        Matcher m = p.matcher(time);
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
        if(year > 0)
            System.out.print(dataValues[2]+" "+getMonth(Integer.valueOf(dataValues[1]))+" "+dataValues[0]);
        else if(day > 30)
            System.out.print(dataValues[2]+" "+getMonth(Integer.valueOf(dataValues[1])));
        else if(day > 0)
            System.out.print(day+" days ago");
        else if(hour > 0) {
            if(hour==1)
                System.out.print("an hour ago");
            else
                System.out.print(hour + " hours ago");
        }
        else if(min > 0) {
            if(min == 1)
                System.out.print("a minute ago");
            else
                System.out.print(min + " minutes ago");
        }
        else if(second > 10)
            System.out.print(second+" seconds ago");
        else
            System.out.print("just now");
    }

    private String getMonth(int month){
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

    @Test
    public void test(){
        try {
            Document doc = Jsoup.connect("https://github.com/trending/java").get();
            Elements eles = doc.select("li[class]");
            System.out.print("113 "+eles.size()+"\n");
            for(Element element:eles){
                System.out.print(element.select("a[href]").text());

                System.out.print("\n"+element.select("p").text());

                Elements lang = element.select("[itemprop=programmingLanguage]");
                String sLang = lang==null?"unknown":lang.text();
                System.out.print("\n"+sLang);

                System.out.print("\n"+element.select("[aria-label=Stargazers]").text());
                System.out.print("\n"+element.select("[aria-label=Forks]").text());
                System.out.print("\n"+element.getElementsByTag("span").last().text()+"\n");
            }
        }catch (IOException e){
            System.out.print(e.toString()+"\n\n");
        }
    }
}