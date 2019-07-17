package com.wp.android_base.test.base;

import android.content.Intent;
import android.view.View;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.tab.MainTabActivity;
import com.wp.android_base.base.update.DownloadApkService;
import com.wp.android_base.base.utils.FileUtil;
import com.wp.android_base.test.base.dialog.DialogTestActivity;

/**
 * Created by wangpeng on 2018/12/7.
 * <p>
 * Description:项目基本常用的一些框架，组件，工具类
 */

public class ProjectsBaseActivity extends BaseActivity{

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_projects_base;
    }

    public void httpApiTest(View v) {
        Intent intent = new Intent(this,HttpApiTestActivity.class);
        startActivity(intent);
    }

    public void logTest(View v){
        Intent intent = new Intent(this,LogTestActivity.class);
        startActivity(intent);
    }

    public void dialog(View view) {
        Intent intent = new Intent(this, DialogTestActivity.class);
        startActivity(intent);
    }

    public void glide(View view) {
        Intent intent = new Intent(this, GlideTestActivity.class);
        startActivity(intent);
    }

    public void sp(View view) {
        Intent intent = new Intent(this, SpTestActivity.class);
        startActivity(intent);
    }


    public void update(View view) {

        if(!FileUtil.isExternalStorageWritable() || !DownloadApkService.canDownloadState()){
            //外部存储不可用，或者DownloadManager组件不可用
            return;
        }
/*        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        startActivity(intent);*/
        DownloadApkService.browserUpdate(this,"http://119.23.26.158:8001/viabtc_debug.apk");
    }

    public void multiLanguage(View view) {
        Intent intent = new Intent(this, MultiLanguageActivity.class);
        startActivity(intent);
    }

    public void span(View v){
        Intent intent = new Intent(this,SpanTestActivity.class);
        startActivity(intent);
    }

    public void aes(View view) {
        startActivity(new Intent(this, AESActivity.class));
    }

    public void mainTab(View v){
        startActivity(new Intent(this, MainTabActivity.class));
    }

    public void toast(View v){
        startActivity(new Intent(this,ToastActivity.class));
    }
}
