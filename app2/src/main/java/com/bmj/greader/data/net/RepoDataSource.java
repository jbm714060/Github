package com.bmj.greader.data.net;

import android.text.TextUtils;

import com.bmj.greader.common.util.StringUtil;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;

import java.util.ArrayList;

import javax.inject.Inject;

import com.bmj.greader.common.config.AppConfig;
import com.bmj.greader.common.wrapper.AppLog;
import com.bmj.greader.data.api.RepoApi;
import com.bmj.greader.data.model.Repo;
import com.bmj.greader.data.model.RepoDetail;
import com.bmj.greader.data.model.User;
import com.bmj.greader.data.net.response.Content;
import com.bmj.greader.data.net.response.SearchRepoResp;
import com.bmj.greader.data.net.response.SearchUserResp;
import com.bmj.greader.data.net.services.RepoService;

import rx.functions.Func1;
import rx.functions.Func5;

/**
 * Created by Administrator on 2016/11/12 0012.
 */
public class RepoDataSource implements RepoApi{
    private static final String SORT_BY_STARS = "stars";
    private static final String ORDER_BY_DESC = "desc";

    RepoService mRepoService;

    @Inject
    public RepoDataSource(RepoService rs){
        mRepoService = rs;
    }

    @Override
    public Observable<Response<SearchRepoResp>> getTop30StarsRepo(@MostStarsType int repoType, int page) {
        StringBuilder queryBuilder = new StringBuilder();
        String key = "";
        String [] lang = null;
        switch(repoType){
            case TYPE_ANDROID:
                key = "android";
                lang = new String[]{"java"};
                break;
            case TYPE_IOS:
                key = "ios";
                lang = new String[]{"Swift","Objective-C"};
                break;
            case TYPE_PYTHON:
                key = "python";
                lang = new String[]{"python"};
                break;
            case TYPE_WEB:
                key = "web";
                lang = new String[]{"HTML", "CSS", "JavaScript"};
                break;
            case TYPE_PHP:
                key = "php";
                lang = new String[]{"PHP"};
                break;
        }
        queryBuilder.append(key);
        if(lang != null && lang.length>0){
            for(String language:lang){
                queryBuilder.append("+language:");
                queryBuilder.append(language);
            }
        }

        AppLog.d("getTop30StarsRepo, q:" + queryBuilder.toString());

        return mRepoService.searchRepo(queryBuilder.toString(),SORT_BY_STARS,ORDER_BY_DESC,
                page);
    }

    @Override
    public Observable<Response<SearchUserResp>> searchUser(String q, int page){
        return mRepoService.searchUser(q,page);
    }

    @Override
    public Observable<Response<SearchRepoResp>> searchRepo(String q, int page) {
        return mRepoService.searchRepo(q,SORT_BY_STARS,ORDER_BY_DESC,page);
    }

    @Override
    public rx.Observable<Repo> getRepo(String owner, String repo) {

        return mRepoService.get(owner,repo);
    }

    @Override
    public rx.Observable<RepoDetail> getRepoDetail(String owner, String name) {
        return Observable.zip(mRepoService.get(owner, name),
                mRepoService.contributors(owner, name,1).map(
                        new Func1<Response<ArrayList<User>>, ArrayList<User>>() {
                            @Override
                            public ArrayList<User> call(Response<ArrayList<User>> arrayListResponse) {
                                return arrayListResponse.body();
                            }
                        }),
                mRepoService.listForks(owner, name, "newest"),
                mRepoService.readme(owner, name),
                isStarred(owner, name),
                new Func5<Repo, ArrayList<User>, ArrayList<Repo>, Content, Boolean, RepoDetail>() {
                    @Override
                    public RepoDetail call(Repo repo, ArrayList<User> users, ArrayList<Repo> repos, Content content, Boolean aBoolean) {
                        RepoDetail detail = new RepoDetail();
                        repo.setStarred(aBoolean);
                        detail.setBaseRepo(repo);
                        detail.setContributors(users);
                        detail.setForks(repos);
                        content.content = StringUtil.base64Decode(content.content);
                        detail.setReadMe(content);
                        return detail;
                    }
                });
    }

    @Override
    public rx.Observable<Response<ArrayList<Repo>>> getMyRepos(int page) {
        return mRepoService.getMyRepos("updated","updated",page);
    }

    @Override
    public rx.Observable<Response<ArrayList<Repo>>> getUserRepos(String username,int page) {
        return mRepoService.getUserRepos(username,"updated",page);
    }

