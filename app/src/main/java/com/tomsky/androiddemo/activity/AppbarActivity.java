package com.tomsky.androiddemo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tomsky.androiddemo.R;

/**
 * Created by j-wangzhitao on 17-8-8.
 */

public class AppbarActivity extends AppCompatActivity {

    public Context mContext;
    public Toolbar mToolbarTb;

    private SwipeRefreshLayout mRefreshSrl;
    private RecyclerView mContentRv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appbar);

        mContentRv = (RecyclerView) findViewById(R.id.rv_content);
        mContentRv.setLayoutManager(new LinearLayoutManager(this));
        mContentRv.setAdapter(new ContentAdapter());

        mRefreshSrl = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);
        mRefreshSrl.setColorSchemeResources(R.color.red, R.color.blue);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mContext = this;

        mToolbarTb = (Toolbar) findViewById(R.id.tb_toolbar);
        if (mToolbarTb!=null) {
            setSupportActionBar(mToolbarTb);

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (mToolbarTb != null) {
            mToolbarTb.setTitle(title);
        }
    }

    private class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentHolder>{

        @Override
        public ContentAdapter.ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ContentHolder(LayoutInflater.from(AppbarActivity.this).inflate(R.layout.item_simple, parent, false));
        }

        @Override
        public void onBindViewHolder(ContentAdapter.ContentHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 50;
        }

        class ContentHolder extends RecyclerView.ViewHolder{

            public ContentHolder(View itemView) {
                super(itemView);

            }
        }

    }
}
