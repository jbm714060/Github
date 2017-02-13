package com.bmj.greader.ui.module.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Explode;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bmj.greader.common.util.TransitionHelper;
import com.bmj.greader.common.wrapper.ImageLoader;
import com.bmj.greader.data.model.User;
import com.bmj.greader.data.pref.AccountPref;
import com.bmj.greader.data.pref.AppPref;
import com.bmj.greader.ui.module.account.LoginActivity;
import com.bmj.greader.ui.module.repo.TrendingFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.bmj.greader.MyApplication;
import com.bmj.greader.R;
import com.bmj.greader.common.wrapper.AppLog;
import com.bmj.greader.dagger2.HasComponent;
import com.bmj.greader.dagger2.component.DaggerMainComponent;
import com.bmj.greader.dagger2.component.MainComponent;
import com.bmj.greader.dagger2.module.ActivityModule;
import com.bmj.greader.dagger2.module.RepoModule;
import com.bmj.greader.dagger2.module.TrendingRepoModule;
import com.bmj.greader.ui.base.BaseActivity;
import com.bmj.greader.ui.module.repo.MostStarFragment;
import com.bmj.greader.ui.module.repo.SearchActivity;
import com.bmj.greader.ui.module.repo.TrendingContainerFragment;

/**
 * Created by Administrator on 2016/11/15 0015.
 */
public class MainActivity extends BaseActivity implements HasComponent<MainComponent>{

    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;

    @BindView(R.id.content_frame)
    FrameLayout mContentFrame;

    @BindView(R.id.toolbar)
    Toolbar mToolBar;
    @BindView(R.id.am_avatar)
    ImageView mAvatar;

    private FragmentManager mFragmentManager = getSupportFragmentManager();

    public static void launch(Context context){
        context.startActivity(new Intent(context,MainActivity.class));
    }

    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private void grantePermission(){
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, permissions,1);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLog.e("trace===MainActivity onCreate");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mBottomBar.onRestoreInstanceState(savedInstanceState);
        mBottomBar.setShowDividers(0);
        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                AppLog.d("onTabSelected");
                changeTitle(tabId);
                switchTab(getFragmentName(tabId));
            }
        });

        mBottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                AppLog.d("onTabReSelected");
            }
        });

        grantePermission();
    }

    private Fragment mCurrentFragment;
    private void switchTab(String fragmentName){
        Fragment fragment = mFragmentManager.findFragmentByTag(fragmentName);

        if(fragment!=null){
            if(fragment == mCurrentFragment)
                return;
            mFragmentManager.beginTransaction().show(fragment).commit();
            if(fragmentName == MostStarFragment.class.getName())
                fragment.onResume();
        }else{
            fragment = Fragment.instantiate(this,fragmentName);
            mFragmentManager.beginTransaction().add(R.id.content_frame,fragment,fragmentName).commit();
        }

        if(mCurrentFragment != null) {
            mFragmentManager.beginTransaction().hide(mCurrentFragment).commit();
        }
        mCurrentFragment = fragment;
    }

    private String getFragmentName(int tabId){
        switch(tabId){
            case R.id.tabs_trending:
                return TrendingFragment.class.getName();
           // case R.id.tabs_account:
            //    return MineFragment.class.getName();
            case R.id.tabs_most_stars:
                return MostStarFragment.class.getName();
            default:
                return null;
        }
    }

    private void changeTitle(int tabId){

    }

    @Override
    public void onStart() {
        super.onStart();
        if(AppPref.isFirstRunning(this)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    IntroducePopup popup = new IntroducePopup(MainActivity.this);
                    popup.showPopupWindow();
                }
            },200);
        }
        updateUser();
    }

    private String mUserLogin = null;
    public void updateUser(){
        if(AccountPref.isLogon(this)) {
            User user = AccountPref.getLogonUser(this);
            mUserLogin = user.getLogin();
            ImageLoader.loadWithCircle(this, user.getAvatar_url(), mAvatar);
        }else{
            mAvatar.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_action_account_circle));
            mUserLogin = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mBottomBar.onSaveInstanceState();
        super.onSaveInstanceState(outState);
    }

    @OnClick({R.id.am_search_layout,R.id.am_avatar})
    protected void onClick(View view){
        if(view.getId() == R.id.am_search_layout)
            SearchActivity.launch(this);
        else{
            if(mUserLogin != null)
                UserDetailActivity.launch(this,mUserLogin);
            else
                LoginActivity.launch(this);
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_search){
            SearchActivity.launch(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    private long mLastPressBackTime = 0;
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - mLastPressBackTime < 10000) {
            finish();
        } else{
            mLastPressBackTime = System.currentTimeMillis();
            Toast.makeText(this,R.string.exit_click_back_again,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public MainComponent getComponent() {
        return DaggerMainComponent.builder()
                .applicationComponent(MyApplication.get(this).getComponent())
                .activityModule(new ActivityModule(this))
                .repoModule(new RepoModule())
                .trendingRepoModule(new TrendingRepoModule())
                .build();
    }
}
