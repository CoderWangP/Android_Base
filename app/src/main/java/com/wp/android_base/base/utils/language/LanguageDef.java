package com.wp.android_base.base.utils.language;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by wangpeng on 2018/12/14.
 * <p>
 * Description:编译时注解，定义语言的字符串表示
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({LanguageDef.SIMPLE_CHINESE,LanguageDef.TRADITIONAL_CHINESE,LanguageDef.ENGLISH,LanguageDef.DEFAULT})
public @interface LanguageDef {
    /**
     * 中文简体，中文繁体，英文，默认(英文)
     */
    String SIMPLE_CHINESE = "zh_Hans_CN",TRADITIONAL_CHINESE = "zh_Hant_HK",ENGLISH = "en_US",DEFAULT = ENGLISH;
}
