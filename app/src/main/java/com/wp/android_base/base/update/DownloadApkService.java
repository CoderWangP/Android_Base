package com.wp.android_base.base.update;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.wp.android_base.base.utils.AppModule;
import com.wp.android_base.base.utils.FileUtil;
import com.wp.android_base.base.utils.PackageUtil;
import com.wp.android_base.base.utils.log.Logger;

import java.io.File;

/**
 * Created by wangpeng on 2018/6/12.
 *
 * android 6.0 ：动态权限配置，这时可以直接在包应用空间(通过Context获取的文件路径)内创建存储文件，不需要另外去动态申请存储权限
 * android 7.0 ：需要适配，StrictModeApi，访问下载的文件需要获取临时权限，通过ContentProvider来访问
 * android 8.0 ：非应用市场下载的apk,需要安装权限
 * <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
 */

public class DownloadApkService extends Service{

    public static final String DOWNLOAD_URL = "downloadUrl";
    private String APK_NAME = PackageUtil.getApplicationName(this) + ".apk";

    private BroadcastReceiver mDownloadReceiver;
    private DownloadManager mDownloadManager;

    private File mDownloadFile;

    private long mRequestDownloadId = -1l;


    public static void forward2DownloadApk(Context context,String downloadUrl){
        if(!FileUtil.isExternalStorageWritable() || !canDownloadState()){
            //外部存储不可用，或者DownloadManager组件不可用
            browserUpdate(context,downloadUrl);
        }else{
            Intent intent = new Intent(context,DownloadApkService.class);
            intent.putExtra(DOWNLOAD_URL,downloadUrl);
            context.startService(intent);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDownloadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadCompleteId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1l);
                    if (downloadCompleteId == mRequestDownloadId) {
                        //当前下载完成是apk
                        checkDownloadStatus(context, downloadCompleteId);
                    }
                }
            }
        };
        //注册下载完成广播
        registerReceiver(mDownloadReceiver,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //进程退出，可能导致service重启，intent为空
        if(intent != null) {
            String downloadUrl = intent.getStringExtra(DOWNLOAD_URL);
            if (!TextUtils.isEmpty(downloadUrl)) {
                mRequestDownloadId = downloadApk(downloadUrl);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private long downloadApk(String downloadUrl) {
        try{
            clearApk();
            Uri uri = Uri.parse(downloadUrl);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setMimeType("application/vnd.android.package-archive");
            mDownloadFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),APK_NAME);
            Logger.e("download_path",mDownloadFile.getAbsolutePath());
            request.setDestinationUri(Uri.fromFile(mDownloadFile));
//            request.setDestinationInExternalFilesDir()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setTitle(APK_NAME);
            request.setVisibleInDownloadsUi(true);
            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            return downloadManager.enqueue(request);
        }catch (Exception e){

        }
        return mRequestDownloadId;
    }

    private void checkDownloadStatus(Context context, long downloadId) {
        mDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = null;
        try {
            cursor = mDownloadManager.query(query);
            if (cursor.moveToFirst()) {
                int status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                switch (status) {
                    case DownloadManager.STATUS_SUCCESSFUL:
                        String downloadUrl = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI));
                        installApk(context,downloadUrl);
                        break;
                    case DownloadManager.STATUS_FAILED:
                        break;
                    case DownloadManager.STATUS_RUNNING:
                        break;
                }
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void installApk(Context context, String downloadUrl) {
        if (TextUtils.isEmpty(downloadUrl)) {
            return;
        }
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.addCategory(Intent.CATEGORY_DEFAULT);
        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.e("downloadUrl",downloadUrl);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            //android 7.0 StrictModeApi，访问下载的文件需要获取临时权限，通过ContentProvider来访问
            Uri uri = FileProvider.getUriForFile(context,"com.wp.android_base.fileprovider",mDownloadFile);
            Logger.e("uri.getEncodedPath()",uri.getEncodedPath());
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            install.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            install.setDataAndType(uri,"application/vnd.android.package-archive");
        }else{
            install.setDataAndType(Uri.parse(downloadUrl), "application/vnd.android.package-archive");
        }
        context.startActivity(install);
    }

    /**
     * 删除之前的apk
     * @return
     */
    private void clearApk() {
        File apkFile = new File(this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),APK_NAME);
        if (apkFile.exists()) {
            apkFile.delete();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mDownloadReceiver);
    }

    /**
     * DownloadManager组件是否可用
     * @return
     */
    public static boolean canDownloadState() {
        try {
            int state = AppModule.provideApplication().getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");
            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 浏览器下载apk
     * @param context
     * @param url
     */
    public static void browserUpdate(Context context,String url){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri apkUrl = Uri.parse(url);
        intent.setData(apkUrl);
        context.startActivity(intent);
    }
}
