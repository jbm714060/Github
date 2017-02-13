package com.bmj.greader.ui.module.repo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.utils.SnackbarUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.bmj.greader.common.wrapper.CustomSnackbar;
import com.bmj.greader.dagger2.component.MainComponent;
import com.bmj.greader.data.model.Languages;
import com.bmj.greader.data.pref.AppPref;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mikepenz.octicons_typeface_library.Octicons;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.bmj.greader.R;
import com.bmj.greader.common.wrapper.AppLog;
import com.bmj.greader.data.api.TrendingApi;
import com.bmj.greader.data.model.TrendingRepo;
import com.bmj.greader.presenter.main.TrendingRepoPresenter;
import com.bmj.greader.ui.base.BaseFragment;
import com.bmj.greader.ui.module.repo.adapter.TrendingRepoRecyclerAdapter;
import com.bmj.mvp.lce.LceView;

import javax.inject.Inject;

/**
 * Created by Administrator on 2016/11/18 0018.
 */
public class TrendingFragment extends BaseFragment implements LceView<ArrayList<TrendingRepo>>{
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.repo_list)
    RecyclerView mRepoListView;
    @BindView(R.id.spinner_languages)
    Spinner mLanguagesSpinner;
    @BindView(R.id.spinner_time)
    Spinner mTimeSpinner;
    @BindView(R.id.header)
    LinearLayout mHeaderLayout;

    @Inject
    TrendingRepoPresenter mPresenter;

    private TrendingRepoRecyclerAdapter mAdapter;
    private View emptyView;
    private TextView emptyVIewTips;
    private String mTrendingTime;
    private String mTrendingLanguage;
    private Long mLastLoadTime = 0l;
    private Animation mHideAnimation;
    private Animation mShowAnimation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(MainComponent.class).inject(this);
        mPresenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending,container,false);
        emptyView = inflater.inflate(R.layout.trending_empy_view,container,false);
        emptyVIewTips = (TextView)emptyView.findViewById(R.id.empty_title);

        ButterKnife.bind(this,view);
        initViews();

        return view;
    }

    private void loadData(String language,String time){
        if(System.currentTimeMillis() - mLastLoadTime <= 3000){
            if(!language.equals(mTrendingLanguage) || !time.equals(mTrendingTime)){
                mTrendingLanguage = language;
                mTrendingTime = time;
                mPresenter.loadTrendingRepo(mTrendingLanguage,mTrendingTime);
                mLastLoadTime = System.currentTimeMillis();
            }
        }else{
            mTrendingLanguage = language;
            mTrendingTime = time;
            mPresenter.loadTrendingRepo(mTrendingLanguage,mTrendingTime);
            mLastLoadTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private void initViews(){
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this.getContext(), R.layout.item_spinner,
                getResources().getStringArray(R.array.trending_time));
        timeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mTimeSpinner.setAdapter(timeAdapter);

        ArrayAdapter<String> languageAdapter = new ArrayAdapter<>(this.getContext(), R.layout.item_spinner,
                getResources().getStringArray(R.array.languages));
        languageAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mLanguagesSpinner.setAdapter(languageAdapter);

        mTimeSpinner.setSelection(AppPref.getTrendingTime(this.getContext()));
        mLanguagesSpinner.setSelection(AppPref.getTrendingLanguage(this.getContext()));
        String selectedTime = mTimeSpinner.getSelectedItem().toString();
        String selectedLanguage = mLanguagesSpinner.getSelectedItem().toString();
        loadData(selectedLanguage,selectedTime);

        mLanguagesSpinner.setOnItemSelectedListener(languageSpinnerListener);
        mTimeSpinner.setOnItemSelectedListener(timeSpinnerListener);

        mRefreshLayout.setOnRefreshListener(onRefreshListener);

        mAdapter = new TrendingRepoRecyclerAdapter(null);
        mAdapter.setOnRecyclerViewItemClickListener(onRecyclerViewItemClickListener);
        mAdapter.setEmptyView(emptyView);
        //mAdapter.openLoadAnimation();
        // 默认提供5种方法（渐显、缩放、从下到上，从左到右、从右到左）
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);

        mRepoListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRepoListView.addItemDecoration(new HorizontalDividerItemDecoration
                .Builder(getActivity())
                .color(Color.TRANSPARENT)
                .size(getResources().getDimensionPixelSize(R.dimen.divider_height))
                .build());
        mRepoListView.setAdapter(mAdapter);

        mHideAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_top);
        mShowAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);

        mRepoListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0){
                    //mHeaderLayout.startAnimation(mHideAnimation);
                    //mHeaderLayout.setVisibility(View.GONE);
                }
                else if(dy < 0) {
                    //mHeaderLayout.startAnimation(mShowAnimation);
                    //mHeaderLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            emptyVIewTips.setText(R.string.busy_carry);
            loadData(mTrendingLanguage,mTrendingTime);
        }
    };

    BaseQuickAdapter.OnRecyclerViewItemClickListener onRecyclerViewItemClickListener =
            new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
        @Override
        public void onItemClick(View view, int i) {
            TrendingRepo item = mAdapter.getItem(i);
            RepoDetailActivity.launch(getActivity(),item.getOwnUser(),item.getRepoName(),view.findViewById(R.id.itr_reponame));
        }
    };

    private AdapterView.OnItemSelectedListener languageSpinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String selectedLanguage = TrendingFragment.this.getResources().getStringArray(R.array.languages)[i];
            AppPref.setTrendingLanguage(TrendingFragment.this.getContext(),i);
            loadData(selectedLanguage,mTrendingTime);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    };

    private AdapterView.OnItemSelectedListener timeSpinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String selectedTime = TrendingFragment.this.getResources().getStringArray(R.array.trending_time)[i];
            AppPref.setTrendingTime(TrendingFragment.this.getContext(),i);
            loadData(mTrendingLanguage,selectedTime);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    };

    @Override
    public void showLoading() {
        mRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(true);
                }
            });
    }

    @Override
    public void dismissLoading() {
        mRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(false);
                }
            });
    }

    @Override
    public void showContent(ArrayList<TrendingRepo> data) {
        if (data != null && data.size() > 0) {
            mAdapter.setNewData(data);
        }else{
            emptyVIewTips.setText(R.string.trending_no_data);
        }
    }

    @Override
    public void showError(Throwable e) {
        emptyVIewTips.setText(R.string.carry_error);
        ToastUtils.showLongToast(this.getContext(),e.getMessage());
        AppLog.e(e);
    }

    @Override
    public void showEmpty() {

    }
}
