package com.wp.android_base.base.utils;

import android.os.Environment;

/**
 * Created by wangpeng on 2018/12/6.
 * <p>
 * Description:文件工具类
 */

public class FileUtil {

    /**
     * 检测外部存储读取、写入功能是否可用
     * @return
     */
    public static boolean isExternalStorageWritable(){
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 检测外部存储读取功能是否可用
     * @return
     */
    public static boolean isExternalStorageReadable(){
        return isExternalStorageWritable()
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState());
    }
}
