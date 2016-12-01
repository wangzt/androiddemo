package com.tomsky.androiddemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.tomsky.androiddemo.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity{

    private MainListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.main_list);
        mAdapter = new MainListAdapter(this, buildDatas());
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemData data = (ItemData) mAdapter.getItem(position);
                if (data != null) {
                    if (data.hasSub) {
                        Intent intent = new Intent();
                        intent.setClassName(MainActivity.this, data.name);
                        startActivity(intent);
                    } else {
                        if (data.name.equals("test_regex")) {
                            String topic = "#AB你好#CD不删#EF你好#HY存在";
                            Log.d("wzt-regex", "topic_ori:"+topic);
                            Log.d("wzt-regex", "topic_result:"+ StringUtil.removeTopic(topic));
                        }
                    }
                }
            }
        });

    }

    private List<ItemData> buildDatas() {
        List<ItemData> datas = new ArrayList<ItemData>();
        ItemData data = new ItemData("Test regex", "test_regex");
        data.hasSub = false;
        datas.add(data);
        datas.add(new ItemData("Release resource", "com.huajiao.animation.airplain.AnimateActivity"));
        datas.add(new ItemData("Test Fresco", "com.huajiao.animation.fresco.FrescoTest"));
        datas.add(new ItemData("Test Effect", "com.huajiao.animation.core.EffectActivity"));
        datas.add(new ItemData("Test WebView", "com.huajiao.animation.WebActivity"));
        datas.add(new ItemData("Test Path", "com.huajiao.animation.path.PathActivity"));
        datas.add(new ItemData("Test Drag", "com.huajiao.animation.activity.DragActivity"));
        datas.add(new ItemData("Test Copy and past", "com.huajiao.animation.activity.CopyActivity"));
        datas.add(new ItemData("Test Partical", "com.huajiao.animation.activity.ParticalActivity"));
        datas.add(new ItemData("Test KTV", "com.huajiao.animation.activity.KTVActivity"));

        return datas;
    }

}
