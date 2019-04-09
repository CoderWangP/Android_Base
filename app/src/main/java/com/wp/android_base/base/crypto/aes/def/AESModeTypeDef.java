package com.wp.android_base.base.crypto.aes.def;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by wp on 2019/3/7.
 * <p>
 * Description:AES算法模式类型，如cbc,ctr
 */

@Retention(RetentionPolicy.SOURCE)
@StringDef({
        AESModeTypeDef.CBC,
        AESModeTypeDef.CTR
})
public @interface AESModeTypeDef {
    String CBC = "CBC",CTR = "CTR";
}
