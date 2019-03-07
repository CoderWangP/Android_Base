package com.wp.android_base.base.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by wangpeng on 2018/1/3.
 *
 * 包相关的工具类
 */

public class PackageUtil {

    /**
     * 版本名称 versionName
     * @param context
     * @return
     */
    public static String getVersionName(Context context){
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 版本号 versionCode
     * @param context
     * @return
     */
    public static int getVersionCode(Context context){
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 包名
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 应用名
     * @param context
     * @return
     */
    public static String getApplicationName(Context context) {
        String appName = null;
        PackageManager packageManager = context.getPackageManager();;
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            appName = (String) packageManager.getApplicationLabel(applicationInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appName;
    }

    /**
     * 获取进程名
     * @param pid ：进程号
     * @return
     */
    public static String getProcessName(int pid){
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = bufferedReader.readLine();
            if(!TextUtils.isEmpty(processName)){
                processName = processName.trim();
            }
            return processName;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    /**
     * 是否是当前应用进程
     * @param context
     * @return
     */
    public static boolean isAppProcess(Context context){
        String packageName = getPackageName(context);
        String processName = getProcessName(android.os.Process.myPid());
        return !TextUtils.isEmpty(processName) && processName.equals(packageName);
    }
}
