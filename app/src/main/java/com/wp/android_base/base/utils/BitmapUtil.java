package com.wp.android_base.base.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by wp on 2019/4/17.
 * <p>
 * Description:处理Bitmap相关的工具类
 */

public class BitmapUtil {


    /**
     * 从资源文件中decode位图，按照需要的宽高
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // 首先只解析图片资源的边界
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        //计算缩放值（图片的像素压缩）
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // 用计算出来的缩放值解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 计算缩放的比例
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            /**
             * 计算最大的inSampleSize值，压缩的比例，这个值必须是2的幂的值，如果不是，解码器最终会向下
             * 舍如到最接近2的幂的值
             *
             * 计算时，保证缩放后的height，width必须大于需要的宽，高
             */
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
