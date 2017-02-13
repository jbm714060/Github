package com.bmj.greader.ui.module.repo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.bmj.greader.ui.module.repo.view.RepoTreeView;
import com.pddstudio.highlightjs.HighlightJsView;
import com.pddstudio.highlightjs.models.Language;
import com.pddstudio.highlightjs.models.Theme;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.bmj.greader.MyApplication;
import com.bmj.greader.R;
import com.bmj.greader.common.util.StringUtil;
import com.bmj.greader.common.wrapper.AppLog;
import com.bmj.greader.dagger2.HasComponent;
import com.bmj.greader.dagger2.component.DaggerRepoComponent;
import com.bmj.greader.dagger2.component.RepoComponent;
import com.bmj.greader.dagger2.module.ActivityModule;
import com.bmj.greader.dagger2.module.RepoModule;
import com.bmj.greader.data.net.response.Content;
import com.bmj.greader.presenter.repo.CodePresenter;
import com.bmj.greader.ui.base.BaseLoadingActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/20 0020.
 */
public class CodeActivity extends BaseLoadingActivity implements RepoTreeView,HasComponent<RepoComponent>{
    private static final String EXTRA_OWNER = "extra_owner";
    private static final String EXTRA_REPO_NAME = "extra_repo_name";
    private static final String EXTRA_CODE_PATH = "extra_code";

    @BindView(R.id.code_view)
    HighlightJsView mCodeView;

    @Inject
    CodePresenter mPresenter;

    public static void launch(Context context,String owner, String reponame, String path){
        Intent intent = new Intent(context,CodeActivity.class);
        intent.putExtra(EXTRA_REPO_NAME,reponame);
        intent.putExtra(EXTRA_CODE_PATH,path);
        intent.putExtra(EXTRA_OWNER,owner);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        ButterKnife.bind(this);
        getComponent().inject(this);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPresenter.attachView(this);

        initView();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public String getLoadingMessage() {
        return getString(R.string.loading);
    }

    private void initView(){
        mCodeView.setTheme(Theme.ANDROID_STUDIO);
        mCodeView.setHighlightLanguage(Language.AUTO_DETECT);
    }

    private void loadData(){
        String owner = getIntent().getStringExtra(EXTRA_OWNER);
        String reponame = getIntent().getStringExtra(EXTRA_REPO_NAME);
        String path = getIntent().getStringExtra(EXTRA_CODE_PATH);
        mPresenter.getContentDetail(owner,reponame,path);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public RepoComponent getComponent() {
        return DaggerRepoComponent.builder()
                .applicationComponent(MyApplication.get(this).getComponent())
                .repoModule(new RepoModule())
                .activityModule(new ActivityModule(this))
                .build();
    }

    @Override
    public void showContent(ArrayList<Content> data) {

    }

    @Override
    public void showCode(Content data) {
        mCodeView.setSource(StringUtil.base64Decode(data.content));
    }

    @Override
    public void showError(Throwable e) {
        AppLog.e(e);
    }

    @Override
    public void showEmpty() {

    }
}
