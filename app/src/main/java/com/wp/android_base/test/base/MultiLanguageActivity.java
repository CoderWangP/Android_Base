package com.wp.android_base.test.base;

import android.view.View;
import android.widget.TextView;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.utils.AppModule;
import com.wp.android_base.base.utils.language.LanguageDef;
import com.wp.android_base.base.utils.language.LanguageUtil;

/**
 * Created by wangpeng on 2018/12/17.
 * <p>
 * Description:多语言测试
 */

public class MultiLanguageActivity extends BaseActivity{

    private TextView mTxCurrentLanguage;

    private int mIndex = 0;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_multi_language;
    }

    @Override
    protected void initializeView() {
        super.initializeView();
        mTxCurrentLanguage = findViewById(R.id.tx_current_language);
    }

    @Override
    protected void registerListener() {
        super.registerListener();
        mTxCurrentLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIndex++;
                int current = mIndex % 3;
                String language = null;
                switch (current){
                    case 0:
                        language = LanguageDef.SIMPLE_CHINESE;
                        break;
                    case 1:
                        language = LanguageDef.TRADITIONAL_CHINESE;
                        break;
                    case 2:
                        language = LanguageDef.ENGLISH;
                        break;
                }
                LanguageUtil.saveCurrentAppLang(language);
                recreate();
            }
        });
    }

    @Override
    protected void requestDatas() {
        super.requestDatas();
        setLanguage();
    }

    private void setLanguage(){
        String language = null;
        switch (LanguageUtil.getCurrentAppLangByStr(AppModule.provideContext())){
            case LanguageDef.SIMPLE_CHINESE:
                mIndex = 0;
                language = "中文简体";
                break;
            case LanguageDef.TRADITIONAL_CHINESE:
                mIndex = 1;
                language = "中文繁體";
                break;
            case LanguageDef.ENGLISH:
                mIndex = 2;
                language = "English";
                break;
        }
        mTxCurrentLanguage.setText(language);
    }
}
