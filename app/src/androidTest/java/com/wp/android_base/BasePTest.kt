package com.wp.android_base

import android.support.test.runner.AndroidJUnit4
import android.util.Base64
import com.wp.android_base.base.crypto.keystore.KeystoreUtil
import com.wp.android_base.base.utils.log.Logger
import com.wp.android_base.base.utils.sp.Sp
import org.junit.Test
import org.junit.runner.RunWith
import java.nio.charset.StandardCharsets
import java.util.*

/**
 *
 * Created by wp on 2020/6/19.
 *
 * Description:
 *
 */
@RunWith(AndroidJUnit4::class)
class BasePTest {
    val TAG = "BasePTest"
    val data = "1234"
    val iv = "iv"
    @Test
    fun testKeystoreEncrypt(){
        val encrypt = KeystoreUtil.encryptByAES(data.toByteArray(StandardCharsets.UTF_8),iv.toByteArray(StandardCharsets.UTF_8))
        val encryptText = Base64.encodeToString(encrypt,Base64.NO_WRAP)
        Logger.d(TAG,"encrypt = $encryptText")
    }

    @Test
    fun testSp(){
        Sp.putValue("key","value")

        val value = Sp.getValue("key",null)
        Logger.d(TAG,"value = $value")
    }

    @Test
    fun testLog(){
        Logger.d(TAG,"test log")
    }
}