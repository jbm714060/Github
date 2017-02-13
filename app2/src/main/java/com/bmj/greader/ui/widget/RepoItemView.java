package com.bmj.greader.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bmj.greader.ui.module.repo.RepoUrlPopup;
import com.flyco.labelview.LabelView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bmj.greader.R;
import com.bmj.greader.common.wrapper.ImageLoader;
import com.bmj.greader.data.model.Repo;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.view.IconicsButton;
import com.mikepenz.iconics.view.IconicsCompatButton;
import com.mikepenz.octicons_typeface_library.Octicons;

/**
 * Created by Administrator on 2016/11/19 0019.
 */
public class RepoItemView extends FrameLayout{

    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.desc)
    TextView mDesc;
    @BindView(R.id.image)
    ImageView mUserIcon;
    @BindView(R.id.owner)
    TextView mOwner;
    @BindView(R.id.update_time)
    TextView mUpdateTime;
    @BindView(R.id.star)
    TextView mStarCount;
    @BindView(R.id.star_view)
    LinearLayout mStarView;
    @BindView(R.id.label_view)
    LabelView mLabelView;
    @BindView(R.id.star_icon)
    ImageView mStarIcon;
    @BindView(R.id.external_link)
    ImageButton mExternalLinkButton;

    private Repo mRepo;

    public RepoItemView(Context context) {
        super(context);
        init();
    }

    public RepoItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RepoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.layout_repo,this);
        ButterKnife.bind(this);
        mLabelView.setVisibility(INVISIBLE);
        mExternalLinkButton.setImageDrawable(new IconicsDrawable(this.getContext()).
                icon("gmi_link").sizeDp(24).colorRes(R.color.md_grey_600));
        MaterialRippleLayout.on(mStarView)
                .rippleAlpha(0.2f)
                .rippleColor(ContextCompat.getColor(this.getContext(),R.color.md_grey_500))
                .rippleHover(true)
                .rippleOverlay(true)
                .create();
    }

    public void setRepo(Repo repo){
        mRepo = repo;
        mName.setText(mRepo.getName());
        mDesc.setText(mRepo.getDescription());

        ImageLoader.load(getContext(),mRepo.getOwner().getAvatar_url(),mUserIcon);
        mOwner.setText(mRepo.getOwner().getLogin());

        if(!TextUtils.isEmpty(mRepo.getLanguage())){
            mLabelView.setVisibility(VISIBLE);
            mLabelView.setText(mRepo.getLanguage());
        }else{
            mLabelView.setVisibility(GONE);
        }
        mUpdateTime.setText(mRepo.getUpdated_at());
        mStarCount.setText(String.valueOf(mRepo.getStargazers_count()));
        setStarIcon(repo.isStarred());
        mExternalLinkButton.setVisibility(VISIBLE);
    }

    public void setStarIcon(boolean isStared){
        mRepo.setStarred(isStared);
        mStarIcon.setImageResource(isStared ?
                R.drawable.ic_star_selected : R.drawable.ic_star);
    }

    @OnClick(R.id.star_view)
    protected void onStarViewClicked(){
        if(mRepoActionListener != null){
            if(mRepo.isStarred()){
                mRepoActionListener.onUnstarAction(mRepo);
            }else{
                mRepoActionListener.onStarAction(mRepo);
            }
        }
    }

    @OnClick(R.id.external_link)
    protected void onExternalLinkClicked(){
        RepoUrlPopup popup = new RepoUrlPopup(((Activity)getContext()),
                mRepo.getHomepage(),mRepo.getHtml_url(),
                mRepo.getClone_url(),mRepo.getSvn_url());
        popup.showPopupWindow();
    }

    @OnClick(R.id.image)
    protected void onUserIconClicked(){
        if(mRepoActionListener!=null){
            mRepoActionListener.onUserAction(mRepo.getOwner().getLogin());
        }
    }

    private RepoActionListener mRepoActionListener;
    public void setRepoActionListener(RepoActionListener repoActionListener){
        mRepoActionListener = repoActionListener;
    }

    public interface RepoActionListener{
        void onStarAction(Repo repo);
        void onUnstarAction(Repo repo);
        void onUserAction(String username);
    }
}
