package com.wp.android_base.base.tab.model;

import android.support.annotation.Keep;

/**
 * Created by wp on 2019/4/9.
 * <p>
 * Description:用于初始化tab的bean
 */
@Keep
public class TabBean {


    public TabBean(String title) {
        this.title = title;
    }

    public TabBean(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    private String title;
    private int icon;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
