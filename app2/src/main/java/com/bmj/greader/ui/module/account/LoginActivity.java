package com.bmj.greader.ui.module.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.blankj.utilcode.utils.ToastUtils;
import com.bmj.greader.MyApplication;
import com.bmj.greader.R;
import com.bmj.greader.common.util.InputMethodUtil;
import com.bmj.greader.dagger2.HasComponent;
import com.bmj.greader.dagger2.component.AccountComponent;
import com.bmj.greader.dagger2.component.DaggerAccountComponent;
import com.bmj.greader.dagger2.module.AccountModule;
import com.bmj.greader.dagger2.module.ActivityModule;
import com.bmj.greader.data.model.User;
import com.bmj.greader.presenter.account.LoginPresenter;
import com.bmj.greader.ui.base.BaseLoadingActivity;
import com.bmj.greader.ui.module.account.view.LoginView;

/**
 * Created by Administrator on 2016/11/13 0013.
 */
public class LoginActivity extends BaseLoadingActivity implements LoginView,HasComponent<AccountComponent>{

    @BindView(R.id.icon)
    ImageView mIcon;
    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.login_btn)
    Button mLoginButton;

    @Inject
    LoginPresenter mPresenter;

    @OnClick(R.id.login_btn)
    protected void onClick(){
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        if(!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(password)){
            InputMethodUtil.hideSoftInput(this);
            mPresenter.login(username,password);
        }
    }

    public static void launch(Context context){
        context.startActivity(new Intent(context,LoginActivity.class));
    }

    public static void launchForResult(Activity activity){
        activity.startActivityForResult(new Intent(activity,LoginActivity.class),0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setTitle(R.string.loginActivity_title);
        mPresenter.attachView(this);
    }

    @Override
    public String getLoadingMessage() {
        return "Logining...";
    }

    @Override
    public AccountComponent getComponent() {
        return DaggerAccountComponent.builder()
                .applicationComponent(MyApplication.get(this.getApplicationContext()).getComponent())
                .accountModule(new AccountModule())
                .activityModule(new ActivityModule(this))
                .build();
    }

    @Override
    public void loginSuccess(User user) {
        ToastUtils.showShortToast(this,"Login Success");
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
