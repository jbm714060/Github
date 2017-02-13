package com.bmj.greader.data.net.services;

import com.bmj.greader.data.net.response.SearchRepoResp;
import com.bmj.greader.data.net.response.TrendingResultResp;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/11/12 0012.
 */
public interface TrendingService {
    @Headers("Cache-Control:public,max-age=600")
   // @GET("trending?languages[]=java&languages[]=swift&languages[]=objective-c&languages[]=bash&languages[]=python&languages[]=html")
    @GET("trending/java?since=daily")
    Observable<TrendingResultResp> getTrendingRepos();

    @Headers({"Cache-Control:public,max-age=600","Des-bmj:searchRepo"})
    @GET("search/repositories")
    Observable<Response<SearchRepoResp>> searchRepo(@Query("q") String key, @Query("sort") String sort,
                                                    @Query("order")String order, @Query("page")int page,
                                                    @Query("per_page")int pageSize);
}
