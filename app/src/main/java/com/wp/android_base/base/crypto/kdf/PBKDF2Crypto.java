package com.wp.android_base.base.crypto.kdf;

import com.wp.android_base.base.crypto.NumericUtil;

import org.spongycastle.crypto.digests.SHA256Digest;
import org.spongycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.spongycastle.crypto.params.KeyParameter;

/**
 * Created by wp on 2019/3/8.
 * <p>
 * Description:PBKDF2加密
 *
 * PBKDF2算法 {@link https://segmentfault.com/a/1190000004261009}
 *
 * PBKDF2函数定义
 * DK = PBKDF2(PRF, Password, Salt, c, dkLen)
 *
 * PRF：是一个伪随机函数，例如HASH_HMAC函数，它会输出长度为hLen的结果。
 * Password：是用来生成密钥的原文密码。
 * Salt：是一个加密用的盐值。
 * c ：是进行重复计算的次数。
 * dkLen：是期望得到的密钥的长度。
 * DK：是最后产生的密钥
 *
 */

public class PBKDF2Crypto {

    /**
     * 导出秘钥的长度
     */
    private int dkLen;
    /**
     * kdf函数迭代计算次数
     */
    private int c;
    /**
     * 与password混合计算的盐
     */
    private String salt;

/*    public byte[] generateDerivedKey(byte[] password) {
        //prf(伪随机函数)指定hmac摘要算法sha256
        PKCS5S2ParametersGenerator generator = new PKCS5S2ParametersGenerator(new SHA256Digest());
        generator.init(password, NumericUtil.hexToBytes(params.getSalt()), params.getC());
        //导出秘钥长度为256位
        return ((KeyParameter) generator.generateDerivedParameters(256)).getKey();
    }*/
}
