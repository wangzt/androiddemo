package com.tomsky.androiddemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzhitao on 8/3/16.
 */
public class MainListAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;

    private List<ItemData> mData = new ArrayList<ItemData>();

    public MainListAdapter(Context context, List<ItemData> list) {
        mLayoutInflater = LayoutInflater.from(context);
        mData.clear();
        mData.addAll(list);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.main_list_item, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.item_tv);
            holder.arrow = (ImageView) convertView.findViewById(R.id.item_arrow);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ItemData data = mData.get(position);
        holder.text.setText(data.title);

        if (data.hasSub) {
            holder.arrow.setVisibility(View.VISIBLE);
        } else {
            holder.arrow.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView text;
        ImageView arrow;
    }
}
