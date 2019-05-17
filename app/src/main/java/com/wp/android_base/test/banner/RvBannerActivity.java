package com.wp.android_base.test.banner;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.utils.ScreenUtil;
import com.wp.android_base.base.widget.recyclerview.decoration.LinearItemDecoration;
import com.wp.android_base.base.widget.recyclerview.snaphelper.PagerEndSnapHelper;
import com.wp.android_base.base.widget.recyclerview.snaphelper.StartPageSnapHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangpeng on 2018/10/15.
 * <p>
 * Description:
 */

public class RvBannerActivity extends BaseActivity{

    private RecyclerView mRvPager;
    private LinearLayoutManager manager;

    private String a = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=4026621761,1071296249&fm=27&gp=0.jpg";
    private String b = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=674975923,1424777705&fm=27&gp=0.jpg";
    private String c = "http://d.hiphotos.baidu.com/image/h%3D300/sign=c2bcde3f2c3fb80e13d167d706d02ffb/4034970a304e251f569f36f6aa86c9177f3e5350.jpg";

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_rv_banner;
    }


    @Override
    protected void initializeView() {
        super.initializeView();

        List<String> bannerItemDatas = new ArrayList<>();
        bannerItemDatas.add(a);
        bannerItemDatas.add(b);
//        bannerItemDatas.add(c);

        mRvPager = findViewById(R.id.rv_pager);
        mRvPager.addItemDecoration(new LinearItemDecoration(Color.RED, ScreenUtil.dp2px(14),false,true));

        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvPager.setLayoutManager(manager);


        RvBannerAdapter rvBannerAdapter = new RvBannerAdapter(this);
        rvBannerAdapter.setData(bannerItemDatas);
        mRvPager.setAdapter(rvBannerAdapter);



        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
//        pagerSnapHelper.attachToRecyclerView(mRvPager);

        PagerEndSnapHelper pagerEndSnapHelper = new PagerEndSnapHelper();
        pagerEndSnapHelper.attachToRecyclerView(mRvPager);

        StartPageSnapHelper startPageSnapHelper = new StartPageSnapHelper();
//        startPageSnapHelper.attachToRecyclerView(mRvPager);

/*        ExtendPagerSnapHelper extendPagerSnapHelper = new ExtendPagerSnapHelper();
        extendPagerSnapHelper.setOnPageChangeListener(new ExtendPagerSnapHelper.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Logger.e("onPageSelected>>",position + "");
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
        });
        extendPagerSnapHelper.attachToRecyclerView(mRvPager);*/

//        extendPagerSnapHelper.setCurrentItem(1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                extendPagerSnapHelper.setCurrentItem(2,true);
            }
        },5 * 1000);

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
    }
}
