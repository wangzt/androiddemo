package com.tomsky.androiddemo.view.banner;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by j-wangzhitao on 17-5-16.
 */

public class BannerAdapter extends PagerAdapter {

    private List<String> mItems = new ArrayList<String>();

    private LinkedList<TextView> mListViews = new LinkedList<>();

    private int size;
    public void setData(List<String> datas) {
        this.mItems = datas;
        size = mItems.size();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TextView itemView = mListViews.poll();
        if (itemView == null) {
            itemView = new TextView(container.getContext());
            itemView.setGravity(Gravity.CENTER);
            itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        }
        if (position == 1 || position == size - 1) {
            itemView.setBackgroundColor(Color.GREEN);
        } else {
            itemView.setBackgroundColor(Color.RED);
        }
        itemView.setTag(mItems.get(position));
        itemView.setText(mItems.get(position));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = (String) v.getTag();
                Toast.makeText(v.getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((TextView) object);
        mListViews.addFirst((TextView) object);
    }
}
