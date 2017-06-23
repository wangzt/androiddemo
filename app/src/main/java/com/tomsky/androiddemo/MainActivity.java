package com.tomsky.androiddemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.tomsky.androiddemo.util.StringUtil;

import java.security.MessageDigest;
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

//        String tsId = "125487166";
//        String guid = "TPqnPSdYXFGH8UP92x9QXw==";

//        String key = getMD5code(tsId).substring(0, 16);
//        String key = tsId;
//
//        String code = Security.decode(guid, key);
//
//        Log.d("wzt-sec", "code:"+code);
    }

    public static String getMD5code(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(string.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            return buf.toString();

            //          return buf.toString().substring(8,24);

        } catch (Exception e) {
            return "";
        }
    }

    private List<ItemData> buildDatas() {
        List<ItemData> datas = new ArrayList<ItemData>();
        ItemData data = new ItemData("Test regex", "test_regex");
        data.hasSub = false;
        datas.add(data);
        datas.add(new ItemData("RecycleView demo", "com.tomsky.androiddemo.activity.RecycleActivity"));
        datas.add(new ItemData("DrawingCache demo", "com.tomsky.androiddemo.activity.DrawingCacheActivity"));
        datas.add(new ItemData("Camera2 demo", "com.tomsky.androiddemo.activity.Camera2Activity"));
        datas.add(new ItemData("UI demo", "com.tomsky.androiddemo.activity.AndroidUIActivity"));
        datas.add(new ItemData("Rotation demo", "com.tomsky.androiddemo.activity.RotationActivity"));
        datas.add(new ItemData("Camera demo", "com.tomsky.androiddemo.activity.CameraActivity"));
        datas.add(new ItemData("Bili danmuku demo", "com.tomsky.androiddemo.activity.BiliDanmukuActivity"));
        datas.add(new ItemData("Live danmuku demo", "com.tomsky.androiddemo.activity.LiveDanmuActivity"));
        datas.add(new ItemData("Lucky pan demo", "com.tomsky.androiddemo.activity.LuckyPanActivity"));
        datas.add(new ItemData("Banner demo", "com.tomsky.androiddemo.activity.BannerActivity"));
        datas.add(new ItemData("DND demo", "com.tomsky.androiddemo.activity.TrashActivity"));
        datas.add(new ItemData("Sticker demo", "com.tomsky.androiddemo.activity.StickerActivity"));

        return datas;
    }

}
