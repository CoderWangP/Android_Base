package com.wp.android_base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.demo.CalendarTaibaiActivity;


/**
 * Created by wangpeng on 2018/6/26.
 *
 */

public class LoginActivity extends BaseActivity{

    public static void forward2Login(Context context){
        Intent intent = new Intent(context,LoginActivity.class);
        if(context instanceof Application){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_login;
    }

    public void testClearTop(View v){
        startActivity(new Intent(this, CalendarTaibaiActivity.class));
    }
}
