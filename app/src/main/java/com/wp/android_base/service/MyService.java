package com.wp.android_base.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by wangpeng on 2018/6/29.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyService extends JobService{
    @Override
    public boolean onStartJob(JobParameters params) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
