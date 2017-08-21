package com.tomsky.androiddemo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.tomsky.androiddemo.R;

/**
 * Created by j-wangzhitao on 17-8-21.
 */

public class MainPagerAdapter extends PagerAdapter {
    Context context;
    int resourceId[];
    LayoutInflater inflater;
    public MainPagerAdapter(Context context, int[] resourceId) {
        this.context = context;
        this.resourceId = resourceId;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 20 * resourceId.length;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    //不销毁界面
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        if(object != null) {
//            container.removeView((View) object);
//        }

        container.removeView((View)instantiateItem(container, position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public float getPageWidth(int position) {
        return super.getPageWidth(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.item_viewpager, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.view_pager_item_img);//new ImageView(context);

//        ViewGroup.LayoutParams params = imageView.getLayoutParams();
//        params.height = 200;
//        imageView.setLayoutParams(params);
        imageView.setMaxHeight(200);
        imageView.setMinimumHeight(200);
        position %= resourceId.length;
        imageView.setImageResource(resourceId[position]);

        ViewParent parent = view.getParent();
        if(parent != null) {
            ((ViewGroup)parent).removeAllViews();
        }
        container.addView(view);

        return view;//super.instantiateItem(container, position);
    }

}

