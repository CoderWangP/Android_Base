package com.wp.android_base.base.utils.log;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by wangpeng on 2018/6/21.
 */

@IntDef({
        LogLevelDef.V,
        LogLevelDef.D,
        LogLevelDef.I,
        LogLevelDef.W,
        LogLevelDef.E,
        LogLevelDef.A,
})
@Retention(RetentionPolicy.SOURCE)
public @interface LogLevelDef {
    int V = 0;
    int D = 1;
    int I = 2;
    int W = 3;
    int E = 4;
    int A = 5;
}

