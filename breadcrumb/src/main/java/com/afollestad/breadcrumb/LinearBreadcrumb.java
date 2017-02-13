package com.afollestad.breadcrumb;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aidan Follestad (afollestad)
 */
public class LinearBreadcrumb extends HorizontalScrollView implements View.OnClickListener {

    public static class Crumb implements Serializable {

        public Crumb(String path,String attachMsg) {
            mPath = path;
            mAttachMsg = attachMsg;
        }

        private final String mPath;
        private final String mAttachMsg;
        private int mScrollY;
        private int mScrollOffset;
        public int getScrollY() {
            return mScrollY;
        }

        public int getScrollOffset() {
            return mScrollOffset;
        }

        public void setScrollY(int scrollY) {
            this.mScrollY = scrollY;
        }

        public void setScrollOffset(int scrollOffset) {
            this.mScrollOffset = scrollOffset;
        }

        public String getPath() {
            return mPath;
        }

        public String getTitle() {
            return (!TextUtils.isEmpty(mAttachMsg)) ? mAttachMsg : mPath;
        }

        public String getmAttachMsg() {
            return mAttachMsg;
        }

        @Override
        public boolean equals(Object o) {
            return (o instanceof Crumb) && ((Crumb) o).getPath().equals(getPath());
        }

        @Override
        public String toString() {
            return "Crumb{" +
                    "mAttachMsg='" + mAttachMsg + '\'' +
                    ", mPath='" + mPath + '\'' +
                    ", mScrollY=" + mScrollY +
                    ", mScrollOffset=" + mScrollOffset +
                    '}';
        }
    }
    public interface SelectionCallback {

        void onCrumbSelection(Crumb crumb, String absolutePath, int count, int index);
    }
    public LinearBreadcrumb(Context context) {
        super(context);
        init();
    }

