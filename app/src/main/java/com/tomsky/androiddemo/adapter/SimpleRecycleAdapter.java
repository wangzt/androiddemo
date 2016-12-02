package com.tomsky.androiddemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by j-wangzhitao on 16-12-2.
 */

public class SimpleRecycleAdapter extends RecyclerView.Adapter<SimpleRecycleAdapter.MyViewHolder> {

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MyViewHolder extends ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
