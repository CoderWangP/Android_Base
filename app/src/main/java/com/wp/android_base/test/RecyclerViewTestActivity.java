package com.wp.android_base.test;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.utils.ScreenUtil;
import com.wp.android_base.base.widget.recyclerview.decoration.LinearItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangpeng on 2018/9/18.
 * <p>
 * Description:
 */

public class RecyclerViewTestActivity extends BaseActivity{

    private RecyclerView mRecyclerView;

    private List<String> mDatas;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_recycler_view;
    }

    @Override
    protected void initializeView() {
        super.initializeView();
        mRecyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected void requestDatas() {
        super.requestDatas();
        mDatas = new ArrayList<>();
        for(int i=0;i<100;i++){
            mDatas.add("position>>" + i);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new LinearItemDecoration("#00ff00", ScreenUtil.dp2px(20)));
        mRecyclerView.setAdapter(new MyAdapter());
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(RecyclerViewTestActivity.this).inflate(R.layout.recycler_view_test,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
            holder.txPosition.setText(mDatas.get(position));
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            public TextView txPosition;
            public ViewHolder(View itemView) {
                super(itemView);
                txPosition = itemView.findViewById(R.id.tx_recycler_test);
            }
        }
    }
}