    public LinearBreadcrumb(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LinearBreadcrumb(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private List<Crumb> mCrumbs;

    private List<Crumb> mOldCrumbs;
    private LinearLayout mChildFrame;
    private int mActive;
    private SelectionCallback mCallback;
    private void init() {
        setMinimumHeight((int) getResources().getDimension(R.dimen.breadcrumb_height));
        setClipToPadding(false);
        mCrumbs = new ArrayList<>();
        mChildFrame = new LinearLayout(getContext());
        addView(mChildFrame, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setAlpha(View view, int alpha) {
        if (view instanceof ImageView && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ((ImageView) view).setImageAlpha(alpha);
        } else {
            ViewCompat.setAlpha(view, alpha);
        }
    }

    public void addCrumb(@NonNull Crumb crumb, boolean refreshLayout) {
        LinearLayout view = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.bread_crumb, this, false);
        view.setTag(mCrumbs.size());
        view.setClickable(true);
        view.setFocusable(true);
        view.setOnClickListener(this);

        mChildFrame.addView(view, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mCrumbs.add(crumb);
        if (refreshLayout) {
            mActive = mCrumbs.size() - 1;
            requestLayout();
        }
        invalidateActivatedAll();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //RTL works fine like this
        View child = mChildFrame.getChildAt(mActive);
        if (child != null)
            smoothScrollTo(child.getLeft(), 0);
    }

    public Crumb findCrumb(@NonNull String forDir) {
        for (int i = 0; i < mCrumbs.size(); i++) {
            if (mCrumbs.get(i).getPath().equals(forDir))
                return mCrumbs.get(i);
        }
        return null;
    }

    public void clearCrumbs() {
        try {
            mOldCrumbs = new ArrayList<>(mCrumbs);
            mCrumbs.clear();
            mChildFrame.removeAllViews();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public Crumb getCrumb(int index) {
        return mCrumbs.get(index);
    }

    public void setCallback(SelectionCallback callback) {
        mCallback = callback;
    }

    public boolean setActive(Crumb newActive) {
        mActive = mCrumbs.indexOf(newActive);
        for(int i = size()-1;size()>mActive+1;i--){
            removeCrumbAt(i);
        }

        ((LinearLayout)mChildFrame.getChildAt(mActive)).getChildAt(1).setVisibility(View.GONE);

        boolean success = mActive > -1;
        if (success)
            requestLayout();
        return success;
    }

    private void invalidateActivatedAll() {
        for (int i = 0; i < mCrumbs.size(); i++) {
            Crumb crumb = mCrumbs.get(i);
            invalidateActivated(mChildFrame.getChildAt(i), mActive == mCrumbs.indexOf(crumb),
                    i < mCrumbs.size() - 1).setText(crumb.getTitle());
        }
    }

    public void removeCrumbAt(int index) {
        mCrumbs.remove(index);
        mChildFrame.removeViewAt(index);
    }

    private void updateIndices() {
        for (int i = 0; i < mChildFrame.getChildCount(); i++)
            mChildFrame.getChildAt(i).setTag(i);
    }

    private boolean isValidPath(String path) {
        return path == null;
    }

    public int size() {
        return mCrumbs.size();
    }

    private TextView invalidateActivated(View view, boolean isActive, boolean isShowSeparator) {
        LinearLayout child = (LinearLayout) view;
        if (isShowSeparator)
            child.getChildAt(1).setVisibility(View.VISIBLE);
        return (TextView) child.getChildAt(0);
    }

    public int getActiveIndex() {
        return mActive;
    }

    @Override
    public void onClick(View v) {
        if (mCallback != null) {
            int index = (Integer) v.getTag();
            if (index >= 0 && index < (size()-1)) {
                setActive(mCrumbs.get(index));
                mCallback.onCrumbSelection(mCrumbs.get(index),
                        getAbsolutePath(mCrumbs.get(index), "/"), mCrumbs.size(), index);
            }
        }
    }

    public static class SavedStateWrapper implements Serializable {
        public final int mActive;
        public final List<Crumb> mCrumbs;
        public final int mVisibility;
        public SavedStateWrapper(LinearBreadcrumb view) {
            mActive = view.mActive;
            mCrumbs = view.mCrumbs;
            mVisibility = view.getVisibility();
        }
    }
    public SavedStateWrapper getStateWrapper() {
        return new SavedStateWrapper(this);
    }

    public void restoreFromStateWrapper(SavedStateWrapper mSavedState, Activity context) {
        if (mSavedState != null) {
            mActive = mSavedState.mActive;
            for (Crumb c : mSavedState.mCrumbs) {
                addCrumb(c, false);
            }
            requestLayout();
            setVisibility(mSavedState.mVisibility);
        }
    }

    public String getAbsolutePath(Crumb crumb, @NonNull String separator) {
        StringBuilder builder = new StringBuilder();
        if (size() > 1 && !crumb.equals(mCrumbs.get(0))) {
            List<Crumb> crumbs = mCrumbs.subList(1, size());
            for (Crumb mCrumb : crumbs) {
                builder.append(mCrumb.getPath());
                builder.append(separator);
                if (mCrumb.equals(crumb)) {
                    break;
                }
            }
            String path = builder.toString();
            return path.substring(0, path.length() -1);
        } else {
            return null;
        }
    }

    public String getCurAbsolutePath(@NonNull String separator){
        return getAbsolutePath(getCrumb(mActive),separator);
    }

    public void addRootCrumb() {
        clearCrumbs();
        addCrumb(new Crumb("/","root"), true);
    }

    public void addPath(@NonNull String path,@NonNull String sha, @NonNull String separator) {
        clearCrumbs();
        addCrumb(new Crumb("",""), false);
        String[] paths = path.split(separator);
        Crumb lastCrumb = null;
        for (String splitPath : paths) {
            lastCrumb = new Crumb(splitPath,sha);
            addCrumb(lastCrumb, false);
        }

        if (lastCrumb != null) {
            setActive(lastCrumb);
        }
    }
}