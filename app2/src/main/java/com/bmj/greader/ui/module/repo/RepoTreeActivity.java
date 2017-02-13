package com.bmj.greader.ui.module.repo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.breadcrumb.LinearBreadcrumb;
import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.bmj.greader.common.util.FileUtil;
import com.bmj.greader.common.util.InputMethodUtil;
import com.bmj.greader.common.util.StringUtil;
import com.bmj.greader.common.wrapper.CustomSnackbar;
import com.bmj.greader.common.wrapper.ImageLoader;
import com.bmj.greader.data.pref.AccountPref;
import com.bmj.greader.data.pref.AppPref;
import com.bmj.greader.presenter.repo.CodePresenter;
import com.bmj.greader.ui.base.BaseActivity;
import com.bmj.greader.ui.module.repo.view.RepoTreeView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.clans.fab.FloatingActionButton;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.pddstudio.highlightjs.HighlightJsView;
import com.pddstudio.highlightjs.models.Language;
import com.pddstudio.highlightjs.models.Theme;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import com.bmj.greader.MyApplication;
import com.bmj.greader.R;
import com.bmj.greader.common.wrapper.AppLog;
import com.bmj.greader.dagger2.HasComponent;
import com.bmj.greader.dagger2.component.DaggerRepoComponent;
import com.bmj.greader.dagger2.component.RepoComponent;
import com.bmj.greader.dagger2.module.ActivityModule;
import com.bmj.greader.dagger2.module.RepoModule;
import com.bmj.greader.data.net.response.Content;
import com.bmj.greader.presenter.repo.RepoTreePresenter;
import com.bmj.greader.ui.module.repo.adapter.RepoContentAdapter;

/**
 * Created by Administrator on 2016/11/20 0020.
 */
public class RepoTreeActivity extends BaseActivity implements RepoTreeView,HasComponent<RepoComponent>{
    @BindView(R.id.adr_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.adr_code_view)
    HighlightJsView mCodeView;
    @BindView(R.id.adr_path_view)
    LinearBreadcrumb mPathView;
    @BindView(R.id.adr_repotree_view)
    RecyclerView mRepoTree;
    @BindView(R.id.dh_reponame)
    TextView mRepoTreeName;
    @BindView(R.id.adr_code_progress)
    CircularProgressBar mCodeProgress;
    @BindView(R.id.adr_tree_progress)
    SmoothProgressBar mSmoothProgress;

    @BindView(R.id.adr_media_view)
    ImageView mMediaView;

    @BindView(R.id.adr_content_layout)
    View mContentLayout;
    @BindView(R.id.adr_slide_layout)
    View mSlideLayout;
    @BindView(R.id.adr_save)
    FloatingActionButton mSaveButton;

    @BindView(R.id.adr_saveoptions_layout)
    View mSaveOptionsLayout;
    @BindView(R.id.adr_edit_dir)
    EditText mDirEdit;
    @BindView(R.id.adr_edit_filename)
    EditText mFileNameEdit;

    @Inject
    RepoTreePresenter mPresenter;
    @Inject
    CodePresenter mCodePresenter;

    private String mOwner;
    private String mRepoName;
    private String mOpenedFileName;
    private RepoContentAdapter mAdapter;
    private TranslateAnimation mSaveOptionsInAnim;
    private TranslateAnimation mSaveOptionsOutAnim;
    private String mCodeString;

