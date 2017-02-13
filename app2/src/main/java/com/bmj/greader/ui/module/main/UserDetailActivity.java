package com.bmj.greader.ui.module.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.Transition;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.bmj.greader.MyApplication;
import com.bmj.greader.R;
import com.bmj.greader.common.util.TransitionHelper;
import com.bmj.greader.common.wrapper.AppLog;
import com.bmj.greader.common.wrapper.CustomSnackbar;
import com.bmj.greader.common.wrapper.ImageLoader;
import com.bmj.greader.dagger2.HasComponent;
import com.bmj.greader.dagger2.component.DaggerRepoComponent;
import com.bmj.greader.dagger2.component.RepoComponent;
import com.bmj.greader.dagger2.module.ActivityModule;
import com.bmj.greader.dagger2.module.RepoModule;
import com.bmj.greader.data.model.User;
import com.bmj.greader.data.pref.AccountPref;
import com.bmj.greader.presenter.account.UserPresenter;
import com.bmj.greader.presenter.repo.FollowUserPresenter;
import com.bmj.greader.ui.base.BaseLoadingActivity;
import com.bmj.greader.ui.base.SimpleTransitionListener;
import com.bmj.greader.ui.module.repo.adapter.UserDetailFragmentAdapter;
import com.bmj.greader.ui.module.repo.view.FollowActionView;
import com.bmj.greader.ui.widget.DrawerUserHeader;
import com.bmj.mvp.lce.LceView;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.octicons_typeface_library.Octicons;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/11 0011.
 */
public class UserDetailActivity extends BaseLoadingActivity implements LceView<User>,FollowActionView,HasComponent<RepoComponent>{
    @BindView(R.id.root_layout)
    View rootLayout;
    @BindView(R.id.aud_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.aud_viewpager)
    ViewPager mViewPager;

    @BindView(R.id.fo_info_company)
    TextView mCompanyView;
    @BindView(R.id.fo_info_location)
    TextView mLocationView;
    @BindView(R.id.fo_info_blog)
    TextView mBlogView;
    @BindView(R.id.fo_info_email)
    TextView mEmailView;
    @BindView(R.id.fo_info_join)
    TextView mJoinView;
    @BindView(R.id.fo_info_avatar)
    ImageView mAvatarView;
    @BindView(R.id.fo_info_name)
    TextView mNameView;
    @BindView(R.id.fo_info_bio)
    TextView mBioView;
    @BindView(R.id.fo_info_login)
    TextView mLoginView;
    @BindView(R.id.fo_info_blog_layout)
    LinearLayout mBlogLayout;
    @BindView(R.id.fo_info_company_layout)
    LinearLayout mCompanyLayout;
    @BindView(R.id.fo_info_location_layout)
    LinearLayout mLocationLayout;
    @BindView(R.id.fo_info_email_layout)
    LinearLayout mEmailLayout;

    @BindView(R.id.aud_follow)
    TextView mFollowView;
    @BindView(R.id.aud_follow_layout)
    LinearLayout mFollowLayout;

    @Inject
    UserPresenter mPresenter;
    @Inject
    FollowUserPresenter mFollowPresenter;

    private Drawer mDrawer;
    private DrawerUserHeader mDrawerUserHeader;
    private PrimaryDrawerItem[] drawerItems;
    private String userLogin;
    private UserDetailFragmentAdapter mFragmentAdapter;

    public int mReposCount;
    public int mStarredReposCount;
    public int mFollowersCount;
    public int mFollowingCount;
    public int mGistCount;

    private static final String EXTRA_USERLOGIN = "userlogin";
    public static void launch(Context context,String userLogin){
        Intent intent = new Intent(context,UserDetailActivity.class);
        intent.putExtra(EXTRA_USERLOGIN,userLogin);
        Pair<View,String>[] pairs = TransitionHelper.createSafeTransitionParticipants((Activity) context,false,false);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,pairs);
        context.startActivity(intent,optionsCompat.toBundle());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        ButterKnife.bind(this);

        setWindowAnimation();

        getComponent().inject(this);
        mPresenter.attachView(this);
        mFollowPresenter.attachView(this);

