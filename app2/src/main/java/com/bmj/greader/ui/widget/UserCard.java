package com.bmj.greader.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.bmj.greader.R;
import com.bmj.greader.common.wrapper.ImageLoader;
import com.bmj.greader.data.model.User;

/**
 * Created by Administrator on 2016/11/15 0015.
 */
public class UserCard extends FrameLayout{

    @BindView(R.id.user_icon)
    ImageView mUserIcon;

    @BindView(R.id.username)
    TextView mUsername;

    @BindView(R.id.bio)
    TextView mBio;

    @BindView(R.id.company)
    TextView mCompany;

    @BindView(R.id.location)
    TextView mLocation;

    @BindView(R.id.blog)
    TextView mBlog;

    @BindView(R.id.email)
    TextView mEmail;

    public UserCard(Context context) {
        super(context);
        init();
    }

    public UserCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UserCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.card_user,this);
        ButterKnife.bind(this);
    }

    public void setUser(User user){
        ImageLoader.loadWithCircle(getContext(),user.getAvatar_url(),mUserIcon);
        mUsername.setText(TextUtils.isEmpty(user.getName())?user.getLogin():user.getName());
        if (TextUtils.isEmpty(user.getBio())) {
            mBio.setVisibility(View.GONE);
        }
        else {
            mBio.setVisibility(View.VISIBLE);
            mBio.setText(user.getBio());
        }

        if (TextUtils.isEmpty(user.getCompany())) {
            mCompany.setVisibility(View.GONE);
        }
        else {
            mCompany.setVisibility(View.VISIBLE);
            mCompany.setText(user.getCompany());
        }

        if (TextUtils.isEmpty(user.getBlog())) {
            mBlog.setVisibility(View.GONE);
        }
        else {
            mBlog.setVisibility(View.VISIBLE);
            mBlog.setText(user.getBlog());
        }

        if (TextUtils.isEmpty(user.getLocation())) {
            mLocation.setVisibility(View.GONE);
        }
        else {
            mLocation.setVisibility(View.VISIBLE);
            mLocation.setText(user.getLocation());
        }

        if (TextUtils.isEmpty(user.getEmail())) {
            mEmail.setVisibility(View.GONE);
        }
        else {
            mEmail.setVisibility(View.VISIBLE);
            mEmail.setText(user.getEmail());
        }
    }
}
