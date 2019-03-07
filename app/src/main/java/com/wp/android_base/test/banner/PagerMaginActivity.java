package com.wp.android_base.test.banner;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.image.glide.GlideApp;
import com.wp.android_base.base.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangpeng on 2018/9/14.
 * <p>
 * Description:
 */

public class PagerMaginActivity extends BaseActivity {

    private String a = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=4026621761,1071296249&fm=27&gp=0.jpg";
    private String b = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=674975923,1424777705&fm=27&gp=0.jpg";

    private ViewPager mVp;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_pager_magin;
    }

    @Override
    protected void initializeView() {
        super.initializeView();
        mVp = findViewById(R.id.vp_banner);
        mVp.setPageMargin(ScreenUtil.dp2px(14));
    }

    @Override
    protected void registerListener() {
        super.registerListener();
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
/*                if (position == 0) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mVp.getLayoutParams();
                    params.leftMargin = ScreenUtil.dp2px(14);
                    params.rightMargin = ScreenUtil.dp2px(28);
                    mVp.setLayoutParams(params);
                } else if (position == 1) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mVp.getLayoutParams();
                    params.leftMargin = ScreenUtil.dp2px(28);
                    params.rightMargin = ScreenUtil.dp2px(14);
                    mVp.setLayoutParams(params);
                }*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
                View view = LayoutInflater.from(container.getContext()).inflate(R.layout.view_pager_banner, container, false);
                ImageView imageView = view.findViewById(R.id.image_banner);
                GlideApp.with(PagerMaginActivity.this)
                        .load(bannerItemDatas.get(position))
                        .into(imageView);
                container.addView(view);
                return view;
            }
        };

        mVp.setAdapter(pagerAdapter);
    }
}
