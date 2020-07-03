package com.tomsky.androiddemo.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.adapter.MainPagerAdapter;
import com.tomsky.androiddemo.adapter.RecyclerAdapter;
import com.tomsky.androiddemo.view.ItemDivider;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by j-wangzhitao on 17-8-21.
 */

public class CoordinatorLayoutActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ArrayList<String> list;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;

    private ViewPager mViewPager;
    private MainPagerAdapter mainPagerAdapter;

    static int[] PAGE_RESOURCE = {R.drawable.p0, R.drawable.p1,R.drawable.p2, R.drawable.p3};
    private AppBarLayout appBarLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);

        initToolbar();
        initData();
        initView();
        initViewAction();
    }

    /**
     * 初始化工具栏
     */
    public void initToolbar() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        //设置Titlebar title 展开时title 位置
        collapsingToolbarLayout.setExpandedTitleGravity(Gravity.NO_GRAVITY);
        //设置Titlebar title 折叠后title 位置
        collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER_HORIZONTAL);
        //设置Titlebar title 折叠后字体颜色
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        //设置Titlebar title 展开后字体颜色
        collapsingToolbarLayout.setExpandedTitleColor(Color.BLUE);
        //设置折叠后titlebar颜色
        collapsingToolbarLayout.setContentScrimColor(Color.parseColor("#99FF0000"));
        //设置title 是否可以被展开
        collapsingToolbarLayout.setTitleEnabled(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //设置显示返回按钮 返回按钮的监听需在onOptionsItemSelected(MenuItem item) 方法中进行监听 返回按钮ID：android.R.id.home
        actionBar.setDisplayHomeAsUpEnabled(true);
        //设置是否显示titlebar title 默认为true 默认显示项目名称
        actionBar.setDisplayShowTitleEnabled(true);
        //自定义显示title内容
        actionBar.setTitle("松松");
    }

    private void initData() {
        if(list == null) {
            list = new ArrayList<String>();
        }
        for (int i = 0; i < 100; i++){
            list.add("item-->" + i);
        }
    }

    private void initView() {
        //下拉是刷新组件设置
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //设置样式刷新显示的位置
        mSwipeRefreshLayout.setProgressViewOffset(true, -20, 100);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.swiperefresh_color1, R.color.swiperefresh_color2, R.color.swiperefresh_color3, R.color.swiperefresh_color4);

        //RecyclerView 设置
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerAdapter = new RecyclerAdapter(this, list);
        mRecyclerAdapter.addOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {//itemclick listener
            @Override
            public void onItemClick(ViewGroup group, View view, int position) {

            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this)); //设置RecyclerView布局方式线性布局
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new ItemDivider());

        //ViewPager Define
        mViewPager = (ViewPager) findViewById(R.id.toolbar_viewpager);
        mainPagerAdapter = new MainPagerAdapter(this, PAGE_RESOURCE);
        mViewPager.setAdapter(mainPagerAdapter);
        mViewPager.setCurrentItem(10 * PAGE_RESOURCE.length, true);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);


    }

    private void initViewAction() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset >= 0) {
                    mSwipeRefreshLayout.setEnabled(true);
                } else {
                    mSwipeRefreshLayout.setEnabled(false);
                }
            }
        });

        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //解决滑动冲突
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        mSwipeRefreshLayout.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mSwipeRefreshLayout.setEnabled(true);
                        break;
                }
                //方案二
//                v.performClick(); //触发OnClick方法
//                mViewPager.getParent().requestDisallowInterceptTouchEvent(true); //防止父容器阻断子控件事件
                return false;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), PAGE_RESOURCE[position % PAGE_RESOURCE.length]);
//                Palette palette = Palette.from(bitmap).generate();
//                Palette.Swatch swatch = palette.getVibrantSwatch();
//                if (swatch == null) {
//                    swatch = palette.getMutedSwatch();
//                }
//                int color = swatch.getRgb();
//                collapsingToolbarLayout.setContentScrimColor(color);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onRefresh() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = CoordinatorLayoutActivity.handler.obtainMessage();
                msg.obj = CoordinatorLayoutActivity.this;
                msg.what = 0;
                CoordinatorLayoutActivity.handler.sendMessage(msg);
            }
        }, 2000);
    }


    static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    CoordinatorLayoutActivity activity = (CoordinatorLayoutActivity) msg.obj;
                    activity.mSwipeRefreshLayout.setRefreshing(false);
                    break;
            }
        }
    };
}
