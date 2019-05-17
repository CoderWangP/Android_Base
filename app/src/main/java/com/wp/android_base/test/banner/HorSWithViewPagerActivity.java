package com.wp.android_base.test.banner;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.image.glide.GlideApp;
import com.wp.android_base.base.utils.ScreenUtil;
import com.wp.android_base.base.utils.ToastUtil;
import com.wp.android_base.base.utils.log.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangpeng on 2018/9/11.
 * <p>
 * Description:
 */

public class HorSWithViewPagerActivity extends BaseActivity{

    private static final String TAG = "HorSWithViewPagerActivity";

    private ViewPager mVpBanner;
    private HorizontalScrollView mHSV;

    private TextView mTx1;
    private TextView mTx2;

    private String a = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=4026621761,1071296249&fm=27&gp=0.jpg";
    private String b = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=674975923,1424777705&fm=27&gp=0.jpg";

    private int HOS_ITEM_WIDTH = ScreenUtil.getScreenWidth() - ScreenUtil.dp2px(14) - ScreenUtil.dp2px(28);
    private int HOS_PAGE_MARGIN = ScreenUtil.dp2px(14);

    private LinkScrollContainer mLlLinkContainer;

    private LinearLayout mLlContainer1;
    private LinearLayout mLlContainer2;


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_hors_with_viewpager;
    }

    @Override
    protected void initializeView() {
        super.initializeView();
        mVpBanner = findViewById(R.id.vp_banner);
        mHSV = findViewById(R.id.hsv);
        mTx1 = findViewById(R.id.tx_1);
        mTx2 = findViewById(R.id.tx_2);
        mLlContainer1 = findViewById(R.id.ll_container_1);
        mLlContainer2 = findViewById(R.id.ll_container_2);
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) mLlContainer1.getLayoutParams();
        params1.width = HOS_ITEM_WIDTH;
        mLlContainer1.setLayoutParams(params1);

        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) mLlContainer2.getLayoutParams();
        params2.width = HOS_ITEM_WIDTH;
        mLlContainer2.setLayoutParams(params2);

        TextView textView = findViewById(R.id.tx_test);
        textView.setText(formatBankNo("273774873773737"));

        mLlLinkContainer = findViewById(R.id.ll_link_container);
        mLlLinkContainer.setLinkScroller(mHSV,mVpBanner);

        mLlLinkContainer.addClickView(mTx1);
        mLlLinkContainer.addClickView(mTx2);
    }



    @Override
    protected void registerListener() {
        super.registerListener();

        mVpBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                float marginX = (position + positionOffset) * (ScreenUtil.getScreenWidth() - HOS_PAGE_MARGIN * 3);
                float marginX = (position + positionOffset) * HOS_ITEM_WIDTH;
                Logger.e("onPageScrolled","position>>" + position + "");
                Logger.e("onPageScrolled","positionOffsetPixels>>" + marginX + "");
                Logger.e("onPageScrolled","positionOffset>>" + positionOffset + "");
                mHSV.scrollTo((int) marginX,0);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mTx1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show("tx1被点击");
            }
        });

        mTx2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show("tx2被点击");
            }
        });
    }

    @Override
    protected void requestDatas() {
        super.requestDatas();
        List<String> bannerItemDatas = new ArrayList<>();
        bannerItemDatas.add(a);
        bannerItemDatas.add(b);

        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View view = LayoutInflater.from(container.getContext()).inflate(R.layout.view_pager_banner,container,false);
                ImageView imageView = view.findViewById(R.id.image_banner);
                GlideApp.with(HorSWithViewPagerActivity.this)
                        .load(bannerItemDatas.get(position))
                        .into(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.show(position + "页被点击");
                    }
                });
                container.addView(view);
                return view;
            }
        };

        mVpBanner.setAdapter(pagerAdapter);
    }


    public static String formatBankNo(String bankNo) {
        try {

            if (TextUtils.isEmpty(bankNo)) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            int length = bankNo.length();
            int count = bankNo.length() / 4;
            for (int i = 0; i <= count; i++) {
                int endIndex = i * 4 + 4;
                if (endIndex > length - 1) {
                    sb.append(bankNo.substring(i * 4));
                } else {
                    sb.append(bankNo.substring(i * 4, endIndex));
                    sb.append("  ");
                }
            }
            return sb.toString();
        } catch (Exception e) {

        }
        return null;
    }
}
