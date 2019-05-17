package com.wp.android_base.test.base;

import android.widget.ImageView;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.image.glide.GlideImageLoader;
import com.wp.android_base.base.utils.ScreenUtil;

/**
 * Created by wangpeng on 2018/7/27.
 * <p>
 * Description:
 */

public class GlideTestActivity extends BaseActivity{

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_glide_test;
    }

    @Override
    protected void initializeView() {
        super.initializeView();

        ImageView imageViewNet = findViewById(R.id.image_net);
        GlideImageLoader.loadImageWithUrl(this,"https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3620561714,625824566&fm=27&gp=0.jpg",imageViewNet);

        ImageView imageViewRes = findViewById(R.id.image_res);
//        GlideImageLoader.loadImageWithRes(this,R.drawable.glide_test_dog,imageViewRes);
//        GlideImageLoader.loadImageWithCircle(this,R.drawable.glide_test_dog,imageViewRes);
        GlideImageLoader.loadImageWithRoundCorner(this,R.drawable.glide_test_dog,imageViewRes,ScreenUtil.dp2px(30));


        ImageView imageRoundCorner = findViewById(R.id.image_round_corner);
        GlideImageLoader.loadImageWithRoundCorner(this,"https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2432163883,4023441733&fm=27&gp=0.jpg",imageRoundCorner,ScreenUtil.dp2px(20));

        ImageView imageRound = findViewById(R.id.image_round);
        GlideImageLoader.loadImageWithCircle(this,"https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1567957104,3107114943&fm=27&gp=0.jpg",imageRound);
    }
}
