package com.wp.android_base.test.check.event;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.wp.android_base.base.utils.ScreenUtil;
import com.wp.android_base.base.utils.log.Logger;


/**
 * Created by wangpeng on 2018/11/15.
 * <p>
 * Description:
 */

public class TouchViewGroup extends LinearLayout{

    public static final String TAG = "TouchViewGroup";

    public TouchViewGroup(Context context) {
        super(context);
    }

    public TouchViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TouchViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Logger.e(TAG,"dispatchTouchEvent","event=" + ev.getAction());
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Logger.e(TAG,"onInterceptTouchEvent","event=" + ev.getAction());
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if(ev.getRawX() > ScreenUtil.getScreenWidth() / 2){
                    return false;
                }else{
                    return true;
                }
//                return true;
            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Logger.e(TAG,"onTouchEvent","event=" + event.getAction());
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
/*                if(event.getRawX() > ScreenUtil.getScreenWidth() / 2){
                    // 重新dispatch一次down事件，使得列表可以继续滚动
                    int oldAction = event.getAction();
                    event.setAction(MotionEvent.ACTION_DOWN);
                    dispatchTouchEvent(event);
                    event.setAction(oldAction);
                }*/
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}
