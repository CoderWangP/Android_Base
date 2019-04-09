package com.wp.android_base.base.crypto.aes.def;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by wp on 2019/3/7.
 * <p>
 * Description:补码填充方式
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({
        PaddingTypeDef.PKCS5_PADDING,
        PaddingTypeDef.NO_PADDING
})
public @interface PaddingTypeDef {
    String PKCS5_PADDING = "PKCS5Padding",NO_PADDING = "NoPadding";
}
