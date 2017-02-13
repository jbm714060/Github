package com.bmj.greader.ui.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.bmj.greader.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/25 0025.
 */
public class AdvancedSearchView extends RelativeLayout{

    //RadioGroup
    @BindView(R.id.as_search_type)
    RadioGroup mRadioGroup;
    @BindView(R.id.as_search_repo)
    RadioButton mRadioRepo;
    @BindView(R.id.as_search_user)
    RadioButton mRadioUser;

    //Repo Options
    @BindView(R.id.repo_options_layout)
    RelativeLayout mRepoOptionsLayout;
    @BindView(R.id.as_afo_edit)
    EditText mRepoOpOwnersEdit;
    @BindView(R.id.as_afo_clear)
    ImageButton mRepoOpOwnersEditClear;

    @BindView(R.id.as_acd_edit)
    EditText mRepoOpDataEdit;
    @BindView(R.id.as_acd_clear)
    ImageButton mRepoOpDataEditClear;

    @BindView(R.id.as_owl_langspinner)
    Spinner mRepoOpLangSpinner;

    @BindView(R.id.as_rws_edit)
    EditText mRepoOpStarsEdit;
    @BindView(R.id.as_rws_clear)
    ImageButton mRepoOpStarsEditClear;

    @BindView(R.id.as_rwf_edit)
    EditText mRepoOpForksEdit;
    @BindView(R.id.as_rwf_clear)
    ImageButton mRepoOpForksEditClear;

    //user options
    @BindView(R.id.as_user_options_layout)
    RelativeLayout mUserOptionsLayout;
    @BindView(R.id.as_ufl_edit)
    EditText mUserOpLocationEdit;
    @BindView(R.id.as_ufl_clear)
    ImageButton mUserOpLocationEditClear;

    @BindView(R.id.as_uwf_edit)
    EditText mUserOpFollowersEdit;
    @BindView(R.id.as_uwf_clear)
    ImageButton mUserOpFollowersEditClear;

    @BindView(R.id.as_uwr_edit)
    EditText mUserOpReposEdit;
    @BindView(R.id.as_uwr_clear)
    ImageButton mUserOpReposEditClear;

    private View mRootView;
    public HashMap<String,String> queryMap = new HashMap<>();

    public AdvancedSearchView(Context context) {
        super(context);
        init(context);
    }

    public AdvancedSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AdvancedSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mRootView = LayoutInflater.from(context).inflate(R.layout.advanced_search,this,true);
        ButterKnife.bind(this);
        mRadioGroup.setOnCheckedChangeListener(mCheckedChangeListener);
    }

    public void SetVisibility(int visibility){
        if(visibility == VISIBLE){
            this.setVisibility(VISIBLE);
            mRepoOpStarsEdit.setText("");
            mRepoOpForksEdit.setText("");
            mRepoOpOwnersEdit.setText("");
            mRepoOpDataEdit.setText("");
            mUserOpLocationEdit.setText("");
            mUserOpFollowersEdit.setText("");
            mUserOpReposEdit.setText("");
        }else if(visibility == INVISIBLE){
            this.setVisibility(INVISIBLE);
        }else if(visibility == GONE){
            this.setVisibility(GONE);
        }
    }

    public HashMap<String, String> getAdvancedOptions(){
        if(mRadioGroup.getCheckedRadioButtonId() == R.id.as_search_user)
            queryMap.put("type","Users");
        else
            queryMap.put("type","Repos");

        String text = mRepoOpOwnersEdit.getText().toString();
        if(!TextUtils.isEmpty(text))
            queryMap.put("user",text);
        else
            queryMap.remove("user");

        text = mRepoOpDataEdit.getText().toString();
        if(!TextUtils.isEmpty(text))
            queryMap.put("created",text);
        else
            queryMap.remove("created");

        text = getResources().getStringArray(R.array.languages)[mRepoOpLangSpinner.getSelectedItemPosition()];
        if(!TextUtils.isEmpty(text))
            queryMap.put("language",text);
        else
            queryMap.remove("language");

        text = mRepoOpStarsEdit.getText().toString();
        if(!TextUtils.isEmpty(text))
            queryMap.put("stars",text);
        else
            queryMap.remove("stars");

        text = mRepoOpForksEdit.getText().toString();
        if(!TextUtils.isEmpty(text))
            queryMap.put("forks",text);
        else
            queryMap.remove("forks");

        text = mUserOpLocationEdit.getText().toString();
        if(!TextUtils.isEmpty(text))
            queryMap.put("location",text);
        else
            queryMap.remove("location");

        text = mUserOpFollowersEdit.getText().toString();
        if(!TextUtils.isEmpty(text))
            queryMap.put("followers",text);
        else
            queryMap.remove("followers");

        text = mUserOpReposEdit.getText().toString();
        if(!TextUtils.isEmpty(text))
            queryMap.put("repos",text);
        else
            queryMap.remove("repos");

        return queryMap;
    }

   private RadioGroup.OnCheckedChangeListener mCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if(i == R.id.as_search_repo) {
                mUserOptionsLayout.setVisibility(GONE);
                mRadioUser.setBackground(getResources().getDrawable(R.drawable.button_bg5));

                mRepoOptionsLayout.setVisibility(VISIBLE);
                mRadioRepo.setBackgroundColor(ContextCompat.getColor(AdvancedSearchView.this.getContext(),R.color.md_grey_50));

            }else{
                mUserOptionsLayout.setVisibility(VISIBLE);
                mRadioUser.setBackgroundColor(ContextCompat.getColor(AdvancedSearchView.this.getContext(),R.color.md_grey_50));

                mRepoOptionsLayout.setVisibility(GONE);
                mRadioRepo.setBackground(getResources().getDrawable(R.drawable.button_bg5));
            }
        }
    };
}
