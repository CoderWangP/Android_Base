package com.wp.android_base.base.crypto.aes;

import com.wp.android_base.base.crypto.aes.def.AESModeTypeDef;
import com.wp.android_base.base.crypto.aes.def.OPType;
import com.wp.android_base.base.crypto.aes.def.PaddingTypeDef;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by wp on 2019/3/7.
 * <p>
 * Description:用于AES加解密
 *
 *
 *  AES加密 https://segmentfault.com/a/1190000011156401
 *  https://segmentfault.com/a/1190000004261009
 *
 *  "AES/CBC/PKCS5Padding" :"算法/模式/补码方式"
 *
 *  cbc模式：串行
 *  ctr模式：并行
 *
 *  AES加密的标准模式下，分组的大小为128位，key的大小可以为128，192，256
 *
 *  初始向量（Initialization Vector）:CBC,CTR等模式都需要传一个初始向量，这个初始销量的大小与aes加密的分组大小一样
 *  aes加密的分组大小为128位，故初始向量的大小也为128位
 *
 *
 *  注意：加密后的字节是不能强转成字符串的，可以将二进制数据转换成16进制
 *
 *
 *
 */

public class AESUtil {

    /**
     * 以ctr模式加密数据
     * @param data
     * @param key
     * @param iv
     * @return
     */
    public static byte[] encryptByCTR(byte[] data, byte[] key, byte[] iv) {
        return doFinal(data, key, iv, OPType.ENCRYPT, AESModeTypeDef.CTR, PaddingTypeDef.PKCS5_PADDING);
    }

    /**
     * 以ctr模式解密数据
     * @param cipherText
     * @param key
     * @param iv
     * @return
     */
    public static byte[] decryptByCTR(byte[] cipherText, byte[] key, byte[] iv) {
        return doFinal(cipherText, key, iv, OPType.DECRYPT, AESModeTypeDef.CTR, PaddingTypeDef.PKCS5_PADDING);
    }

    public static byte[] encryptByCBC(byte[] data, byte[] key, byte[] iv) {
        return doFinal(data, key, iv, OPType.ENCRYPT, AESModeTypeDef.CBC, PaddingTypeDef.PKCS5_PADDING);
    }

    public static byte[] decryptByCBC(byte[] cipherText, byte[] key, byte[] iv) {
        return doFinal(cipherText, key, iv, OPType.DECRYPT, AESModeTypeDef.CBC, PaddingTypeDef.PKCS5_PADDING);
    }

    public static byte[] encryptByCTRNoPadding(byte[] data, byte[] key, byte[] iv) {
        return doFinal(data, key, iv, OPType.ENCRYPT, AESModeTypeDef.CTR, PaddingTypeDef.NO_PADDING);
    }

    public static byte[] decryptByCTRNoPadding(byte[] ciphertext, byte[] key, byte[] iv) {
        return doFinal(ciphertext, key, iv, OPType.DECRYPT, AESModeTypeDef.CTR, PaddingTypeDef.NO_PADDING);
    }

    public static byte[] encryptByCBCNoPadding(byte[] data, byte[] key, byte[] iv) {
        return doFinal(data, key, iv, OPType.ENCRYPT, AESModeTypeDef.CBC, PaddingTypeDef.NO_PADDING);
    }

    public static byte[] decryptByCBCNoPadding(byte[] ciphertext, byte[] key, byte[] iv) {
        return doFinal(ciphertext, key, iv,OPType.DECRYPT,AESModeTypeDef.CBC, PaddingTypeDef.NO_PADDING);
    }


    /**
     * 加密，解密
     * 加密后的字节是不能强转成字符串的，可以将二进制数据转换成16进制
     * @param data:需要加密，解密的元数据
     * @param key：AES加，解密的key
     * @param iv：初始向量
     * @param opType：操作类型，加密，解密
     * @param aesModeType：算法模式
     * @param paddingType：补码模式
     * @return
     */
    private static byte[] doFinal(byte[]data,byte[] key,byte[] iv,@OPType int opType, @AESModeTypeDef String aesModeType, @PaddingTypeDef String paddingType){
        String transformationDesc = String.format("AES/%S/%S",aesModeType,paddingType);
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
            Cipher cipher = Cipher.getInstance(transformationDesc);
            cipher.init(opType,secretKeySpec,ivParameterSpec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
