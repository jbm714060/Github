package com.bmj.greader.presenter.repo;

import com.bmj.greader.data.model.issue.Issue;
import com.bmj.greader.data.net.pagination.PaginationLinks;
import com.bmj.greader.data.net.services.IssueService;
import com.bmj.greader.data.rx.ResponseObserver;
import com.bmj.greader.presenter.base.RxMvpPresenter;
import com.bmj.greader.ui.module.repo.view.IssueView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/12/22 0022.
 */
public class RepoIssuePresenter extends RxMvpPresenter<IssueView>{
    private IssueService mIssueService;
    public PaginationLinks mLinks;
    private String mUserLogin;
    private String mRepoName;
    private Map<String,String> mFilter;

    @Inject
    public RepoIssuePresenter(IssueService issueService){
        mIssueService = issueService;
    }

    public void loadIssues(String userlogin,String reponame,String state){
        mUserLogin = userlogin;
        mRepoName = reponame;
        mFilter = new HashMap<>();
        mFilter.put("state",state);
        mLinks = null;
        loadMoreIssues();
    }

    public void loadMoreIssues(){
        final int page = getPage();
        if(page < 1){
            getMvpView().showError(new Throwable("no more page"));
            return;
        }

        mCompositeSubscription.add(
                mIssueService.issues(mUserLogin,mRepoName,mFilter,getPage())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Func1<Response<ArrayList<Issue>>, ArrayList<Issue>>() {
                            @Override
                            public ArrayList<Issue> call(Response<ArrayList<Issue>> arrayListResponse) {
                                mLinks = new PaginationLinks(arrayListResponse.headers().get("Link"));
                                return arrayListResponse.body();
                            }
                        })
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                if(page==1)
                                    getMvpView().showLoading();
                            }
                        })
                        .doOnTerminate(new Action0() {
                            @Override
                            public void call() {
                                if(page==1)
                                    getMvpView().dismissLoading();
                            }
                        })
                        .subscribe(new ResponseObserver<ArrayList<Issue>>() {
                            @Override
                            public void onSuccess(ArrayList<Issue> issues) {
                                if(page == 1)
                                    getMvpView().showContent(issues);
                                else
                                    getMvpView().showMore(issues);
                            }

                            @Override
                            public void onError(Throwable e) {
                                getMvpView().showError(e);
                            }
                        })
        );
    }

    public int getPage(){
        if(mLinks == null)
            return 1;
        else if (mLinks.getNextPage() !=0 && mLinks.getNextPage() <= mLinks.getLastPage()) {
            return mLinks.getNextPage();
        }
        else
            return -1;
    }

    public int getPageSize(){
        return mLinks.getPageSize();
    }
}
