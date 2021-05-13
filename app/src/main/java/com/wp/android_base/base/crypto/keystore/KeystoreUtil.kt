package com.wp.android_base.base.crypto.keystore

import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.wp.android_base.base.utils.AppModule
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.security.auth.x500.X500Principal

/**
 *
 * Created by wp on 2020/6/16.
 *
 * Description:利用Android Keystore系统,防止从应用进程和Android设备中提取秘钥材料
 *
 * @see <"https://developer.android.com/training/articles/keystore">
 *
 */
object KeystoreUtil {

    const val TAG = "KeystoreUtil"
    const val ALIAS_AES = "androidBase_AES"
    const val ALIAS_RSA = "androidBase_RSA"
    const val PROVIDER = "AndroidKeyStore"

    /*
     * <p><h3>Example: AES key for encryption/decryption in GCM mode</h3>
     * The following example illustrates how to generate an AES key in the Android KeyStore system under
     * alias {@code key2} authorized to be used only for encryption/decryption in GCM mode with no
     * padding.
     *
     */
    private fun init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /**
             * AES API23后支持，对称加密  非对称加密用KeyPairGenerator去生成
             */
            val kg: KeyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, PROVIDER)
            val parameterSpec = KeyGenParameterSpec.Builder(
                    ALIAS_AES,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            ).run {
                setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                setRandomizedEncryptionRequired(false)
                build()
            }
            kg.init(parameterSpec)
            kg.generateKey()
        } else {
            //RSA api23以下，用RSA非对称加解密
            val keyAlgorithmRSA = "RSA"
            val keyPairGeneratorSpec = KeyPairGeneratorSpec.Builder(AppModule.provideContext())
                    .setAlias(ALIAS_RSA)
                    /*.setSubject(X500Principal("CN=Your Company,O=Your Organization,C=Your Country"))*/
                    .setSubject(X500Principal("CN=$ALIAS_RSA"))
                    .setSerialNumber(BigInteger.ONE)
                    // Jan 1 1970 see[Key]
                    .setStartDate(Date(0L))
                    // Jan 1 2048
                    .setEndDate(Date(2461449600000L))
                    .build()
            val keyPairGenerator = KeyPairGenerator.getInstance(keyAlgorithmRSA, PROVIDER)
            keyPairGenerator.initialize(keyPairGeneratorSpec)
            keyPairGenerator.generateKeyPair()
        }
    }

    /**
     * 初始化AES key  api 23 AndroidKeystore才支持
     */
    private fun initAESKey(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /**
             * AES API23后支持，对称加密  非对称加密用KeyPairGenerator去生成
             */
            val kg: KeyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, PROVIDER)
            val parameterSpec = KeyGenParameterSpec.Builder(
                    ALIAS_AES,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            ).run {
                setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                setRandomizedEncryptionRequired(false)
                build()
            }
            kg.init(parameterSpec)
            kg.generateKey()
        }
    }

    /**
     * 初始化RSA key
     */
    private fun initRSAKey(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val kpg: KeyPairGenerator = KeyPairGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_RSA, PROVIDER
            )
            val parameterSpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
                    ALIAS_RSA,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            ).run {
                setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                setRandomizedEncryptionRequired(false)
                build()
            }
            kpg.initialize(parameterSpec)
            kpg.generateKeyPair()
        }else{
            //RSA api23以下，用RSA非对称加解密
            val keyAlgorithmRSA = "RSA"
            val keyPairGeneratorSpec = KeyPairGeneratorSpec.Builder(AppModule.provideContext())
                    .setAlias(ALIAS_RSA)
                    /*.setSubject(X500Principal("CN=Your Company,O=Your Organization,C=Your Country"))*/
                    .setSubject(X500Principal("CN=$ALIAS_RSA"))
                    .setSerialNumber(BigInteger.ONE)
                    // Jan 1 1970 see[Key]
                    .setStartDate(Date(0L))
                    // Jan 1 2048
                    .setEndDate(Date(2461449600000L))
                    .build()
            val keyPairGenerator = KeyPairGenerator.getInstance(keyAlgorithmRSA, PROVIDER)
            keyPairGenerator.initialize(keyPairGeneratorSpec)
            keyPairGenerator.generateKeyPair()
        }
    }


    /**
     * AES 加密key
     */
    private fun getSecretKey():SecretKey?{
        val ks: KeyStore = KeyStore.getInstance(PROVIDER).apply { load(null) }
        if (!ks.containsAlias(ALIAS_AES)) {
            initAESKey()
        }
        return ks.getKey(ALIAS_AES, null) as SecretKey?
    }

    /**
     * RSA 私钥
     */
    private fun getPrivateKey():PrivateKey?{
        val ks: KeyStore = KeyStore.getInstance(PROVIDER).apply { load(null) }
        if (!ks.containsAlias(ALIAS_RSA)) {
            initRSAKey()
        }
        return ks.getKey(ALIAS_RSA, null) as PrivateKey?
    }

    /**
     * RSA 公钥
     */
    private fun getPublicKey():PublicKey?{
        val ks: KeyStore = KeyStore.getInstance(PROVIDER).apply { load(null) }
        if (!ks.containsAlias(ALIAS_RSA)) {
            initRSAKey()
        }
        return ks.getCertificate(ALIAS_RSA)?.publicKey
    }

    /**
     * AES加密
     */
    fun encryptByAES(data: ByteArray, iv: ByteArray): ByteArray? {
        val transformationDesc = "AES/CBC/PKCS7Padding"
        val ivParameterSpec = IvParameterSpec(iv)
        val cipher = Cipher.getInstance(transformationDesc)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(), ivParameterSpec)
        return cipher.doFinal(data)
    }

    /**
     * AES解密
     */
    fun decryptByAES(data: ByteArray, iv: ByteArray): ByteArray? {
        val transformationDesc = "AES/CBC/PKCS7Padding"
        val ivParameterSpec = IvParameterSpec(iv)
        val cipher = Cipher.getInstance(transformationDesc)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), ivParameterSpec)
        return cipher.doFinal(data)
    }


    /**
     * 公钥加密
     */
    fun encryptByRSA(data: ByteArray):ByteArray?{
        val transformationDesc = "RSA/ECB/PKCS1Padding"
        val cipher = Cipher.getInstance(transformationDesc)
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey())
        return cipher.doFinal(data)
    }

    /**
     * 私钥解密
     */
    fun decryptByRSA(data: ByteArray):ByteArray?{
        val transformationDesc = "RSA/ECB/PKCS1Padding"
        val cipher = Cipher.getInstance(transformationDesc)
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey())
        return cipher.doFinal(data)
    }
}