    @Override
    public Observable<Response<ArrayList<Repo>>> getMyStarredRepos(int page) {
        return mRepoService.getMyStarredRepos("updated",page);
    }

    @Override
    public rx.Observable<Response<ArrayList<Repo>>> getUserStarredRepos(String username,int page) {
        return mRepoService.getUserStarredRepos(username,"updated",page);
    }

    @Override
    public rx.Observable<Boolean> starRepo(String owner, String repo) {
        return mRepoService.starRepo(owner,repo).map(new Func1<Response<ResponseBody>, Boolean>() {
            @Override
            public Boolean call(Response<ResponseBody> responseBodyResponse) {
                AppLog.d("response:" + responseBodyResponse);
                return responseBodyResponse!=null && responseBodyResponse.code() == 204;
            }
        });
    }

    @Override
    public rx.Observable<Boolean> unstarRepo(String owner, String repo) {
        return mRepoService.unstarRepo(owner,repo).map(new Func1<Response, Boolean>() {
            @Override
            public Boolean call(Response response) {
                AppLog.d("response:" + response);
                return response != null && response.code() == 204;
            }
        });
    }

    @Override
    public rx.Observable<Boolean> isStarred(String owner, String repo) {
        return mRepoService.checkIfRepoIsStarred(owner,repo)
                .map(new Func1<Response<ResponseBody>, Boolean>() {
                    @Override
                    public Boolean call(Response<ResponseBody> response) {
                        /**
                         * Response if this repository is starred by you
                         *  Status: 204 No Content
                         * Response if this repository is not starred by you
                         *  Status: 404 Not Found
                         */
                        return response != null && response.code() == 204;
                }
        });
    }

    @Override
    public rx.Observable<Content> getRepoReadme(String owner, String repo) {
        return mRepoService.readme(owner,repo);
    }

    @Override
    public Observable<Response<ArrayList<User>>> getRepoContributors(String owner,String repoName,int page){
        return mRepoService.contributors(owner,repoName,page);
    }

    @Override
    public Observable<ArrayList<Repo>> getRepoForks(String owner,String repoName, String sort){
        return mRepoService.listForks(owner,repoName,sort);
    }

    @Override
    public rx.Observable<ArrayList<Content>> getRepoContents(String owner, String repo, String path) {
        if(TextUtils.isEmpty((path)))
            return mRepoService.contents(owner,repo);
        else
            return mRepoService.contentsWithPath(owner,repo,path);
    }

    @Override
    public rx.Observable<Content> getContentDetail(String owner, String repo, String path) {
        return mRepoService.contentDetail(owner,repo,path);
    }

    @Override
    public rx.Observable<User> getSingleUser(String name) {
        return mRepoService.getSingleUser(name);
    }

    @Override
    public rx.Observable<Response<ArrayList<User>>> getUserFollowing(String user,int page) {
        return mRepoService.getUserFollowing(user,page);
    }

    @Override
    public rx.Observable<Response<ArrayList<User>>> getMyFollowing(int page) {
        return mRepoService.getMyFollowing(page);
    }

    @Override
    public rx.Observable<Response<ArrayList<User>>> getUserFollowers(String user,int page) {
        return mRepoService.getUserFollowers(user,page);
    }

    @Override
    public rx.Observable<Response<ArrayList<User>>> getMyFollowers(int page) {
        return mRepoService.getMyFollowers(page);
    }

    @Override
    public Observable<Boolean> checkIfFollowUser(String userLogin){
        return mRepoService.checkIsFollowingUser(userLogin)
                .map(new Func1<Response<ResponseBody>, Boolean>() {
                    @Override
                    public Boolean call(Response<ResponseBody> response) {
                        return (response != null) && (response.code() == 204);
                    }
                });
    }

    @Override
    public Observable<Boolean> followUser(String userLogin){
        return mRepoService.followUser(userLogin)
                .map(new Func1<Response<ResponseBody>, Boolean>() {
                    @Override
                    public Boolean call(Response<ResponseBody> response) {
                        return response!=null && response.code() == 204;
                    }
                });
    }

    @Override
    public Observable<Boolean> unfollowUser(String userLogin){
        return mRepoService.unfollowUser(userLogin)
                .map(new Func1<Response<ResponseBody>, Boolean>() {
                    @Override
                    public Boolean call(Response<ResponseBody> response) {
                        return response!=null && response.code()==204;
                    }
                });
    }
}
