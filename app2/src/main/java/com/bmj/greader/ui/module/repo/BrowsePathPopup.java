package com.bmj.greader.ui.module.repo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.afollestad.breadcrumb.LinearBreadcrumb;
import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.bmj.greader.R;
import com.bmj.greader.common.util.InputMethodUtil;
import com.bmj.greader.ui.module.repo.adapter.DirectoryRecyclerAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by Administrator on 2017/1/17 0017.
 */
public class BrowsePathPopup extends BasePopupWindow{
    @BindView(R.id.fbp_return)
    ImageView mReturnButton;
    @BindView(R.id.fbp_current_path)
    LinearBreadcrumb mCurrentPathView;
    @BindView(R.id.fbp_dir_list)
    RecyclerView mDirList;
    @BindView(R.id.fbp_add_dir)
    ImageView mAddDirButton;
    @BindView(R.id.fbp_ok)
    ImageView mOkButton;

    @BindView(R.id.fbp_input_cancel)
    ImageView mInputCanCelButton;
    @BindView(R.id.fbp_input_ok)
    ImageView mInputOkButton;
    @BindView(R.id.fbp_input_dir)
    EditText mDirEdit;
    @BindView(R.id.fbp_input_layout)
    RelativeLayout mInputLayout;

    private DirectoryRecyclerAdapter mDirAdapter;
    private OnPathSelectedListener mSelectedListener;

    public BrowsePathPopup(@NonNull Activity activity,@NonNull String path,@NonNull OnPathSelectedListener selectedListener){
        super(activity);
        mSelectedListener = selectedListener;

        mCurrentPathView.addPath(path,"","/");
        ArrayList<String> subDirs = new ArrayList<>();
        getSubDirs(path,subDirs);
        mDirAdapter = new DirectoryRecyclerAdapter(subDirs);
        mDirList.setLayoutManager(new LinearLayoutManager(getContext()));
        mDirList.setAdapter(mDirAdapter);
        mDirAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                String name = mDirAdapter.getItem(i);
                String curPath = mCurrentPathView.getAbsolutePath(mCurrentPathView
                        .getCrumb(mCurrentPathView.getActiveIndex()),"/");
                if(curPath.endsWith("/"))
                    curPath += name;
                else
                    curPath += "/"+name;

                ArrayList<String> subDirs = new ArrayList<>();
                if(getSubDirs(curPath,subDirs)) {
                    mCurrentPathView.addCrumb(new LinearBreadcrumb.Crumb(name,""),true);
                    mDirAdapter.setNewData(subDirs);
                    mReturnButton.setVisibility(View.VISIBLE);
                }
            }
        });
        mReturnButton.setImageDrawable(new IconicsDrawable(getContext(),
                MaterialDesignIconic.Icon.gmi_mail_reply).sizeRes(R.dimen.dimen_24)
                .colorRes(R.color.md_grey_600));
        mAddDirButton.setImageDrawable(new IconicsDrawable(getContext(),
                MaterialDesignIconic.Icon.gmi_plus).sizeRes(R.dimen.dimen_20)
                .colorRes(R.color.md_grey_600));
        mOkButton.setImageDrawable(new IconicsDrawable(getContext(),
                MaterialDesignIconic.Icon.gmi_check).sizeRes(R.dimen.dimen_20)
                .colorRes(R.color.md_green_400));
        mInputCanCelButton.setImageDrawable(new IconicsDrawable(getContext(),
                MaterialDesignIconic.Icon.gmi_close).sizeRes(R.dimen.dimen_16)
                .colorRes(R.color.md_grey_600));
        mInputOkButton.setImageDrawable(new IconicsDrawable(getContext(),
                MaterialDesignIconic.Icon.gmi_check).sizeRes(R.dimen.dimen_20)
                .colorRes(R.color.md_grey_600));
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.fbp_anim);
    }

    @Override
    public View onCreatePopupView() {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.file_browse_popup,null);
        ButterKnife.bind(this,root);
        return root;
    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    protected Animator initShowAnimator() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mAnimaView,"alpha",0,1f).setDuration(300);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha);
        return set;
    }

    @Override
    protected Animator initExitAnimator() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mAnimaView,"alpha",1f,0f).setDuration(300);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mAnimaView,"scaleX",1f,0f).setDuration(300);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mAnimaView,"scaleY",1,0f).setDuration(300);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha,scaleX,scaleY);
        return set;
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    private boolean getSubDirs(String path,ArrayList<String> subDirs){
        File file;
        if(!TextUtils.isEmpty(path)) {
            file = new File(path);
            File[] files = file.listFiles();
            if(files== null) {
                ToastUtils.showShortToast(getContext(), "没有权限访问");
                return false;
            }

            for (File item:files){
                if(item.isDirectory())
                    subDirs.add(item.getName());
            }
            return true;
        }else {
            return false;
        }
    }

    @OnClick({R.id.fbp_add_dir,R.id.fbp_return,R.id.fbp_ok,R.id.fbp_input_ok,R.id.fbp_input_cancel})
    protected void onClick(View view){
        switch(view.getId()){
            case R.id.fbp_add_dir:
                mInputLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.fbp_return:
                Log.i("showgist",mCurrentPathView.getAbsolutePath(
                        mCurrentPathView.getCrumb(mCurrentPathView.getActiveIndex()-1),"/"));
                File curParent = FileUtils.getFileByPath(mCurrentPathView.getAbsolutePath(
                        mCurrentPathView.getCrumb(mCurrentPathView.getActiveIndex()-1),"/"));
                if(curParent!=null && curParent.exists()){
                    if(!curParent.canWrite()){
                        ToastUtils.showShortToast(getContext(), "没有权限访问");
                    }else {
                        mCurrentPathView.setActive(mCurrentPathView.getCrumb(mCurrentPathView.getActiveIndex()-1));
                        if (mCurrentPathView.size() == 1 || curParent.getParent() == null) {
                            mReturnButton.setVisibility(View.GONE);
                        }

                        ArrayList<String> subs = new ArrayList<>();
                        getSubDirs(curParent.getAbsolutePath(), subs);
                        mDirAdapter.setNewData(subs);
                    }
                }
                break;
            case R.id.fbp_ok:
                mSelectedListener.onPathSelected(mCurrentPathView.getCurAbsolutePath("/"));
                dismiss();
                break;
            case R.id.fbp_input_cancel:
                mInputLayout.setVisibility(View.GONE);
                InputMethodUtil.hideSoftInput(mInputLayout);
                break;
            case R.id.fbp_input_ok:
                mInputLayout.setVisibility(View.GONE);
                InputMethodUtil.hideSoftInput(mInputLayout);
                String inputDir = mDirEdit.getText().toString();
                String dir = mCurrentPathView.getCurAbsolutePath("/")+"/"+inputDir;
                if(!TextUtils.isEmpty(inputDir) && FileUtils.createOrExistsDir(dir)){
                    ArrayList<String> sub = new ArrayList<>();
                    if(getSubDirs(dir,sub)) {
                        mCurrentPathView.addCrumb(new LinearBreadcrumb.Crumb(inputDir,""),true);
                        mDirAdapter.setNewData(sub);
                    }
                }else{
                    ToastUtils.showShortToast(getContext(),"创建文件夹【"+inputDir+"】失败");
                }
                break;
        }
    }

    public interface OnPathSelectedListener{
        void onPathSelected(String selectedPath);
    }
}
