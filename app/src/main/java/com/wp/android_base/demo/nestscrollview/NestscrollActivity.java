package com.wp.android_base.demo.nestscrollview;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;

/**
 * Created by wp on 2019/9/29.
 * <p>
 * Description:NestScrollView嵌套RecyclerView，导致RecyclerView获取焦点，导致整个NestScrollView内容上移
 *
 *解决：1.在NestScrollView的唯一子View的  根布局设置android:descendantFocusability=”blocksDescendants”
 *
 *
 * android:descendantFocusability 有三种值：
 * beforeDescendants：viewgroup会优先其子类控件而获取到焦点
 * afterDescendants：viewgroup只有当其子类控件不需要获取焦点时才获取焦点
 * blocksDescendants：viewgroup会覆盖子类控件而直接获得焦点
 */

public class NestscrollActivity extends BaseActivity{
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_nestscroll;
    }

    @Override
    protected void initializeView() {
        super.initializeView();
        RecyclerView rv = findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);
        rv.setNestedScrollingEnabled(false);
        MyAdapter adapter = new MyAdapter();
        rv.setAdapter(adapter);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(NestscrollActivity.this).inflate(R.layout.recycler_view_nest_scroll,viewGroup,false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.txPosition.setText("第" + i+"个");
        }

        @Override
        public int getItemCount() {
            return 100;
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            public TextView txPosition;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txPosition = itemView.findViewById(R.id.tx_position);
            }
        }
    }
}
