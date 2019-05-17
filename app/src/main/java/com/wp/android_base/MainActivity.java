package com.wp.android_base;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.stetho.common.LogUtil;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.tab.model.TabBean;
import com.wp.android_base.base.utils.BigDecimalUtil;
import com.wp.android_base.base.utils.PackageUtil;
import com.wp.android_base.base.utils.log.LogLevelDef;
import com.wp.android_base.base.utils.log.Logger;
import com.wp.android_base.demo.CalendarTaibaiActivity;
import com.wp.android_base.demo.EditTextFocusActivity;
import com.wp.android_base.miner.MiningActivity;
import com.wp.android_base.test.ConstraintLayoutActivity;
import com.wp.android_base.test.base.AESActivity;
import com.wp.android_base.test.base.ProjectsBaseActivity;
import com.wp.android_base.test.RecyclerViewTestActivity;
import com.wp.android_base.test.TestFragmentActivity;
import com.wp.android_base.test.banner.BannerActivity;
import com.wp.android_base.test.WidgetTestActivity;
import com.wp.android_base.test.banner.HorSWithViewPagerActivity;
import com.wp.android_base.test.banner.PagerMaginActivity;
import com.wp.android_base.test.banner.RvBannerActivity;
import com.wp.android_base.test.banner.RvWithViewPagerActivity;
import com.wp.android_base.test.check.LifecycleTestActivity;
import com.wp.android_base.test.check.event.TouchEventActivity;
import com.wp.android_base.test.rx.RxTestActivity;
import com.wp.android_base.test.tab.TabWidgetActivity;
import com.wp.android_base.base.utils.RSAUtils;
import com.wp.android_base.base.utils.ScreenUtil;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Arrays;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 *
 */
public class MainActivity extends BaseActivity {

    public static final String TAG = "MainActivity";

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private TextView mTxGlide;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initializeView() {
        super.initializeView();
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setProgressViewOffset(true, ScreenUtil.dp2px(140), ScreenUtil.dp2px(160));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 3 * 1000);
            }
        });
