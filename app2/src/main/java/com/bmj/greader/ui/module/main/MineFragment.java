package com.bmj.greader.ui.module.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bmj.greader.common.wrapper.FeedbackPlatform;
import com.bmj.greader.common.wrapper.ImageLoader;
import com.bmj.greader.common.wrapper.SharePlatform;
import com.bmj.greader.data.model.User;
import com.bmj.greader.data.pref.AccountPref;
import com.bmj.greader.ui.base.BaseFragment;
import com.bmj.greader.R;
import com.bmj.greader.ui.module.account.LoginActivity;
import com.bmj.greader.ui.module.account.UserActivity;
import com.bmj.greader.ui.module.setting.SettingsActivity;

/**
 * Created by Administrator on 2016/11/15 0015.
 */
public class MineFragment extends BaseFragment{
    @BindView(R.id.user_icon)
    ImageView mUserIcon;
    @BindView(R.id.username)
    TextView mUsername;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_mine,null);
        ButterKnife.bind(this,contentView);
        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUser();
    }

    private void updateUser(){
        if(AccountPref.isLogon(getActivity())){
            User user = AccountPref.getLogonUser(getActivity());
            ImageLoader.loadWithCircle(getActivity(),user.getAvatar_url(),mUserIcon);
            String displayName = TextUtils.isEmpty(user.getName())?user.getLogin():user.getName();
            mUsername.setText(displayName);
        }else{
            mUsername.setText(R.string.please_login);
        }
    }

    @OnClick({R.id.account_view, R.id.history, R.id.share_app, R.id.feedback, R.id.settings})
    protected void onClick(View view){
        switch (view.getId()) {
            case R.id.account_view:
                if (AccountPref.isLogon(getContext())) {
                    UserActivity.launch(getActivity(), AccountPref.getLogonUser(getActivity()));
                }
                else {
                    LoginActivity.launch(getActivity());
                }
                break;
            case R.id.history:
                // TODO
                break;

            case R.id.share_app:
                SharePlatform.share(getActivity());
                break;

            case R.id.feedback:
                FeedbackPlatform.openFeedback(getActivity());
                break;

            case R.id.settings:
                SettingsActivity.launch(getActivity());
                break;
        }
    }
}
