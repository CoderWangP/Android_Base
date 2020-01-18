package com.wp.android_base.base.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
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

    private static final String TAG = "PackageUtil";

    private static String sVersionName = "";
    private static String sVersionCode = "";
    private static String sAppName = "";
    private static String sPackageName = "";

    public static String getVersionName(){
        if (!TextUtil.isEmpty(sVersionName)) {
            return sVersionName;
        }
        try {
            Context context = AppModule.provideContext();
            if(context != null){
                PackageManager manager = context.getPackageManager();
                PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
                sVersionName =  info.versionName;
                return sVersionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取apk文件的版本信息
     * @param apkPath
     * @return
     */
    public static String getVersionName(String apkPath){
        try {
            Context context = AppModule.provideContext();
            if(context != null){
                PackageManager manager = context.getPackageManager();
                PackageInfo info = manager.getPackageArchiveInfo(apkPath,PackageManager.GET_ACTIVITIES);
                if(info != null){
                    return info.versionName;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getVersionCode(){
        if (!TextUtil.isEmpty(sVersionCode)) {
            return sVersionCode;
        }
        try {
            Context context = AppModule.provideContext();
            if(context != null){
                PackageManager manager = context.getPackageManager();
                PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
                sVersionCode = String.valueOf(info.versionCode);
                return sVersionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取apk文件的版本信息
     * @param apkPath
     * @return
     */
    public static String getVersionCode(String apkPath){
        try {
            Context context = AppModule.provideContext();
            if(context != null){
                PackageManager manager = context.getPackageManager();
                PackageInfo info = manager.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
                if (Build.VERSION.SDK_INT >= 28) {
                    return String.valueOf(info.getLongVersionCode());
                }else{
                    return String.valueOf(info.versionCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取应用名称
     * @return
     */
    public static String getAppName() {
        if(!TextUtil.isEmpty(sAppName)){
            return sAppName;
        }
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        String packageName = null;
        try {
            Context context = AppModule.provideContext();
            if(context != null){
                packageManager = context.getPackageManager();
                packageName = context.getPackageName();
                if(packageManager != null){
                    applicationInfo = packageManager.getApplicationInfo(packageName, 0);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        if(applicationInfo != null){
            sAppName = (String) packageManager.getApplicationLabel(applicationInfo);
        }
        return sAppName;
    }

    /**
     * 获取包名
     * @return
     */
    public static String getPackageName() {
        if(!TextUtil.isEmpty(sPackageName)){
            return sPackageName;
        }
        sPackageName = AppModule.provideContext().getPackageName();
        return sPackageName;
    }

    /**
     * 获取apk文件的包名
     * @return
     */
    public static String getPackageName(String apkPath) {
        try {
            Context context = AppModule.provideContext();
            if(context != null){
                PackageManager manager = context.getPackageManager();
                PackageInfo info = manager.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
                return info.packageName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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
     * @return
     */
    public static boolean isAppProcess(){
        String packageName = getPackageName();
        String processName = getProcessName(android.os.Process.myPid());
        return !TextUtils.isEmpty(processName) && processName.equals(packageName);
    }
}
