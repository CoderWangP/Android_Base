package com.wp.android_base.base.image.glide;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;


/**
 * Created by wangpeng on 2018/7/27.
 * <p>
 * Description:图片加载管理类
 */

public class GlideImageLoader {

    /**
     * 加载网络图片
     * @param activity 对应的activity
     * @param url 图片url
     * @param imageView 显示的ImageView
     */
    public static void loadImageWithUrl(Activity activity, String url, ImageView imageView){
        GlideApp.with(activity).load(url)
                .centerCrop()
                .into(imageView);
    }

    /**
     * 加载网络图片
     * @param fragment
     * @param url
     * @param imageView
     */
    public static void loadImageWithUrl(Fragment fragment, String url, ImageView imageView){
        GlideApp.with(fragment).load(url)
                .centerCrop()
                .into(imageView);
    }

    /**
     * 加载本地资源图片
     * @param activity
     * @param resId
     * @param imageView
     */
    public static void loadImageWithRes(Activity activity, int resId, ImageView imageView){
        GlideApp.with(activity).load(resId)
                /*本地资源图片不外部存储*/
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .into(imageView);
    }

    /**
     * 加载本地资源图片
     * @param fragment
     * @param resId
     * @param imageView
     */
    public static void loadImageWithRes(Fragment fragment, int resId, ImageView imageView){
        GlideApp.with(fragment).load(resId)
                /*本地资源图片不外部存储*/
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .into(imageView);
    }

    /**
     * 圆角图片
     * @param activity
     * @param url
     * @param imageView
     */
    public static void loadImageWithRoundCorner(Activity activity, String url, ImageView imageView,int radius){
        MultiTransformation<Bitmap> multiTransformation = new MultiTransformation<>(new CenterCrop(),new RoundedCorners(radius));
        GlideApp.with(activity).load(url)
                .apply(RequestOptions.bitmapTransform(multiTransformation))
                .into(imageView);
    }

    /**
     * 圆角图片
     * @param fragment
     * @param url
     * @param imageView
     */
    public static void loadImageWithRoundCorner(Fragment fragment, String url, ImageView imageView,int radius){
        MultiTransformation<Bitmap> multiTransformation = new MultiTransformation<>(new CenterCrop(),new RoundedCorners(radius));
        GlideApp.with(fragment).load(url)
                .apply(RequestOptions.bitmapTransform(multiTransformation))
                .into(imageView);
    }


    /**
     * 圆角图片
     * @param activity
     * @param resId
     * @param imageView
     * @param radius
     */
    public static void loadImageWithRoundCorner(Activity activity, int resId, ImageView imageView,int radius){
        MultiTransformation<Bitmap> multiTransformation = new MultiTransformation<>(new CenterCrop(),new RoundedCorners(radius));
        GlideApp.with(activity).load(resId)
                 /*本地资源图片不外部存储*/
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .apply(RequestOptions.bitmapTransform(multiTransformation))
                .into(imageView);
    }


    /**
     * 圆角图片
     * @param fragment
     * @param resId
     * @param imageView
     * @param radius
     */
    public static void loadImageWithRoundCorner(Fragment fragment, int resId, ImageView imageView,int radius){
        MultiTransformation<Bitmap> multiTransformation = new MultiTransformation<>(new CenterCrop(),new RoundedCorners(radius));
        GlideApp.with(fragment).load(resId)
                /*本地资源图片不外部存储*/
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .apply(RequestOptions.bitmapTransform(multiTransformation))
                .into(imageView);
    }


    /**
     * 圆形图片
     * @param activity
     * @param url
     * @param imageView
     */
    public static void loadImageWithCircle(Activity activity, String url, ImageView imageView){
        MultiTransformation<Bitmap> multiTransformation = new MultiTransformation<>(new CenterCrop(),new CircleCrop());
        GlideApp.with(activity).load(url)
                .apply(RequestOptions.bitmapTransform(multiTransformation))
                .into(imageView);
    }

    /**
     * 圆形图片
     * @param fragment
     * @param url
     * @param imageView
     */
    public static void loadImageWithCircle(Fragment fragment, String url, ImageView imageView){
        MultiTransformation<Bitmap> multiTransformation = new MultiTransformation<>(new CenterCrop(),new CircleCrop());
        GlideApp.with(fragment).load(url)
                .apply(RequestOptions.bitmapTransform(multiTransformation))
                .into(imageView);
    }

    /**
     * 圆形图片
     * @param activity
     * @param resId
     * @param imageView
     */
    public static void loadImageWithCircle(Activity activity, int resId, ImageView imageView){
        MultiTransformation<Bitmap> multiTransformation = new MultiTransformation<>(new CenterCrop(),new CircleCrop());
        GlideApp.with(activity).load(resId)
                /*本地资源图片不外部存储*/
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .apply(RequestOptions.bitmapTransform(multiTransformation))
                .into(imageView);
    }

    /**
     * 圆形图片
     * @param fragment
     * @param resId
     * @param imageView
     */
    public static void loadImageWithCircle(Fragment fragment, int resId, ImageView imageView){
        MultiTransformation<Bitmap> multiTransformation = new MultiTransformation<>(new CenterCrop(),new CircleCrop());
        GlideApp.with(fragment).load(resId)
                /*本地资源图片不外部存储*/
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .apply(RequestOptions.bitmapTransform(multiTransformation))
                .into(imageView);
    }

}
