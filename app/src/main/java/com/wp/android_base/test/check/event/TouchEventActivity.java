package com.wp.android_base.test.check.event;

import android.view.MotionEvent;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.utils.log.Logger;

/**
 * Created by wangpeng on 2018/11/15.
 * <p>
 * Description:
 */

public class TouchEventActivity extends BaseActivity{

    public static final String TAG = "TouchEventActivity";

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_touch_event;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Logger.e(TAG,"dispatchTouchEvent","event=" + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Logger.e(TAG,"onTouchEvent","event=" + event.getAction());
        return super.onTouchEvent(event);
    }
}
