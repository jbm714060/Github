package com.bmj.greader.data.net.services;


import java.util.ArrayList;

import com.bmj.greader.data.model.Repo;
import com.bmj.greader.data.model.User;
import com.bmj.greader.data.net.response.Content;
import com.bmj.greader.data.net.response.SearchRepoResp;
import com.bmj.greader.data.net.response.SearchUserResp;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/11/12 0012.
 */
public interface RepoService {
    @Headers({"Cache-Control:public,max-age=600"})
    @GET("search/repositories")
    Observable<Response<SearchRepoResp>> searchRepo(@Query("q") String q, @Query("sort") String sort,
                                                    @Query("order")String order, @Query("page")int page);

    @Headers({"Cache-Control:public,max-age=600"})
    @GET("search/users")
    Observable<Response<SearchUserResp>> searchUser(@Query("q") String q,  @Query("page")int page);

    @Headers("Cache-Control:public,max-age=3600")
    @GET("repos/{owner}/{name}")
    Observable<Repo> get(@Path("owner")String owner,@Path("name")String name);

    @Headers("Cache-Control:public,max-age=3600")
    @GET("repos/{owner}/{name}/contributors")
    Observable<Response<ArrayList<User>>> contributors(@Path("owner")String owner,@Path("name")String repoName,@Query("page")int page);

    @Headers("Cache-Control:public,max-age=3600")
    @GET("repos/{owner}/{name}/readme")
    Observable<Content> readme(@Path("owner")String owner,@Path("name")String repoName);

    @Headers("Cache-Control:public,max-age=3600")
    @GET("repos/{owner}/{name}/forks")
    Observable<ArrayList<Repo>> listForks(@Path("owner")String owner,@Path("name")String repoName,
                                          @Query("sort")String sort);

    @Headers("Cache-Control: public, max-age=600")
    @GET("user/repos")
    Observable<Response<ArrayList<Repo>>> getMyRepos(@Query("sort") String sort, @Query("type") String type,@Query("page")int page);

    @Headers("Cache-Control: public, max-age=600")
    @GET("users/{name}/repos")
    Observable<Response<ArrayList<Repo>>> getUserRepos(@Path("name") String user, @Query("sort") String sort,@Query("page")int page);

    @Headers("Cache-Control: public, max-age=600")
    @GET("user/starred")
    Observable<Response<ArrayList<Repo>>> getMyStarredRepos(@Query("sort") String sort,@Query("page")int page);

    @Headers("Cache-Control: public, max-age=600")
    @GET("users/{name}/starred")
    Observable<Response<ArrayList<Repo>>> getUserStarredRepos(@Path("name") String user, @Query("sort") String sort,@Query("page")int page);

    @Headers("Content-Length: 0")
    @PUT("/user/starred/{owner}/{repo}")
    Observable<Response<ResponseBody>> starRepo(@Path("owner") String owner, @Path("repo") String repo);

    @GET("/user/starred/{owner}/{repo}")
    Observable<Response<ResponseBody>> checkIfRepoIsStarred(@Path("owner") String owner, @Path("repo") String repo);

    @DELETE("/user/starred/{owner}/{repo}")
    Observable<Response<ResponseBody>> unstarRepo(@Path("owner")String owner,@Path("repo")String repo);


    @Headers("Cache-Control: public, max-age=3600")
    @GET("/repos/{owner}/{repo}/contents")
    Observable<ArrayList<Content>> contents(@Path("owner") String owner, @Path("repo") String repo);

    @Headers("Cache-Control: public, max-age=3600")
    @GET("/repos/{owner}/{repo}/contents")
    Observable<ArrayList<Content>> contentsByRef(@Path("owner") String owner, @Path("repo") String repo,
                                                 @Query("ref") String ref);

    @Headers("Cache-Control: public, max-age=3600")
    @GET("/repos/{owner}/{repo}/contents/{path}")
    Observable<ArrayList<Content>> contentsWithPath(@Path("owner") String owner, @Path("repo") String repo,
                                                    @Path("path") String path);

    @Headers("Cache-Control: public, max-age=3600")
    @GET("/repos/{owner}/{repo}/contents/{path}")
    Observable<ArrayList<Content>> contentsWithPathByRef(@Path("owner") String owner, @Path("repo") String repo,
                                                         @Path("path") String path, @Query("ref") String ref);

    @Headers("Cache-Control: public, max-age=3600")
    @GET("/repos/{owner}/{repo}/contents/{path}")
    Observable<Content> contentDetail(@Path("owner") String owner, @Path("repo") String repo,
                                      @Path("path") String path);

    @Headers("Cache-Control: public, max-age=3600")
    @GET("/repos/{owner}/{repo}/contents/{path}")
    Observable<Content> contentDetailByRef(@Path("owner") String owner, @Path("repo") String repo,
                                           @Path("path") String path, @Query("ref") String ref);

    @Headers("Cache-Control: public, max-age=3600")
    @GET("/users/{user}")
    Observable<User> getSingleUser(@Path("user") String user);

    @Headers("Cache-Control: public, max-age=3600")
    @GET("/users/{user}/following")
    Observable<Response<ArrayList<User>>> getUserFollowing(@Path("user") String user,@Query("page")int page);

    @Headers("Cache-Control: public, max-age=60")
    @GET("/user/following")
    Observable<Response<ArrayList<User>>> getMyFollowing(@Query("page")int page);

    @Headers("Cache-Control: public, max-age=3600")
    @GET("/users/{user}/followers")
    Observable<Response<ArrayList<User>>> getUserFollowers(@Path("user") String user,@Query("page")int page);

    @Headers("Cache-Control: public, max-age=60")
    @GET("/user/followers")
    Observable<Response<ArrayList<User>>> getMyFollowers(@Query("page")int page);

    @GET("/user/following/{userlogin}")
    Observable<Response<ResponseBody>> checkIsFollowingUser(@Path("userlogin")String userLogin);

    @Headers("Content-Length:0")
    @PUT("/user/following/{userlogin}")
    Observable<Response<ResponseBody>> followUser(@Path("userlogin")String userLogin);

    @DELETE("/user/following/{userlogin}")
    Observable<Response<ResponseBody>> unfollowUser(@Path("userlogin")String userLogin);
}
