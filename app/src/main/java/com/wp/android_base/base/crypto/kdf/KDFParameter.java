package com.wp.android_base.base.crypto.kdf;

/**
 * Created by wp on 2019/3/12.
 * <p>
 * Description:KDF算法参数
 */

public interface KDFParameter {

    /**
     * 推导出秘钥的长度，32字节
     */
    int DK_LEN = 32;
    /**
     * 获取盐
     * @return
     */
    String getSalt();

}
