package com.wp.android_base.model.gituser;

import com.wp.android_base.base.utils.log.Logger;

import java.io.Serializable;

/**
 * Created by wangpeng on 2018/7/23.
 */

public class GitUser implements Serializable{

    private static final String TAG = "GitUser";

    private long count;


    public GitUser() {
        Logger.e(TAG,"这是构造方法");
    }

    static {
        Logger.e(TAG,"这是静态代码块，随着类的加载而执行，只执行一次，并优先与主函数");
    }

    {
        Logger.e(TAG,"这是构造代码块");
    }


    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    private Object readResolve(){
        return count;
    }
}
