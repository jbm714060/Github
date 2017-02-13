package com.bmj.greader.ui.module.repo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bmj.greader.R;
import com.bmj.greader.data.model.Repo;
import com.bmj.greader.ui.base.BaseFragment;
import com.bmj.greader.ui.module.repo.adapter.RepoListRecyclerAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/28 0028.
 */
public class RepoListFragment extends BaseFragment{
    @BindView(R.id.rview_repolist)
    RecyclerView mRepoListRecycler;

    private RepoListRecyclerAdapter mRepoListAdapter;

    public static RepoListFragment newInstance(ArrayList<Repo> repoList){
        RepoListFragment repoListFragment = new RepoListFragment();
        Bundle arg = new Bundle();
        arg.putParcelableArrayList("repolist",repoList);
        repoListFragment.setArguments(arg);
        return repoListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repo_list,null);
        ButterKnife.bind(this,view);
        initViews();
        return view;
    }

    private void initViews(){
        ArrayList<Repo> repoList = getArguments().getParcelableArrayList("repolist");
        mRepoListAdapter = new RepoListRecyclerAdapter(repoList);
        mRepoListAdapter.setOnRecyclerViewItemClickListener(mItemClickListener);
        mRepoListRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRepoListRecycler.addItemDecoration(new HorizontalDividerItemDecoration
                .Builder(getActivity())
                .sizeResId(R.dimen.dimen_5)
                .color(Color.TRANSPARENT)
                .build()
        );
        mRepoListRecycler.setAdapter(mRepoListAdapter);
    }

    BaseQuickAdapter.OnRecyclerViewItemClickListener mItemClickListener =
            new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int i) {
                    Repo repo = mRepoListAdapter.getItem(i);
                    RepoDetailActivity.launch(getActivity(),repo.getOwner().getLogin(),repo.getName(),
                            view.findViewById(R.id.item_repo_name));
                }
            };
}
