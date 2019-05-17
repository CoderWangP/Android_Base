package com.wp.android_base.test.banner;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.image.glide.GlideApp;
import com.wp.android_base.base.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangpeng on 2018/9/19.
 * <p>
 * Description:
 */

public class RvWithViewPagerActivity extends BaseActivity{


    private ViewPager mVpBanner;

    private RecyclerView mRvHeader;

    private String a = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=4026621761,1071296249&fm=27&gp=0.jpg";
    private String b = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=674975923,1424777705&fm=27&gp=0.jpg";

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_rv_with_view_pager;
    }

    @Override
    protected void initializeView() {
        super.initializeView();
        mVpBanner = findViewById(R.id.vp_banner);
        mRvHeader = findViewById(R.id.rv_header);
    }


    @Override
    protected void requestDatas() {
        super.requestDatas();
        setVpDatas();
        setRvDatas();
    }

    private void setRvDatas() {
        RecyclerView.Adapter<ViewHolder> adapter = new RecyclerView.Adapter<ViewHolder>() {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(RvWithViewPagerActivity.this).inflate(R.layout.recycler_view_with_vp,parent,false);
                return new ViewHolder(v);
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                holder.txItemPosition.setText("position>>" + position);
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvHeader.setLayoutManager(linearLayoutManager);
        mRvHeader.setAdapter(adapter);

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(mRvHeader);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txItemPosition;
        public ViewHolder(View itemView) {
            super(itemView);
            txItemPosition = itemView.findViewById(R.id.tx_position);
        }
    }

    private void setVpDatas() {
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
                GlideApp.with(RvWithViewPagerActivity.this)
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
}
