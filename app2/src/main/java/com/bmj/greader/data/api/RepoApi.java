package com.bmj.greader.data.api;

import retrofit2.Response;
import rx.Observable;
import android.support.annotation.IntDef;

import java.util.ArrayList;

import com.bmj.greader.data.model.Repo;
import com.bmj.greader.data.model.RepoDetail;
import com.bmj.greader.data.model.User;
import com.bmj.greader.data.net.response.Content;
import com.bmj.greader.data.net.response.SearchRepoResp;
import com.bmj.greader.data.net.response.SearchUserResp;

/**
 * Created by Administrator on 2016/11/11 0011.
 */
public interface RepoApi {
    int TYPE_ANDROID = 1;
    int TYPE_IOS = 2;
    int TYPE_WEB = 3;
    int TYPE_PYTHON = 4;
    int TYPE_PHP = 5;
    @IntDef({TYPE_ANDROID, TYPE_IOS, TYPE_WEB, TYPE_PYTHON, TYPE_PHP})@interface MostStarsType{}

    int OWNER_REPOS = 1;
    int STARRED_REPOS = 2;
    int ORG_REPOS = 3;
    @IntDef({OWNER_REPOS,STARRED_REPOS,ORG_REPOS})@interface RepoType{}

    int FOLLOWING = 1;
    int FOLLOWER = 2;
    @IntDef({FOLLOWING,FOLLOWER}) @interface UserType{}

    /**
     * Get the top 30 stars repositories.
     * @param repoType
     * @return
     */
    Observable<Response<SearchRepoResp>> getTop30StarsRepo(@MostStarsType int repoType, int page);

    /**
     * search repos via key & language
     * @param q
     * @param page
     * @return
     */
    Observable<Response<SearchRepoResp>> searchRepo(String q, int page);

    Observable<Response<SearchUserResp>> searchUser(String q, int page);

    /**
     * Get repo info by the owner & repo name.
     * @param owner
     * @param repo
     * @return
     */
    rx.Observable<Repo> getRepo(String owner, String repo);

    Observable<Response<ArrayList<User>>> getRepoContributors(String owner,String repoName,int page);

    Observable<ArrayList<Repo>> getRepoForks(String owner,String repoName, String sort);

    /**
     * Get repo's details, including repo, contributors, readme, forks.
     * @param owner
     * @param name
     * @return
     */
    rx.Observable<RepoDetail> getRepoDetail(String owner, String name);

/*
    Observable<UserDetail> getSelfUserDetail();

    Observable<UserDetail> getOtherUserDetail(String userlogin);*/

    /**
     * Get current user's repositories.
     * @return
     */
    rx.Observable<Response<ArrayList<Repo>>> getMyRepos(int page);

    /**
     * Get user's repositories.
     * @return
     */
    rx.Observable<Response<ArrayList<Repo>>> getUserRepos(String username,int page);

    /**
     * Get current user's starred repositories.
     * @return
     */
    rx.Observable<Response<ArrayList<Repo>>> getMyStarredRepos(int page);

    /**
     * Get user's starred repositories.
     * @return
     */
    rx.Observable<Response<ArrayList<Repo>>> getUserStarredRepos(String username,int page);

    /**
     * Star a repository
     */
    rx.Observable<Boolean> starRepo(String owner, String repo);

    /**
     * Star a repository
     */
    rx.Observable<Boolean> unstarRepo(String owner, String repo);

    /**
     * Check if the repository is starred or not.
     * @param owner
     * @param repo
     * @return
     */
    rx.Observable<Boolean> isStarred(String owner, String repo);

    /**
     * Get repo's readme content.
     * @param owner
     * @param repo
     * @return
     */
    rx.Observable<Content> getRepoReadme(String owner, String repo);

    /**
     * Get repo's content by path. set path to null to get first level contents.
     * @param owner
     * @param repo
     * @return
     */
    rx.Observable<ArrayList<Content>> getRepoContents(String owner, String repo, String path);

    /**
     * Get file content's details, which contain a "content".
     * @param owner
     * @param repo
     * @param path
     * @return
     */
    rx.Observable<Content> getContentDetail(String owner, String repo, String path);

    /**
     * Get a single user by name.
     * @param name
     * @return
     */
    rx.Observable<User> getSingleUser(String name);

    /**
     * get user's following user list.
     * @param user
     * @return
     */
    rx.Observable<Response<ArrayList<User>>> getUserFollowing(String user,int page);

    /**
     * get my following list.
     * @return
     */
    rx.Observable<Response<ArrayList<User>>> getMyFollowing(int page);

    /**
     * get user's followers.
     * @param user
     * @return
     */
    rx.Observable<Response<ArrayList<User>>> getUserFollowers(String user,int page);

    /**
     * get my followers.
     * @return
     */
    rx.Observable<Response<ArrayList<User>>> getMyFollowers(int page);

    Observable<Boolean> checkIfFollowUser(String userLogin);

    Observable<Boolean> followUser(String userLogin);

    Observable<Boolean> unfollowUser(String userLogin);
}
