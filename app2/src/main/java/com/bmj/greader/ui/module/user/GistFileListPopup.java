package com.bmj.greader.ui.module.user;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bmj.greader.R;
import com.bmj.greader.common.util.StringUtil;
import com.bmj.greader.data.model.gist.GistFile;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by Administrator on 2016/12/20 0020.
 */

public class GistFileListPopup extends BasePopupWindow{

    private ListView mFileList;
    private ArrayList<GistFile> mGistFiles;
    private Context mContext;
    private Activity mActivity;

    public GistFileListPopup(Activity activity, Context context, ArrayList<GistFile> gistFiles) {
        super(activity);
        mActivity = activity;
        mGistFiles = gistFiles;
        mContext = context;
        mFileList = (ListView) findViewById(R.id.pfl_file_list);
        initViews();
    }

    private void initViews(){
        mFileList.setAdapter(new ArrayAdapter<>(mContext,
                android.R.layout.simple_expandable_list_item_1,getData()));

        mFileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FileReadPopup fileReadPopup = new FileReadPopup(mActivity,mGistFiles.get(i));
                fileReadPopup.showPopupWindow();
            }
        });
    }

    private List<String> getData(){
        List<String> files = new ArrayList<>();
        for(GistFile file:mGistFiles){
            files.add(StringUtil.replaceAllBlank(file.getFilename()));
        }
        return files;
    }

    @Override
    protected Animation initShowAnimation() {
        AnimationSet set=new AnimationSet(false);
        Animation shakeAnima=new RotateAnimation(0,15,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        shakeAnima.setInterpolator(new CycleInterpolator(3));
        shakeAnima.setDuration(240);
        set.addAnimation(getDefaultAlphaAnimation());
        set.addAnimation(shakeAnima);
        return set;
    }

    @Override
    protected Animator initShowAnimator() {
        /*final int[] myLocation = new int[2];
        final int[] anchorLocation = new int[2];
        contentView.getLocationOnScreen(myLocation);
        anchor.getLocationOnScreen(anchorLocation);
        final int cx = anchorLocation[0] - myLocation[0] + anchor.getWidth()/2;
        final int cy = anchorLocation[1] - myLocation[1] + anchor.getHeight()/2;

        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        final int dx = Math.max(cx, contentView.getMeasuredWidth() - cx);
        final int dy = Math.max(cy, contentView.getMeasuredHeight() - cy);
        final float finalRadius = (float) Math.hypot(dx, dy);
        Animator animator = ViewAnimationUtils.createCircularReveal(contentView, cx, cy, 0f, finalRadius);
        animator.setDuration(500);*/
        return super.initShowAnimator();
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.popup_file_list);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.pfl_cardview);
    }
}
