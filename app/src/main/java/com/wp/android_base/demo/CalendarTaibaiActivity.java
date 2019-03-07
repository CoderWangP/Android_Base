package com.wp.android_base.demo;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wp.android_base.LoginActivity;
import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.utils.log.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangpeng on 2018/11/13.
 * <p>
 * Description:仿太白万年历
 */

public class CalendarTaibaiActivity extends BaseActivity{

    public static final String TAG = "CalendarTaibaiActivity";

    private RecyclerView mRvContent;
    private List<String> mDatas;

    private View mCalendar;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_calendar_taibai;
    }

    @Override
    protected void initializeView() {
        super.initializeView();
        File file = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        Logger.e(TAG,file.getAbsolutePath());
        mRvContent = findViewById(R.id.rv_content);
        mCalendar = findViewById(R.id.view_calendar);

        mRvContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
/*                Logger.e(TAG,"onScrollStateChanged>>scrolling up>>" + mRvContent.canScrollVertically(-1));
                Logger.e(TAG,"onScrollStateChanged>>scrolling down>>" + mRvContent.canScrollVertically(1));*/
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    protected void registerListener() {
        super.registerListener();
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
        mRvContent.setLayoutManager(linearLayoutManager);
//        mRvContent.addItemDecoration(new LinearItemDecoration("#00ff00", ScreenUtil.dp2px(20)));
        mRvContent.setAdapter(new MyAdapter());
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{


        @NonNull
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(CalendarTaibaiActivity.this).inflate(R.layout.recycler_view_test,parent,false);
            return new MyAdapter.ViewHolder(view);
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

/*    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Logger.e(TAG,"dispatchTouchEvent>>" + mRvContent.canScrollVertically(-1));
        return super.dispatchTouchEvent(ev);
    }*/
}
