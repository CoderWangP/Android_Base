package com.wp.android_base.test.banner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wp.android_base.R;
import com.wp.android_base.base.image.glide.GlideApp;
import com.wp.android_base.base.utils.ScreenUtil;

import java.util.List;

/**
 * Created by wangpeng on 2018/8/25.
 * <p>
 * Description:
 */

public class RvBannerAdapter extends RecyclerView.Adapter<RvBannerAdapter.ViewHolder>{

    private List<String> mBanners;
    public void setData(List<String> banners){
        this.mBanners = banners;
    }

    private Context mContext;

    public RvBannerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pager_banner,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String banner = mBanners.get(position);
        GlideApp.with(mContext).load(banner).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mBanners == null ? 0 : mBanners.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_banner);
/*            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) image.getLayoutParams();
            params.width = ScreenUtil.getScreenWidth() - ScreenUtil.dp2px(14 * 3);
//            params.width = ScreenUtil.getScreenWidth() - ScreenUtil.dp2px(14 * 2 * 2);
//            params.width = ScreenUtil.getScreenWidth();
            params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
//            params.height = ScreenUtil.dp2px(300);
            image.setLayoutParams(params);*/
        }
    }
}
