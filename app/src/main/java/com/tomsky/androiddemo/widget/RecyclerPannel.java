package com.tomsky.androiddemo.widget;

import android.app.Activity;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.activity.RecycleActivity;
import com.tomsky.androiddemo.adapter.GridRecycleAdapter;
import com.tomsky.androiddemo.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j-wangzhitao on 17-6-20.
 */

public class RecyclerPannel extends BasePannel {

    private RecyclerView mRecyclerView;

    public RecyclerPannel(Activity context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.recycler_pannel;
    }

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.grid_recycle_view);

        int paddingLeftRight = UIUtils.dp2px(11);
        int paddingTop = UIUtils.dp2px(5);
        int paddingBottom = UIUtils.dp2px(5);

        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            datas.add(String.valueOf(i));
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        GridRecycleAdapter adapter = new GridRecycleAdapter();
        adapter.setDatas(datas);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new GridItemDecoration(UIUtils.dp2px(5)));

        mRecyclerView.setPadding(paddingLeftRight, paddingTop, paddingLeftRight, paddingBottom);
    }

}
