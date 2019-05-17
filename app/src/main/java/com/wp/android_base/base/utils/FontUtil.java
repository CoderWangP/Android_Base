package com.wp.android_base.base.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Created by wangpeng on 2018/1/16.
 * 字体工具类
 */

public class FontUtil {

    private static HashMap<String, Typeface> fontCache = new HashMap<>();

    /**
     *
     * @param context
     * @param path：注意path是字体文件完整的路径，这里的字体文件放在asset里，从asset里获取
     * @return
     */
    public static Typeface getTypeface(Context context,String path) {
        Typeface typeface = fontCache.get(path);
        if (typeface == null) {
            try {
                //从asset里获取字体
                typeface = Typeface.createFromAsset(context.getAssets(), path);
            } catch (Exception e) {
                return null;
            }
            fontCache.put(path, typeface);
        }
        return typeface;
    }

    public static Typeface getTypeface(Context context) {
        String path = "fonts/ViabtcAlternateBold.ttf";
        return getTypeface(context,path);
    }
}
