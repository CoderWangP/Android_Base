package com.wp.android_base.test.banner;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.image.glide.GlideApp;
import com.wp.android_base.base.widget.recyclerview.decoration.LinearItemDecoration;
import com.wp.android_base.base.widget.recyclerview.snaphelper.PagerEndSnapHelper;
import com.wp.android_base.test.base.LogTestActivity;
import com.wp.android_base.base.utils.ScreenUtil;
import com.wp.android_base.base.utils.log.Logger;
import com.wp.android_base.base.widget.banner.ADViewPager;
import com.wp.android_base.base.widget.banner.BaseBannerPagerAdapter;
import com.wp.android_base.base.widget.banner.DotViews;
import com.wp.android_base.base.widget.banner.ScalePageTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangpeng on 2018/7/4.
 */

public class BannerActivity extends BaseActivity {

    private static final String TAG = "BannerActivity";

    private String a = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=4026621761,1071296249&fm=27&gp=0.jpg";
    private String b = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=674975923,1424777705&fm=27&gp=0.jpg";
    private String c = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1401286100,3082775436&fm=15&gp=0.jpg";

    private ADViewPager mVpBanner;
    private DotViews mDotViews;

    private ADViewPager mVpBanner2;
    private DotViews mDotViews2;

    private RecyclerView mRvPager;

    LinearLayoutManager manager;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_banner;
    }

    @Override
    protected void initializeView() {
        super.initializeView();
        mVpBanner = findViewById(R.id.vp_banner);
        mDotViews = findViewById(R.id.dotviews);

        mVpBanner2 = findViewById(R.id.vp_banner2);
        mDotViews2 = findViewById(R.id.dotviews2);

        List<String> bannerItemDatas = new ArrayList<>();
        bannerItemDatas.add(a);
        bannerItemDatas.add(b);
        bannerItemDatas.add(c);

        initBanner1(bannerItemDatas);
        initBanner2(bannerItemDatas);

        mRvPager = findViewById(R.id.rv_pager);

        mRvPager.addItemDecoration(new LinearItemDecoration(Color.WHITE,ScreenUtil.dp2px(14)));
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvPager.setLayoutManager(manager);


        RvBannerAdapter rvBannerAdapter = new RvBannerAdapter(this);
        rvBannerAdapter.setData(bannerItemDatas);
        mRvPager.setAdapter(rvBannerAdapter);

/*        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(mRvPager);*/

        PagerEndSnapHelper pagerEndSnapHelper = new PagerEndSnapHelper();
        pagerEndSnapHelper.attachToRecyclerView(mRvPager);

        mRvPager.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
    }



    private void initBanner2(List<String> bannerItemDatas) {
        ScalePageTransformer scalePageTransformer = new ScalePageTransformer();
        scalePageTransformer.setMinScale(0.85f);
        mVpBanner2.setPageTransformer(true, scalePageTransformer);
        mVpBanner2.setPageMargin(-ScreenUtil.dp2px(10));
        mDotViews2.setSize(bannerItemDatas.size());
        mVpBanner2.addPageChangeListener(new ADViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mDotViews2.select(position % bannerItemDatas.size());
            }
        });

        BaseBannerPagerAdapter<String> bannerPagerAdapter = new BaseBannerPagerAdapter<String>() {
            @Override
            protected void onBindViewData(int position, View view, String url) {
                ImageView imageView = view.findViewById(R.id.image_banner);
                Glide.with(BannerActivity.this).load(url).into(imageView);
/*                GlideApp.with(BannerActivity.this)
                        .load(url)
                        .into(imageView);*/
                view.setOnClickListener(v -> {
                    Logger.e(TAG, "click_position = " + position);
                    LogTestActivity.forward2LoginTest(BannerActivity.this);
                });
            }

            @Override
            protected int getPageLayoutId() {
                return R.layout.view_pager_banner;
            }
        };

        bannerPagerAdapter.setBannerItemDatas(bannerItemDatas);

        mVpBanner2.setAdapter(bannerPagerAdapter);
        mVpBanner2.setStartItem();
//        mVpBanner2.play(4 * 1000);
    }

    private void initBanner1(List<String> bannerItemDatas) {
        mVpBanner.setOffscreenPageLimit(3);
        mVpBanner.setPageMargin(ScreenUtil.dp2px(20));


        mDotViews.setSize(bannerItemDatas.size());

        mVpBanner.addPageChangeListener(new ADViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mDotViews.select(position % bannerItemDatas.size());
/*                if(position == 0){
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mVpBanner.getLayoutParams();
                    params.rightMargin = ScreenUtil.dp2px(50);
                    mVpBanner.setLayoutParams(params);
                }else if(position == bannerItemDatas.size() - 1){
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mVpBanner.getLayoutParams();
                    params.rightMargin = ScreenUtil.dp2px(10);
                    mVpBanner.setLayoutParams(params);
                }*/
            }
        });

        BaseBannerPagerAdapter<String> bannerPagerAdapter = new BaseBannerPagerAdapter<String>() {
            @Override
            protected void onBindViewData(int position, View view, String url) {
                ImageView imageView = view.findViewById(R.id.image_banner);
//                Glide.with(BannerActivity.this).load(url).into(imageView);
                GlideApp.with(BannerActivity.this)
                        .load(url)
                        .into(imageView);
                view.setOnClickListener(v -> {
                    Logger.e(TAG, "click_position = " + position);
                    LogTestActivity.forward2LoginTest(BannerActivity.this);
                });
            }

            @Override
            protected int getPageLayoutId() {
                return R.layout.view_pager_banner;
            }
        };

        bannerPagerAdapter.setBannerItemDatas(bannerItemDatas);

        mVpBanner.setAdapter(bannerPagerAdapter);
        mVpBanner.setCurrentItem(0);
//        mVpBanner.play(4 * 1000);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mVpBanner != null && !mVpBanner.isPlaying()) {
            Logger.e(TAG, "banner恢复");
            mVpBanner.play(4 * 1000);
        }

        if (mVpBanner2 != null && !mVpBanner2.isPlaying()) {
            Logger.e(TAG, "banner2恢复");
            mVpBanner2.play(4 * 1000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVpBanner != null && mVpBanner.isPlaying()) {
            Logger.e(TAG, "banner停止");
            mVpBanner.stop();
        }

        if (mVpBanner2 != null && mVpBanner2.isPlaying()) {
            Logger.e(TAG, "banner2停止");
            mVpBanner2.stop();
        }
    }
}
