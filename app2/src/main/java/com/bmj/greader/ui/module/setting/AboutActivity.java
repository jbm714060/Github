package com.bmj.greader.ui.module.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.bmj.greader.R;
import com.bmj.greader.ui.base.BaseActivity;

/**
 * Created by Administrator on 2016/11/22 0022.
 */
public class AboutActivity extends BaseActivity{

    public static void launch(Context context){
        context.startActivity(new Intent(context,AboutActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle(R.string.about);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
