package com.wp.android_base.test.tab;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wp.android_base.R;
import com.wp.android_base.base.tab.BaseTabFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangpeng on 2018/8/7.
 * <p>
 * Description:
 */

public class TabFragment extends BaseTabFragment{


    private TextView mTxTIlte;
    private RecyclerView mRv;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_tab;
    }

    @Override
    protected void initializeView() {
        super.initializeView();
        mTxTIlte = mRootView.findViewById(R.id.title);
        mRv = mRootView.findViewById(R.id.rv_with_coordinetorLayout);
    }

    @Override
    protected void requestDatas() {
        mTxTIlte.setText(mBundle == null ? "title" : mBundle.getString("title"));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(linearLayoutManager);
        mRv.setAdapter(new MyAdapter(createDatas()));
        super.requestDatas();
    }

    private List<String> createDatas() {
        List<String> datas = new ArrayList<>();
        int random = (int) (Math.random() * 50);
        for(int i=0;i<random;i++){
            datas.add("position:" + i);
        }
        return datas;
    }

    @Override
    protected void loadNetData() {

    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        public List<String> mPositions;

        public MyAdapter(List<String> positions) {
            this.mPositions = positions;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_tab,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.mTxPosition.setText(mPositions.get(position));
        }

        @Override
        public int getItemCount() {
            return mPositions == null ? 0 : mPositions.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            public TextView mTxPosition;

            public ViewHolder(View itemView) {
                super(itemView);
                mTxPosition = itemView.findViewById(R.id.tx_position);
            }
        }
    }
}
