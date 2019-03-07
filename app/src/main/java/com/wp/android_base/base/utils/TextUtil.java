package com.wp.android_base.base.utils;

import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by wangpeng on 2019/1/9.
 * 文案工具类
 */
public class TextUtil {

    /**
     * 容错 "null" 情况
     * @param str
     * @return
     */
    public static boolean isEmpty(@Nullable CharSequence str) {
        return TextUtils.isEmpty(str) || "null".equalsIgnoreCase(str.toString());
    }
}
