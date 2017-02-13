package com.bmj.greader.data.net.services;

import com.bmj.greader.data.model.GithubComment;
import com.bmj.greader.data.model.gist.CommentRequest;
import com.bmj.greader.data.model.issue.Issue;
import com.bmj.greader.data.model.issue.IssueRequest;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Administrator on 2016/12/22 0022.
 */
public interface IssueService {

    /**
     * state : Can be either open, closed, or all. Default: open
     * @param userlogin
     * @param reponame
     * @param filter
     * @param page
     * @return
     */
    @Headers("Cache-header:public,max-age:3600")
    @GET("/repos/{userlogin}/{reponame}/issues?sort=updated")
    Observable<Response<ArrayList<Issue>>> issues(@Path("userlogin")String userlogin, @Path("reponame")String reponame,
                                                 @QueryMap Map<String,String> filter, @Query("page")int page);

    @Headers("Cache-header:public,max-age:3600")
    @GET("/repos/{owner}/{name}/issues/{num}/comments")
    Observable<Response<ArrayList<GithubComment>>> comments(@Path("owner") String owner, @Path("name") String repo,
                                 @Path("num") int num, @Query("page") int page);

    @POST("/repos/{owner}/{name}/issues")
    Observable<Issue> create(@Path("owner") String owner, @Path("name") String repo,
                             @Body IssueRequest issue);

    @POST("/repos/{owner}/{name}/issues/{num}/comments")
    Observable<Response<GithubComment>> addComment(@Path("owner") String owner, @Path("name") String repo,
                                         @Path("num") int num, @Body CommentRequest comment);

    @DELETE("/repos/{owner}/{name}/issues/comments/{id}")
    Observable<Response<ResponseBody>> deleteComment(@Path("owner") String owner, @Path("name") String name,
                                                     @Path("id") int id);
}
