package com.wp.android_base.test.base;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.crypto.aes.AESUtil;
import com.wp.android_base.base.crypto.NumericUtil;
import com.wp.android_base.base.crypto.keystore.KeystoreUtil;
import com.wp.android_base.base.utils.log.Logger;

import org.spongycastle.crypto.digests.Blake2bDigest;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Created by wp on 2019/3/7.
 * <p>
 * Description:
 */

public class AESActivity extends BaseActivity{

//    public static final String ORIGIN_DATA = "123456";
    public static final String ORIGIN_DATA = "maximum burst web normal bachelor patrol obvious boat online arena poem liberty";

    private static final String TAG = "AESActivity";

    private TextView mTxOriginData;
    private TextView mTxEncryptData;
    private TextView mTxDecryptData;
    private TextView mTxEncrypt;
    private TextView mTxDecrypt;

    private TextView mTxKeystoreEncryptAES;
    private TextView mTxKeystoreDecryptAES;

    private TextView mTxKeystoreEncryptRSA;
    private TextView mTxKeystoreDecryptRSA;

    byte[] key = NumericUtil.generateRandomBytes(128 / 8);
    byte[] iv = NumericUtil.generateRandomBytes(128 / 8);

    private String ENCRYPT_DATA = null;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_aes;
    }

    @Override
    protected void initializeView() {
        super.initializeView();
        mTxOriginData = findViewById(R.id.tx_origin_data);
        mTxEncryptData = findViewById(R.id.tx_encrypt_data);
        mTxDecryptData = findViewById(R.id.tx_decrypt_data);
        mTxEncrypt = findViewById(R.id.tx_encrypt);
        mTxDecrypt = findViewById(R.id.tx_decrypt);

        mTxOriginData.setText("原数据：\n" + ORIGIN_DATA);

        byte[] value = NumericUtil.hexToBytes("0A");
        Logger.e("value=" + Arrays.toString(value));

        byte[] data = new byte[]{10};

        mTxOriginData.setVisibility(View.VISIBLE);

        mTxKeystoreEncryptAES = findViewById(R.id.tx_keystore_encrypt_aes);
        mTxKeystoreDecryptAES = findViewById(R.id.tx_keystore_decrypt_aes);

        mTxKeystoreEncryptRSA = findViewById(R.id.tx_keystore_encrypt_rsa);
        mTxKeystoreDecryptRSA = findViewById(R.id.tx_keystore_decrypt_rsa);
    }

    private boolean[] bytesToBits(byte[] data) {
        boolean[] bits = new boolean[data.length * 8];
        for (int i = 0; i < data.length; ++i)
            for (int j = 0; j < 8; ++j)
                bits[(i * 8) + j] = (data[i] & (1 << (7 - j))) != 0;
        return bits;
    }

    @Override
    protected void registerListener() {
        super.registerListener();

        mTxEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //采用aes-128
                byte[] cipherText = AESUtil.encryptByCBC(ORIGIN_DATA.getBytes(StandardCharsets.UTF_8),key,iv);
                String text = NumericUtil.bytesToHex(cipherText);
                ENCRYPT_DATA = text;
                Logger.e(TAG,"cipherText = " + text);
                Logger.e(TAG,"cipherText length = " + text.length());
                mTxEncryptData.setText("加密后的数据（转换成了16进制）：\n" + text);
            }
        });

        mTxDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(ENCRYPT_DATA)){
                    return;
                }
                byte[] cipherText = AESUtil.decryptByCBC(NumericUtil.hexToBytes(ENCRYPT_DATA),key,iv);
                String text = new String(cipherText, StandardCharsets.UTF_8);
                Logger.e(TAG,"text = " + text);
                mTxDecryptData.setText("解密后的数据：\n" + text);
            }
        });

        mTxKeystoreEncryptAES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] cipherText = KeystoreUtil.INSTANCE.encryptByAES(ORIGIN_DATA.getBytes(StandardCharsets.UTF_8),iv);
                String text = NumericUtil.bytesToHex(cipherText);
                ENCRYPT_DATA = text;
                Logger.e(TAG,"cipherText = " + text);
                Logger.e(TAG,"cipherText length = " + text.length());
                mTxEncryptData.setText("加密后的数据（转换成了16进制）：\n" + text);
            }
        });

        mTxKeystoreDecryptAES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(ENCRYPT_DATA)){
                    return;
                }
                byte[] cipherText = KeystoreUtil.INSTANCE.decryptByAES(NumericUtil.hexToBytes(ENCRYPT_DATA),iv);
                String text = new String(cipherText, StandardCharsets.UTF_8);
                Logger.e(TAG,"text = " + text);
                mTxDecryptData.setText("解密后的数据：\n" + text);
            }
        });


        mTxKeystoreEncryptRSA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] cipherText = KeystoreUtil.INSTANCE.encryptByRSA(ORIGIN_DATA.getBytes(StandardCharsets.UTF_8));
                String text = NumericUtil.bytesToHex(cipherText);
                ENCRYPT_DATA = text;
                Logger.e(TAG,"cipherText = " + text);
                Logger.e(TAG,"cipherText length = " + text.length());
                mTxEncryptData.setText("加密后的数据（转换成了16进制）：\n" + text);
            }
        });

        mTxKeystoreDecryptRSA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(ENCRYPT_DATA)){
                    return;
                }
                byte[] cipherText = KeystoreUtil.INSTANCE.decryptByRSA(NumericUtil.hexToBytes(ENCRYPT_DATA));
                String text = new String(cipherText, StandardCharsets.UTF_8);
                Logger.e(TAG,"text = " + text);
                mTxDecryptData.setText("解密后的数据：\n" + text);
            }
        });
    }
}