    private static final String EXTRA_OWNER = "extra_owner";
    private static final String EXTRA_REPO_NAME = "extra_repo_name";
    public static void launch(Context context,String ownername,String reponame){
        Intent intent = new Intent(context,RepoTreeActivity.class);
        intent.putExtra(EXTRA_OWNER,ownername);
        intent.putExtra(EXTRA_REPO_NAME,reponame);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        setContentView(R.layout.activity_drawer_repotree);
        ButterKnife.bind(this);

        initViews();

        mPresenter.attachView(this);
        mCodePresenter.attachView(this);

        mOwner = getIntent().getStringExtra(EXTRA_OWNER);
        mRepoName = getIntent().getStringExtra(EXTRA_REPO_NAME);
        mRepoTreeName.setText(mRepoName);

        loadContent(mOwner,mRepoName,null);
        mDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                mContentLayout.setLeft(mSlideLayout.getRight());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private long mLastPressBackTime = 0;
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - mLastPressBackTime < 10000) {
            finish();
        } else{
            mLastPressBackTime = System.currentTimeMillis();
            Toast.makeText(this,R.string.exit_click_back_again2,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        mCodePresenter.detachView();
    }

    private void initViews(){
        mSmoothProgress.setIndeterminate(false);
        mCodeProgress.setIndeterminate(false);
        mCodeView.setTheme(Theme.VS);
        mCodeView.setHighlightLanguage(Language.AUTO_DETECT);
        mCodeView.setOnScrollChangeListener(mCodeViewScrollListner);

        mPathView.addRootCrumb();
        mPathView.setCallback(mPathSelectionCallback);
        mAdapter = new RepoContentAdapter(this,null);
        mAdapter.setOnRecyclerViewItemClickListener(mItemClickListener);

        mRepoTree.setLayoutManager(new LinearLayoutManager(this));
        mRepoTree.addItemDecoration(new HorizontalDividerItemDecoration
                .Builder(this)
                .color(Color.TRANSPARENT)
                .size(getResources().getDimensionPixelSize(R.dimen.divider_height))
                .build());
        mRepoTree.setAdapter(mAdapter);

        mDrawerLayout.openDrawer(GravityCompat.START);

        initSaveButton();
        initSaveOptionsView();//gmi_check  gmi_plus  gmi_floppy  gmi_arrow_back  gmi_mail_reply
    }

    private void initSaveButton(){
        mSaveButton.setImageDrawable(new IconicsDrawable(this, MaterialDesignIconic.Icon.gmi_floppy).
                colorRes(R.color.md_grey_100).sizeRes(R.dimen.dimen_20));
        mSaveButton.setHideAnimation(AnimationUtils.loadAnimation(RepoTreeActivity.this,
                R.anim.hide_to_bottom));
        mSaveButton.setShowAnimation(AnimationUtils.loadAnimation(RepoTreeActivity.this,
                R.anim.show_from_bottom));
    }

    private void initSaveOptionsView(){
        mSaveOptionsInAnim = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF,0,
                TranslateAnimation.RELATIVE_TO_SELF,0, TranslateAnimation.RELATIVE_TO_SELF,1,
                TranslateAnimation.RELATIVE_TO_SELF,0);
        mSaveOptionsInAnim.setDuration(200);
        mSaveOptionsInAnim.setInterpolator(new AccelerateInterpolator());
        mSaveOptionsOutAnim = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF,0,
                TranslateAnimation.RELATIVE_TO_SELF,0, TranslateAnimation.RELATIVE_TO_SELF,0,
                TranslateAnimation.RELATIVE_TO_SELF,1);
        mSaveOptionsOutAnim.setDuration(200);
        mSaveOptionsOutAnim.setInterpolator(new AccelerateInterpolator());
    }

    private void showSaveOptionsLayout(){
        mSaveOptionsLayout.setVisibility(View.VISIBLE);
        mSaveOptionsLayout.startAnimation(mSaveOptionsInAnim);
    }

    private void hideSaveOptionsLayout(){
        mSaveOptionsLayout.setVisibility(View.INVISIBLE);
        mSaveOptionsLayout.startAnimation(mSaveOptionsOutAnim);
    }

    @OnClick({R.id.adr_save,R.id.adr_save_cancel,R.id.adr_save_complete,R.id.adr_dir_selector})
    protected void onClick(View view){
        switch (view.getId()){
            case R.id.adr_save:
                mSaveButton.hide(true);
                mSaveOptionsLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFileNameEdit.setText(mOpenedFileName);

                        String lastPath = AppPref.getLastSaveFileDir(RepoTreeActivity.this);
                        if(!TextUtils.isEmpty(lastPath)) {
                            mDirEdit.setText(lastPath);
                            mFileNameEdit.setText(FileUtil.getNoRepeatFileName(lastPath,mFileNameEdit.getText().toString()));
                            showSaveOptionsLayout();
                        } else{
                            File file = Environment.getExternalStorageDirectory();
                            if(!file.canWrite())
                                ToastUtils.showShortToast(RepoTreeActivity.this, "没有权限访问存储器");
                            else {
                                mDirEdit.setText(file.getAbsolutePath());
                                mFileNameEdit.setText(FileUtil.getNoRepeatFileName(mDirEdit.getText().toString(),
                                        mFileNameEdit.getText().toString()));
                                showSaveOptionsLayout();
                            }
                        }
                    }
                }, 200);
                break;
            case R.id.adr_save_cancel:
                hideSaveOptionsLayout();
                mSaveButton.show(true);
                break;
            case R.id.adr_save_complete:
                hideSaveOptionsLayout();
                String dir = mDirEdit.getText().toString();
                String filename = mFileNameEdit.getText().toString();
                AppPref.setLastSaveFileDir(this,dir);
                if(!FileUtils.createOrExistsDir(dir)) {
                    ToastUtils.showShortToast(this, "新建文件夹失败");
                    mSaveButton.show(true);
                }else{
                    saveFile(dir+"/"+filename);
                }
                break;
            case R.id.adr_dir_selector:
                BrowsePathPopup popup = new BrowsePathPopup(this, mDirEdit.getText().toString(),
                        new BrowsePathPopup.OnPathSelectedListener() {
                            @Override
                            public void onPathSelected(String selectedPath) {
                                mDirEdit.setText(selectedPath);
                                mFileNameEdit.setText(FileUtil.getNoRepeatFileName(mDirEdit.getText().toString(),
                                        mFileNameEdit.getText().toString()));
                            }
                        });
                popup.showPopupWindow();
        }
    }

    private boolean isSaved = false;
    private void saveFile(final String filePath){
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onStart();
                subscriber.onNext(FileUtils.writeFileFromString(filePath,mCodeString,false));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .doOnSubscribe(new Action0() {
              @Override
              public void call() {
                  mSaveButton.show(true);
                  mSaveButton.setIndeterminate(true);
              }
          })
          .doOnTerminate(new Action0() {
              @Override
              public void call() {
                 mSaveButton.setIndeterminate(false);
              }
          })
          .doOnNext(new Action1<Boolean>() {
              @Override
              public void call(Boolean result) {
                  if(result) {
                      ToastUtils.showShortToast(RepoTreeActivity.this, "保存成功");
                      mSaveButton.hide(true);
                      isSaved = true;
                  }else
                      ToastUtils.showShortToast(RepoTreeActivity.this,"保存失败");
              }
          }).subscribe();
    }

    private View.OnScrollChangeListener mCodeViewScrollListner = new View.OnScrollChangeListener() {
        @Override
        public void onScrollChange(View view, int x, int y, int oldx, int oldy) {
            if((y-oldy)>0) {
                mSaveButton.hide(true);
            }
            else if((y-oldy)<0 && !isSaved && isNeedSave) {
                mSaveButton.show(true);
            }
        }
    };

    private void loadContent(String owner,String reponame,String path){
        mPresenter.getRepoContents(owner,reponame,path);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        mPathView.getLocationOnScreen(location);
        RectF rect = new RectF(location[0], location[1], location[0] + mPathView.getWidth(),
                location[1] + mPathView.getHeight());
        // event.getX(); 获取相对于控件自身左上角的 x 坐标值
        // event.getY(); 获取相对于控件自身左上角的 y 坐标值
        float x = ev.getRawX(); // 获取相对于屏幕左上角的 x 坐标值
        float y = ev.getRawY(); // 获取相对于屏幕左上角的 y 坐标值
        boolean isInViewRect = rect.contains(x, y);
        if(isInViewRect){
            mPathView.dispatchTouchEvent(ev);
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void showLoading() {
        mSmoothProgress.setIndeterminate(true);
    }

    @Override
    public void dismissLoading() {
        mSmoothProgress.setIndeterminate(false);
    }

    @Override
    public RepoComponent getComponent() {
        return DaggerRepoComponent.builder()
                .applicationComponent(MyApplication.get(this).getComponent())
                .activityModule(new ActivityModule(this))
                .repoModule(new RepoModule())
                .build();
    }

    @Override
    public void showContent(ArrayList<Content> data) {
        TreeMap<String,Content> dirMap = new TreeMap<>();
        TreeMap<String,Content> fileMap = new TreeMap<>();
        for(int i=0;i<data.size();i++){
            Content content = data.get(i);
            if(content.isDir())
                dirMap.put(content.name,content);
            else
                fileMap.put(content.name,content);
        }
        ArrayList<Content> contents = new ArrayList<>();
        Iterator<String> iterator = dirMap.keySet().iterator();
        while(iterator.hasNext()){
            contents.add(dirMap.get(iterator.next()));
        }
        iterator = fileMap.keySet().iterator();
        while(iterator.hasNext()){
            contents.add(fileMap.get(iterator.next()));
        }

        mAdapter.setNewData(contents);
    }

    @Override
    public void showCode(Content data) {
        mCodeString = StringUtil.base64Decode(data.content);
        mCodeView.setSource(mCodeString);
        mCodeView.refresh();
        mCodeProgress.setIndeterminate(false);
        mSaveButton.show(true);
    }

    @Override
    public void showError(Throwable e) {
        CustomSnackbar.make(mDrawerLayout,e.getMessage(), Snackbar.LENGTH_LONG)
                .setTextColor(R.color.md_white_1000).show();
        if(mSmoothProgress.isIndeterminate())
            mSmoothProgress.setIndeterminate(false);
        if(mCodeProgress.isIndeterminate())
            mCodeProgress.setIndeterminate(false);
        AppLog.e(e);
    }

    @Override
    public void showEmpty() {

    }

    private View mLastSelectedView;
    private BaseQuickAdapter.OnRecyclerViewItemClickListener mItemClickListener =
            new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
        @Override
        public void onItemClick(View view, int i) {
            Content content = mAdapter.getItem(i);
            if(content.isDir()){
                mLastSelectedView = null;
                int index = content.path.lastIndexOf("/");
                mPathView.addCrumb(new LinearBreadcrumb.Crumb(content.path.substring(index+1),""),true);
                loadContent(mOwner,mRepoName,content.path);
            }else if(content.isFile()){
                if(mLastSelectedView != null){
                    ((TextView)mLastSelectedView.findViewById(R.id.file_name)).setTextColor(
                            ContextCompat.getColor(RepoTreeActivity.this,R.color.black));
                }
                ((TextView)view.findViewById(R.id.file_name)).setTextColor(
                        ContextCompat.getColor(RepoTreeActivity.this,R.color.md_blue_500));
                mLastSelectedView = view;

                int fileType = content.getFileType();
                if(fileType < Content.FILE_ZIP) {
                    mOpenedFileName = content.name;
                    openFile(fileType,content);
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        }
    };

    private boolean isNeedSave = true;
    private void openFile(int fileType,Content content){
        mSaveButton.hide(false);
        mSaveOptionsLayout.setVisibility(View.INVISIBLE);
        isSaved = false;

        switch (fileType){
            case Content.FILE_MEDIA:
                isNeedSave = false;
                mCodeProgress.setIndeterminate(true);
                mMediaView.setVisibility(View.VISIBLE);
                mCodeView.setVisibility(View.GONE);
                ImageLoader.loadGif(this,content.download_url, mMediaView, new ImageLoader.OnResourceReady() {
                    @Override
                    public void onResourceReady() {
                        mCodeProgress.setIndeterminate(false);
                    }

                    @Override
                    public void onError() {
                        mCodeProgress.setIndeterminate(false);
                        ToastUtils.showShortToast(RepoTreeActivity.this,"网络加载错误，可以尝试重试");
                    }
                });
                break;
            default:
                mCodeProgress.setIndeterminate(true);
                mCodePresenter.getContentDetail(mOwner, mRepoName, content.path);
                mMediaView.setVisibility(View.GONE);
                mCodeView.setVisibility(View.VISIBLE);
                break;
        }
    }

    private LinearBreadcrumb.SelectionCallback mPathSelectionCallback =
            new LinearBreadcrumb.SelectionCallback() {
        @Override
        public void onCrumbSelection(LinearBreadcrumb.Crumb crumb, String absolutePath, int count, int index) {
            loadContent(mOwner,mRepoName,absolutePath);
        }
    };
}
