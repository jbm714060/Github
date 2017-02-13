package com.bmj.greader.ui.module.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bmj.greader.R;
import com.bmj.greader.common.config.GithubConfig;
import com.bmj.greader.common.util.AppUtil;
import com.bmj.greader.ui.base.BaseActivity;
import com.bmj.greader.ui.module.account.UserActivity;

/**
 * Created by Administrator on 2016/11/21 0021.
 */
public class SettingsActivity extends BaseActivity{

    @BindView(R.id.night_mode_toggle)
    ToggleButton mNightModeToggle;
    @BindView(R.id.current_cache)
    TextView mCurrentCache;
    @BindView(R.id.current_version)
    TextView mCurrentVersion;

    public static void launch(Context context){
        context.startActivity(new Intent(context,SettingsActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        setTitle(R.string.settings);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
    }

    private void initViews(){
        mCurrentVersion.setText(AppUtil.getVersionName(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.night_mode, R.id.clear_cache, R.id.upgrade, R.id.about, R.id.about_author})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.night_mode:
                // TODO
                break;

            case R.id.clear_cache:
                // TODO
                break;

            case R.id.upgrade:
                // TODO
                break;

            case R.id.about:
                AboutActivity.launch(this);
                break;

            case R.id.about_author:
                UserActivity.launch(this, GithubConfig.AUTHOR_NAME);
                break;
        }
    }
}
