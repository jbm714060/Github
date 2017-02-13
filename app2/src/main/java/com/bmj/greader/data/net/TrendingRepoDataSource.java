package com.bmj.greader.data.net;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import com.bmj.greader.common.util.StringUtil;
import com.bmj.greader.common.wrapper.AppLog;
import com.bmj.greader.data.api.TrendingApi;
import com.bmj.greader.data.model.TrendingRepo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/11/12 0012.
 */
public class TrendingRepoDataSource implements TrendingApi{
    private static final String END_POINT= "https://github.com/trending/";
    private long lastUpdateTime = 0;
    private String mLastUrl;

    @Inject
    public TrendingRepoDataSource(){
    }

    @Override
    public Observable<ArrayList<TrendingRepo>> getTrendingRepo(final String language,final String time) {
        return Observable.create(new Observable.OnSubscribe<ArrayList<TrendingRepo>>() {
            @Override
            public void call(Subscriber<? super ArrayList<TrendingRepo>> subscriber) {
                if(!subscriber.isUnsubscribed()){
                    subscriber.onStart();

                    String url = getUrl(language,time);
                    Log.i("showgist",url);

                    if((System.currentTimeMillis() - lastUpdateTime) < 3000
                            && url.equals(mLastUrl)) {
                        subscriber.onError(new Throwable("please refresh after 3 seconds"));
                    }

                    mLastUrl = url;

                    try {
                        ArrayList<TrendingRepo> trendingList = new ArrayList<>();
                        Document doc = Jsoup.connect(url).get();
                        Elements eles = doc.select("li[class]");
                        AppLog.d("scrape repo size"+eles.size()+"\n");
                        for(Element element:eles){
                            TrendingRepo repo = new TrendingRepo();

                            String fullname = element.select("a[href]").attr("href");
                            repo.setOwnUser(StringUtil.trimNewLine(fullname.split("/")[1]));
                            repo.setRepoName(StringUtil.trimNewLine(fullname.split("/")[2]));
                            repo.setDescription(element.select("p").text().trim());

                            Elements lang = element.select("[itemprop=programmingLanguage]");
                            String sLang = lang==null?"unknown":lang.text();

                            repo.setLanguage(sLang.trim());
                            repo.setRepoStarCount(element.select("[aria-label=Stargazers]").text().trim());
                            repo.setRepoForksCount(element.select("[aria-label=Forks]").text().trim());
                            repo.setNewStars(element.getElementsByTag("span").last().text().trim());
                            trendingList.add(repo);
                        }
                        subscriber.onNext(trendingList);
                        subscriber.onCompleted();
                        lastUpdateTime = System.currentTimeMillis();
                    }catch (IOException e){
                        subscriber.onError(e);
                    }
                }
            }
        });
    }

    private String getUrl(String language,String time){
        StringBuilder url = new StringBuilder(END_POINT);
        if(!language.equals("ALL")){
            language  = language.toLowerCase();
            url.append(language);
        }

        if(time.equals("today"))
            url.append("?since=daily");
        else if(time.equals("this week"))
            url.append("?since=weekly");
        else if(time.equals("this month"))
            url.append("?since=monthly");
        else
            url.append("?since=daily");

        return url.toString();
    }
}
