package com.bmj.greader.common.wrapper;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import com.bmj.greader.R;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;

import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 2016/11/10 0010.
 */
public class ImageLoader {
    /**
     * Load image from source and set it into the imageView.
     * @param context context.
     * @param source could be Uri/String/File/ResourceId.
     * @param view the imageView.
     * 可以用activity fragment context，支持简单的动画，支持加载中和错误时的默认图片
     * 支持图片的转换 支持监听
     */
    public static void load(Activity context, Object source, ImageView view){
        Glide.with(context)
                .load(source)
                .centerCrop()
                .into(view);
    }

    public static void load(Context context, Object source, ImageView view){
        Glide.with(context)
                .load(source)
                .centerCrop()
                .into(view);
    }

    public static void load(Object source ,ImageView view){
        Glide.with(view.getContext())
                .load(source)
                .centerCrop()
                .into(view);
    }

    public static void loadWithCircle(Activity context, Object source, ImageView view){
        Glide.with(context)
                .load(source)
                .bitmapTransform(new CropCircleTransformation(context))
                .placeholder(R.drawable.ic_github)
                .into(view);
    }

    public static void loadWithCircle(Context context, Object source, ImageView view){
        Glide.with(context)
                .load(source)
                .bitmapTransform(new CropCircleTransformation(context))
                .placeholder(R.drawable.ic_github)
                .into(view);
    }

    public static void loadWithCircle(Object source ,ImageView view){
        Glide.with(view.getContext())
                .load(source)
                .bitmapTransform(new CropCircleTransformation(view.getContext()))
                .placeholder(R.drawable.ic_github)
                .into(view);
    }

    public static void loadGif(Activity activity,Object source, final ImageView view, final OnResourceReady ready){
        Glide.with(activity)
                .load(source)
                .crossFade()
               // .asGif()
                .listener(new RequestListener<Object, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Object model, Target<GlideDrawable> target, boolean isFirstResource) {
                        ready.onError();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Object model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                       // Toast.makeText(view.getContext(),"图片加载完成",Toast.LENGTH_SHORT).show();
                        ready.onResourceReady();
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                //.error( R.drawable.loaderror2 )
                .into(view);
    }

    public interface OnResourceReady{
        void onResourceReady();
        void onError();
    }

    public static Drawable load2Drawable(Context context, Object source){
        try {
            return Glide.with(context)
                    .load(source)
                    .placeholder(R.drawable.ic_github)
                    .fitCenter()
                    .into(200,200)
                    .get();
        }catch (ExecutionException e){
            AppLog.e(e);
            return ContextCompat.getDrawable(context,R.drawable.ic_github);
        }catch(InterruptedException e){
            AppLog.e(e);
            return ContextCompat.getDrawable(context,R.drawable.ic_github);
        }
    }
}
