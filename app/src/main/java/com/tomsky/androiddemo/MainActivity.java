package com.tomsky.androiddemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.tomsky.androiddemo.api.BaseBean;
import com.tomsky.androiddemo.api.BaseMethod;
import com.tomsky.androiddemo.util.AlgorithmUtils;
import com.tomsky.androiddemo.util.RegexUtils;
import com.tomsky.androiddemo.util.StringUtil;
import com.tomsky.androiddemo.util.UIUtils;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
                        intent.putExtra("just_for_like", 100L);
//                        for (int i = 0; i < 3000; i++) {
//                            intent.putExtra("my_extra_"+i, "gfapdgdapgfjkgpaergkdfgsdfvalue_"+i);
//                        }
                        startActivity(intent);
                    } else {
                        if (data.name.equals("test_regex")) {
//                            String topic = "#AB你好#CD不删#EF你好#HY存在";
//                            Log.d("wzt-regex", "topic_ori:"+topic);
//                            Log.d("wzt-regex", "topic_result:"+ StringUtil.removeTopic(topic));
//                            RegexUtils.testComma();
                            AlgorithmUtils.testMergePoints();
                        }
                    }
                }
            }
        });

        BaseBean baseBean = new BaseBean();
        baseBean.setUid("11111111111");
        baseBean.setUname("my name");
        baseBean.setAge(18);
        BaseMethod.printBaseBean(baseBean);

//        String tsId = "125487166";
//        String guid = "TPqnPSdYXFGH8UP92x9QXw==";

//        String key = getMD5code(tsId).substring(0, 16);
//        String key = tsId;
//
//        String code = Security.decode(guid, key);
//
        Log.d("wzt-sec", "status bar height:"+ UIUtils.getStatusBarHeight());
        RegexUtils.matchDollar();
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
        datas.add(new ItemData("Android View demo", "com.tomsky.androiddemo.activity.AndroidViewActivity"));
        datas.add(new ItemData("Rotation demo", "com.tomsky.androiddemo.activity.RotationActivity"));
        datas.add(new ItemData("Camera demo", "com.tomsky.androiddemo.activity.CameraActivity"));
        datas.add(new ItemData("Bili danmuku demo", "com.tomsky.androiddemo.activity.BiliDanmukuActivity"));
        datas.add(new ItemData("Live danmuku demo", "com.tomsky.androiddemo.activity.LiveDanmuActivity"));
        datas.add(new ItemData("Lucky pan demo", "com.tomsky.androiddemo.activity.LuckyPanActivity"));
        datas.add(new ItemData("Banner demo", "com.tomsky.androiddemo.activity.BannerActivity"));
        datas.add(new ItemData("DND demo", "com.tomsky.androiddemo.activity.TrashActivity"));
        datas.add(new ItemData("Sticker demo", "com.tomsky.androiddemo.activity.StickerActivity"));
        datas.add(new ItemData("OpenGL demo", "com.tomsky.androiddemo.activity.OpenGLActivity"));
        datas.add(new ItemData("Abstract Parcel demo", "com.tomsky.androiddemo.activity.MutiCreaterActivity"));
        datas.add(new ItemData("AppbarLayout demo", "com.tomsky.androiddemo.activity.AppbarActivity"));
        datas.add(new ItemData("CoordinatorLayout demo", "com.tomsky.androiddemo.activity.CoordinatorLayoutActivity"));
        datas.add(new ItemData("AIDL demo", "com.tomsky.androiddemo.activity.AIDLActivity"));
        datas.add(new ItemData("RXJava demo", "com.tomsky.androiddemo.activity.RXJavaActivity"));
        datas.add(new ItemData("Anim demo", "com.tomsky.androiddemo.activity.AnimDemoActivity"));
        datas.add(new ItemData("Constraint demo", "com.tomsky.androiddemo.activity.ConstraintActivity"));
        datas.add(new ItemData("Dialog activity", "com.tomsky.androiddemo.activity.DialogActivity"));
        datas.add(new ItemData("Queue activity", "com.tomsky.androiddemo.activity.QueueActivity"));
        datas.add(new ItemData("String Encode activity", "com.tomsky.androiddemo.StringEncodeActivity"));
        datas.add(new ItemData("Flutter activity", "com.tomsky.androiddemo.activity.MyFlutterActivity"));
        datas.add(new ItemData("Dynamic activity", "com.tomsky.androiddemo.activity.DynamicUIActivity"));
        datas.add(new ItemData("New Dynamic activity", "com.tomsky.androiddemo.activity.DyLayoutActivity"));
        datas.add(new ItemData("PicInPic activity", "com.tomsky.androiddemo.activity.PicInPicActivity"));
        datas.add(new ItemData("Float activity", "com.tomsky.androiddemo.activity.FloatActivity"));
        datas.add(new ItemData("Recycler Drag activity", "com.tomsky.androiddemo.activity.RecyclerDragActivity"));
        datas.add(new ItemData("ExpandLayout activity", "com.tomsky.androiddemo.activity.ExpandLayoutActivity"));
        datas.add(new ItemData("OutLine activity", "com.tomsky.androiddemo.activity.OutLineActivity"));
        datas.add(new ItemData("ViewPager2 activity", "com.tomsky.androiddemo.activity.ViewPager2Activity"));

        return datas;
    }

}
