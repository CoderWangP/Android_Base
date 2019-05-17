package com.wp.android_base.base.utils;

import java.util.Collection;
import java.util.Map;

/**
 * Created by wangpeng on 2019/1/8.
 * <p>
 * Description:
 */

public class CheckDataUtil {

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.size() <= 0;
    }

    public static boolean hasData(Collection collection) {
        return collection != null && collection.size() > 0;
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.size() <= 0;
    }

    public static boolean hasData(Map map) {
        return map != null && map.size() > 0;
    }

    public static boolean isEmpty(Object[] data) {
        return data == null || data.length <= 0;
    }

    public static boolean hasData(Object[] data) {
        return data != null && data.length > 0;
    }
}
