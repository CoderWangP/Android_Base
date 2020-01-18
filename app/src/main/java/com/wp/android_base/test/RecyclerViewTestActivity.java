package com.wp.android_base.test;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.utils.ScreenUtil;
import com.wp.android_base.base.utils.log.Logger;
import com.wp.android_base.base.widget.recyclerview.decoration.LinearItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangpeng on 2018/9/18.
 * <p>
 * Description:
 */

public class RecyclerViewTestActivity extends BaseActivity{

    static final String TAG = "RecyclerViewTestActivity";

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
            int random = 1 + (int) (Math.random() * (10 -1 + 1));
            StringBuilder sb = new StringBuilder();
            for(int j=0;j<random;j++){
                sb.append("text");
            }
            mDatas.add(sb.toString() + i);
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
            holder.txAuto.setText(mDatas.get(position));
 /*           holder.txAuto.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
            TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(holder.txAuto,20,30,TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM,TypedValue.COMPLEX_UNIT_SP);
            if(position == 3){
                int width = holder.txAuto.getMeasuredWidth();
                Logger.e(TAG,"width= " + width);
            }
            holder.txAuto.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            int width = holder.txAuto.getMeasuredWidth();
            Logger.e(TAG,"position = " + position,"getMeasuredWidth= " + width);

            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.txAuto.getLayoutParams();
            params.width = width;
            params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            holder.txAuto.setLayoutParams(params);*/
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            public TextView txAuto;
            public TextView txComplete;
            public ViewHolder(View itemView) {
                super(itemView);
                txAuto = itemView.findViewById(R.id.tx_auto);
                txComplete = itemView.findViewById(R.id.tx_complete_display);
            }
        }
    }

}
