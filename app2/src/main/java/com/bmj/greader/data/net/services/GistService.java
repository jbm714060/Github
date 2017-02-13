package com.bmj.greader.data.net.services;

import com.bmj.greader.data.model.gist.CommentRequest;
import com.bmj.greader.data.model.gist.Gist;
import com.bmj.greader.data.model.GithubComment;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/12/19 0019.
 */
public interface GistService {
    @Headers("Cache-Control:public,max-age=3600")
    @GET("/users/{userlogin}/gists")
    Observable<Response<ArrayList<Gist>>> getUserGists(@Path("userlogin")String userLogin, @Query("page")int page);

    @Headers("Cache-Control:public,max-age=300")
    @GET("/gists")
    Observable<Response<ArrayList<Gist>>> getOwnGists(@Query("page")int page);

    @Headers("Cache-Control:public,max-age=300")
    @GET("/gists/starred")
    Observable<Response<ArrayList<Gist>>> getOwnStarredGists(@Query("page")int page);

    @Headers("Cache-Control:public,max-age=600")
    @GET("/gists/{id}/comments")
    Observable<Response<ArrayList<GithubComment>>> getGistComments(@Path("id")String gistId, @Query("page")int page);

    @Headers("Content-Length:0")
    @PUT("/gists/{id}/star")
    Observable<Response<ResponseBody>> starGist(@Path("id")String gistId);

    @DELETE("/gists/{id}/star")
    Observable<Response<ResponseBody>> unstarGist(@Path("id")String gistId);

    /**
     * Status: 204 No Content
     * Status: 404 Not Found
     */
    @GET("/gists/{id}/star")
    Observable<Response<ResponseBody>> checkIfStarred(@Path("id")String gistId);

    @POST("/gists/{gistid}/comments")
    Observable<Response<GithubComment>> createComment(@Path("gistid")String gistId, @Body CommentRequest body);

    /**
     * Status: 204 No Content
     * @param gistId
     * @param commentId
     * @return
     */
    @DELETE("/gists/{gistid}/comments/{commentid}")
    Observable<Response<ResponseBody>> deleteComment(@Path("gistid")String gistId,@Path("commentid")int commentId);
}