//        test();
//        getByteArray((byte) 10);
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[1];
        secureRandom.nextBytes(bytes);
        bytesToBits(bytes);

    }

    public static byte[] getByteArray(byte value) {
        byte[] byteArr = new byte[8]; //一个字节八位
        for (int i = 7; i > 0; i--) {
            byteArr[i] = (byte) (value & 1); //获取最低位
            value = (byte) (value >> 1); //每次右移一位
        }
        Logger.e(TAG, "byteArr=" + Arrays.toString(byteArr));
        return byteArr;
    }

    private static boolean[] bytesToBits(byte[] data) {
        Logger.e(TAG, "byte[0]=" + data[0]);
        boolean[] bits = new boolean[data.length * 8];
        for (int i = 0; i < data.length; ++i)
            for (int j = 0; j < 8; ++j)
                bits[(i * 8) + j] = (data[i] & (1 << (7 - j))) != 0;

        Logger.e(TAG, "bits=" + Arrays.toString(bits));
        return bits;
    }

    private void test() {
        EditText editText = findViewById(R.id.et_test);
        String text = "1";
        editText.setText(text);
        editText.setSelection(0);
        String params = "a=3&b=1";
        String[] keyValues = params.split("&|=");
        rsa();

        mTxGlide = findViewById(R.id.glide);

        String value = BigDecimalUtil.add("1.22", "2", -2);
        BigDecimal bigDecimal = new BigDecimal("109");
        String value1 = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
        Logger.e(TAG, value1);
        Logger.e(TAG, BigDecimalUtil.scale(value1));

        Logger.e(TAG, PackageUtil.getPackageName(this));
        Logger.e(TAG, PackageUtil.isAppProcess(this));
    }

    private void rsa() {
/*        String text = "128fbec97fd740160defc74c5d340b5c";
        String enTx = "RvvScEmuXH+wHTupQ/c9jt1aQ2Meht1v49y6kvyBiuhYYv4+5QnFgXJkUBQk8d8U/MQRTKMPr+Qt" +
                "1mobJNdjUwbsZjqbEtHpn2bF/UQCrm07JNapn578jmkTYnyvxZsuiRzUbbc5RcAe99vAkMyDfSTG" +
                "/Do5LhO5h8BQ2RilW8a9ZAZL48UgX2yblvR2tAGYLzF8PnWWOhhoEhzYfZYb4PJFMbb2AtDqS5KR" +
                "Qdb8PtfR3WtqG+/axZtwNeCBSifXQcK6KwkyzuGJj//Fq4L60SoXGKaQlLiUIx3F8AvG1WnvF7Zt" +
                "qKUzIxtA5coHay0A8//DO+Ug4CXnHyQMfla3wA==";
        try {
            PublicKey publicKey = RSAUtils.keyStrToPublicKey(RSAUtils.readKeyFromAssets(getApplication(),"publicKey.txt"));
            PrivateKey privateKey = RSAUtils.keyStrToPrivate(RSAUtils.readKeyFromAssets(getApplication(),"privateKey.txt"));
            String encrpt = RSAUtils.encryptDataByPublicKey(text.getBytes(),publicKey);
            LogUtil.e("加密文本",encrpt);
            String decrypt = RSAUtils.decryptedToStrByPrivate(enTx,privateKey);
            LogUtil.e("解密文本",decrypt);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("加密解密",e.getMessage());
        }*/
    }


    public void banner(View v) {
        Intent intent = new Intent(this, BannerActivity.class);
        startActivity(intent);
    }

    public void mining(View v) {
//        ToastUtil.show("toast测试" + Math.random());
        Intent intent = new Intent(this, MiningActivity.class);
        startActivity(intent);
    }


    public void widgetTest(View view) {
        Intent intent = new Intent(this, WidgetTestActivity.class);
        startActivity(intent);
    }


    public void tab(View view) {
/*        if(mTxGlide.getVisibility() == View.VISIBLE){
            mTxGlide.setVisibility(View.GONE);
        }else{
            mTxGlide.setVisibility(View.VISIBLE);
            mTxGlide.setText(Math.random() + "");
        }*/


        Intent intent = new Intent(this, TabWidgetActivity.class);
        startActivity(intent);
    }

    public void testFragment(View view) {
        Intent intent = new Intent(this, TestFragmentActivity.class);
        startActivity(intent);
    }

    public void hsv(View view) {
        Intent intent = new Intent(this, HorSWithViewPagerActivity.class);
        startActivity(intent);
    }

    public void pagerMagin(View view) {
        Intent intent = new Intent(this, PagerMaginActivity.class);
        startActivity(intent);
    }

    public void recyclerView(View view) {
        Intent intent = new Intent(this, RecyclerViewTestActivity.class);
        startActivity(intent);
    }

    public void rvWithVp(View view) {
        Intent intent = new Intent(this, RvWithViewPagerActivity.class);
        startActivity(intent);
    }

    public void rvBanner(View view) {
        Intent intent = new Intent(this, RvBannerActivity.class);
        startActivity(intent);
    }

    public void checkLifecycle(View view) {
        startActivity(new Intent(this, LifecycleTestActivity.class));
    }

    public void calendar(View view) {
        startActivity(new Intent(this, CalendarTaibaiActivity.class));
    }

    public void touch(View view) {
        startActivity(new Intent(this, TouchEventActivity.class));
    }

    public void projectsBase(View view) {
        startActivity(new Intent(this, ProjectsBaseActivity.class));
    }


    public void rx(View view) {
        startActivity(new Intent(this, RxTestActivity.class));
    }

    public void constraint(View view) {
        startActivity(new Intent(this, ConstraintLayoutActivity.class));
    }

    public void editFocus(View view) {
        startActivity(new Intent(this, EditTextFocusActivity.class));
    }
}
