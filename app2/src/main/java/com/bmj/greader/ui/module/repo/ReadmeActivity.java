package com.bmj.greader.ui.module.repo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.mikepenz.octicons_typeface_library.Octicons;
import com.mukesh.MarkdownView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.bmj.greader.R;
import com.bmj.greader.data.net.response.Content;
import com.bmj.greader.ui.base.BaseActivity;

/**
 * Created by Administrator on 2016/11/20 0020.
 */
public class ReadmeActivity extends BaseActivity{
    @BindView(R.id.readme_content)
    MarkdownView mMarkdownView;

    private static final String EXTRA_README = "extra_readme";
    public static void launch(Context context, Content readme){
        Intent intent = new Intent(context,ReadmeActivity.class);
        intent.putExtra(EXTRA_README,readme);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readme);
        ButterKnife.bind(this);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
    }

    private void initViews(){
        Content readmeContent = getIntent().getParcelableExtra(EXTRA_README);
        mMarkdownView.setMarkDownText(readmeContent.content);
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