        setSupportActionBar(mToolbar);

        userLogin = getIntent().getStringExtra(EXTRA_USERLOGIN);
        mFragmentAdapter = new UserDetailFragmentAdapter(getSupportFragmentManager(),userLogin);
        setTitle(userLogin);

        initViews();
    }

    private  void setWindowAnimation(){
        getWindow().setAllowReturnTransitionOverlap(true);
        getWindow().setAllowEnterTransitionOverlap(true);

        final Fade enterfade = new Fade();
        enterfade.setDuration(500);
        enterfade.setInterpolator(new AccelerateDecelerateInterpolator());
        getWindow().setEnterTransition(enterfade);
        enterfade.addListener(new SimpleTransitionListener(){
            @Override
            public void onTransitionEnd(Transition transition) {
                super.onTransitionEnd(transition);
                transition.removeListener(this);
                loadData();
            }
        });

        Fade exitfade = new Fade();
        exitfade.setDuration(500);
        exitfade.setInterpolator(new AccelerateDecelerateInterpolator());
        getWindow().setExitTransition(exitfade);
    }

    private void loadData(){
        mPresenter.getSingleUser(userLogin);
        if(!AccountPref.isSelf(this,userLogin))
            mFollowPresenter.getFollowUserState(userLogin);
    }

    private void initViews(){
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setOffscreenPageLimit(2);

        drawerItems = new PrimaryDrawerItem[5];
        drawerItems[0] = new PrimaryDrawerItem().withIcon(Octicons.Icon.oct_repo).withName("Repositories").withBadge("0")
                .withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700));
        drawerItems[1] = new PrimaryDrawerItem().withIcon(Octicons.Icon.oct_star).withName("Stars").withBadge("0")
                .withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700));
        drawerItems[2] = new PrimaryDrawerItem().withIcon(Octicons.Icon.oct_person_follow).withName("Followers").withBadge("0")
                .withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700));
        drawerItems[3] = new PrimaryDrawerItem().withIcon(Octicons.Icon.oct_person_follow).withName("Following").withBadge("0")
                .withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700));
        drawerItems[4] = new PrimaryDrawerItem().withIcon(Octicons.Icon.oct_gist).withName("Gist").withBadge("0")
                .withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700));

        mDrawerUserHeader = new DrawerUserHeader(this);
        mDrawerUserHeader.withBackground(R.drawable.userdrawer3);
        DrawerBuilder drawerBuilder = new DrawerBuilder().withActivity(this)
                .withToolbar(mToolbar)
                .withHeader(mDrawerUserHeader)
                .withDisplayBelowStatusBar(true)
                .withCloseOnClick(true)
                .withActionBarDrawerToggle(true)
                .withTranslucentStatusBar(true)
                .withDrawerWidthRes(R.dimen.dimen_300)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(drawerItems)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        mViewPager.setCurrentItem(position-1);
                        mDrawer.closeDrawer();
                        return true;
                    }
                });

        if(AccountPref.isSelf(this,userLogin)){
            View footerView = getLayoutInflater().inflate(R.layout.drawer_user_footer,null);
            TextView loginout = (TextView) footerView.findViewById(R.id.loginout_button);
            loginout.setOnClickListener(mLoginViewClickedListener);
            footerView.findViewById(R.id.loginout_layout).setOnClickListener(mLoginViewClickedListener);
            drawerBuilder.withStickyFooterDivider(false)
                    .withStickyFooterShadow(false)
                    .withStickyFooter((ViewGroup) footerView);
        }
        mDrawer = drawerBuilder.build();
    }

    View.OnClickListener mLoginViewClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.loginout_button){
                AccountPref.removeLogonUser(UserDetailActivity.this);
                UserDetailActivity.this.onBackPressed();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        mFollowPresenter.detachView();
    }

    @Override
    public RepoComponent getComponent() {
        return DaggerRepoComponent.builder().applicationComponent(MyApplication.get(this).getComponent())
                .activityModule(new ActivityModule(this))
                .repoModule(new RepoModule())
                .build();
    }

    @Override
    public String getLoadingMessage() {
        return null;
    }

    public void showContent(User data) {
        mToolbar.setTitle(data.getLogin());
        //mToolbar.setSubtitle(data.getName());

        mDrawerUserHeader.withName(data.getName()).withBio(data.getBio());
        ImageLoader.loadWithCircle(data.getAvatar_url(),mDrawerUserHeader.getAvatar());

        drawerItems[0].withBadge(data.getPublic_repos()+"");
        mDrawer.updateItem(drawerItems[0]);
        drawerItems[2].withBadge(data.getFollowers()+"");
        mDrawer.updateItem(drawerItems[2]);
        drawerItems[3].withBadge(data.getFollowing()+"");
        mDrawer.updateItem(drawerItems[3]);
        drawerItems[4].withBadge(data.getPublic_gists()+"");
        mDrawer.updateItem(drawerItems[4]);

        mReposCount = data.getPublic_repos();
        mStarredReposCount = 0;
        mFollowersCount = data.getFollowers();
        mFollowingCount = data.getFollowing();
        mGistCount = data.getPublic_gists();

        if(!TextUtils.isEmpty(data.getCompany()))
            mCompanyView.setText(data.getCompany());
        else
            mCompanyLayout.setVisibility(View.GONE);

        if(!TextUtils.isEmpty(data.getLocation()))
            mLocationView.setText(data.getLocation());
        else
            mLocationLayout.setVisibility(View.GONE);
        if(!TextUtils.isEmpty(data.getBlog()))
            mBlogView.setText(data.getBlog());
        else
            mBlogLayout.setVisibility(View.GONE);
        if(!TextUtils.isEmpty(data.getEmail()))
            mEmailView.setText(data.getEmail());
        else
            mEmailLayout.setVisibility(View.GONE);
        if(!TextUtils.isEmpty(data.getCreated_at()))
            mJoinView.setText(data.getCreated_at());

        ImageLoader.load(data.getAvatar_url(),mAvatarView);
        if(!TextUtils.isEmpty(data.getName()))
            mNameView.setText(data.getName());
        else
            mNameView.setVisibility(View.GONE);
        if(!TextUtils.isEmpty(data.getBio()))
            mBioView.setText(data.getBio());
        else
            mBioView.setVisibility(View.GONE);
        mLoginView.setText(data.getLogin());
    }

    public void setStarredReposCount(int count){
        drawerItems[1].withBadge(count+"");
        mDrawer.updateItem(drawerItems[1]);
    }

    @Override
    public void showError(Throwable e) {
        AppLog.e(e);
        CustomSnackbar.make(getWindow().getDecorView(), e.getMessage(), Snackbar.LENGTH_LONG)
                .setTextColor(R.color.md_white_1000).show();
    }

    @Override
    public void showEmpty() {

    }

    @OnClick(R.id.aud_follow_layout)
    protected void onClick(View view){
        if(AccountPref.checkLogon(this)) {
            String text = mFollowView.getText().toString();
            if (text.equals(getResources().getString(R.string.follow))) {
                mFollowView.setText(R.string.following2);
                mFollowPresenter.followUser(userLogin);
            } else {
                mFollowView.setText(R.string.unfollowing);
                mFollowPresenter.unfollowUser(userLogin);
            }
        }
    }

    @Override
    public void showFollowState(boolean isFollowing) {
        if(isFollowing){
            mFollowView.setText(R.string.unfollow);
        }else{
            mFollowView.setText(R.string.follow);
        }
        mFollowLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFollowIsSuccess(boolean isSuccess) {
        if(isSuccess) {
            mFollowView.setText(R.string.unfollow);
            ToastUtils.showShortToast(this,"Follow Successfully");
        } else {
            mFollowView.setText(R.string.follow);
            ToastUtils.showShortToast(this,"Follow Failed");
        }
    }

    @Override
    public void showUnfollowIsSuccess(boolean isSuccess) {
        if(isSuccess){
            mFollowView.setText(R.string.follow);
            ToastUtils.showShortToast(this,"Unfollow Successfully");
        } else{
            mFollowView.setText(R.string.unfollow);
            ToastUtils.showShortToast(this,"Unfollow Failed");
        }
    }
}
