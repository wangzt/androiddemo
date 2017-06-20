package com.tomsky.androiddemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tomsky.androiddemo.BaseApplication;
import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.util.UIUtils;

import java.util.List;

/**
 * Created by j-wangzhitao on 16-12-2.
 */

public class GridRecycleAdapter extends RecyclerView.Adapter<GridRecycleAdapter.MyViewHolder> {

    private List<String> datas;
    private int mItemWidth, mItemHeight;

    public GridRecycleAdapter() {
        int screenWidth = BaseApplication.getContext().getResources().getDisplayMetrics().widthPixels;
        mItemHeight = (screenWidth - UIUtils.dp2px(62)) / 4;
        mItemWidth = mItemHeight;
    }

    public void setDatas(List<String> datas) {
        this.datas = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_view, parent, false);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new StaggeredGridLayoutManager.LayoutParams(mItemWidth, mItemHeight);
        } else {
            layoutParams.width = mItemWidth;
            layoutParams.height = mItemHeight;
        }
        view.setLayoutParams(layoutParams);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (datas == null) return;

        String text = datas.get(position);
        holder.textView.setText(text);
    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }

    class MyViewHolder extends ViewHolder {
        public TextView textView;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.grid_item_tv);
        }
    }
}
