package com.wp.android_base.base.utils.language;

import android.content.Context;

import com.wp.android_base.base.utils.Sp;

import java.util.Locale;

/**
 * Created by wangpeng on 2018/12/14.
 * <p>
 * Description:多语言适配工具类
 */

public class LanguageUtil {

    private static final String SP_LANGUAGE_NAME = "sp4Language";
    private static final String KEY_FOR_LANGUAGE = "key4Language";


    /**
     * 返回当前语言，以Locale形式
     * @param context
     * @return
     */
    public static Locale getCurrentAppLangByLocale(Context context) {
        Locale locale = null;
        String language = getSavedAppLanguage(context);
        switch (language) {
            case LanguageDef.SIMPLE_CHINESE:
                locale = Locale.SIMPLIFIED_CHINESE;
                break;
            case LanguageDef.TRADITIONAL_CHINESE:
                locale = Locale.TRADITIONAL_CHINESE;
                break;
            case LanguageDef.ENGLISH:
                locale = Locale.ENGLISH;
                break;
        }
        return locale;
    }

    /**
     * 返回当前语言，以Str形式
     * @param context
     * @return
     */
    public static @LanguageDef String getCurrentAppLangByStr(Context context) {
        return getSavedAppLanguage(context);
    }

    /**
     * app内改语言时，存储当前使用的语言
     * @param context
     * @param lang
     */
    public static void saveCurrentAppLang(Context context,@LanguageDef String lang){
        Sp.from(context.getApplicationContext(),SP_LANGUAGE_NAME).writer().putString(KEY_FOR_LANGUAGE,lang).apply();
    }

    /**
     * 如果没有找到存储的Language，表示是首次进入，这时需要根据用户的手机环境来判断选择使用哪种语言
     * @param context
     * @return
     */
    private static @LanguageDef String getSavedAppLanguage(Context context) {
        String language = Sp.from(context.getApplicationContext(),SP_LANGUAGE_NAME).read().getString(KEY_FOR_LANGUAGE,"none");
        if("none".equals(language)){
            Locale locale = Locale.getDefault();
            String lang = locale.getLanguage();
            String country = locale.getCountry();
            //默认英文
            if("zh".equalsIgnoreCase(lang)){
                if("TW".equalsIgnoreCase(country) || "HK".equalsIgnoreCase(country)){
                    language = LanguageDef.TRADITIONAL_CHINESE;
                }else{
                    language = LanguageDef.SIMPLE_CHINESE;
                }
            }else{
                language = LanguageDef.ENGLISH;
            }
        }
        return language;
    }
}
