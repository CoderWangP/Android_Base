package com.wp.android_base.base.crypto.aes.def;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.crypto.Cipher;

/**
 * Created by wp on 2019/3/7.
 * <p>
 * Description:AES操作类型，加密，还是解密
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        OPType.ENCRYPT,
        OPType.DECRYPT
})
public @interface OPType {
    int ENCRYPT = Cipher.ENCRYPT_MODE/*加密*/,DECRYPT = Cipher.DECRYPT_MODE/*解密*/;
}
