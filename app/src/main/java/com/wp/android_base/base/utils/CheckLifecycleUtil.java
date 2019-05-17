package com.wp.android_base.base.utils;

import android.app.Activity;
import android.os.Build;
import android.support.v4.app.Fragment;

/**
 * Created by wangpeng on 2018/6/22.
 */
public class CheckLifecycleUtil {

    public static boolean isAlive(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return activity != null && !activity.isFinishing() && !activity.isDestroyed();
        } else {
            return activity != null && !activity.isFinishing();
        }
    }

    public static boolean isAlive(Fragment fragment) {
        return fragment != null && fragment.isAdded() && isAlive(fragment.getActivity());
    }


    public static boolean isUnAlive(Activity activity) {
        return !isAlive(activity);
    }

    public static boolean isUnAlive(Fragment fragment) {
        return !isAlive(fragment);
    }
}
