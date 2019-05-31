package com.wp.android_base.test.java.clone;

import android.support.design.widget.TabLayout;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.utils.log.Logger;

/**
 * Created by wp on 2019/5/31.
 * <p>
 * Description:
 */

public class TestCloneActivity extends BaseActivity{

    private static final String TAG = "TestCloneActivity";

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_clone;
    }

    @Override
    protected void requestDatas() {
        super.requestDatas();

        Person originPerson = new Person("张三","计算机");
        Person clonePerson = (Person) originPerson.clone();
        Logger.e(TAG,"originPerson == clonePerson ? " + (originPerson == clonePerson),
                "originPerson.name= " + originPerson.name + "，clonePerson.name= " + clonePerson.name,
                "originPerson.course == clonePerson.course ? " + (originPerson.course == clonePerson.course),
                "originPerson.course.name = " + originPerson.course.name + "，clonePerson.course.name" + clonePerson.course.name);

    }
}
