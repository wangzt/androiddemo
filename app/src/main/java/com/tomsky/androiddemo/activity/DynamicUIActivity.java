package com.tomsky.androiddemo.activity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eclipsesource.v8.V8;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.GenericDraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.dynamic.ProomDataCenter;
import com.tomsky.androiddemo.dynamic.ProomLayoutManager;
import com.tomsky.androiddemo.dynamic.virtualview.ProomRootView;
import com.tomsky.androiddemo.util.RegexUtils;
import com.tomsky.androiddemo.util.UIUtils;
import com.tomsky.androiddemo.util.WeakHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DynamicUIActivity extends FragmentActivity implements WeakHandler.IHandler {

    private static final String TAG = "wzt-dynamic";

//    private ConstraintLayout container;
//    private int containerId = R.id.dynamic_container;

    private boolean hasAdd = false;

    private WeakHandler mHandler = new WeakHandler(this);
    private static final int MSG_LAYOUT = 100;
    private static final int MSG_ON_ATTACH = 101;
    private static final int MSG_UPDATE_VIEW = 102;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);
//        container = findViewById(R.id.dynamic_container);
//
        findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testImage();
//                ProomDataCenter.parseKey(readData());
//                if (hasAdd) return;
//                hasAdd = true;
//
//                new Thread() {
//                    @Override
//                    public void run() {
//                        addSubView();
//                    }
//                }.start();
            }
        });

        findViewById(R.id.update_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyImage();
//                new Thread() {
//                    @Override
//                    public void run() {
//                        try {
//                            String gameStr = readKeyData(R.raw.p_game);
//                            JSONObject jsonObject = new JSONObject(gameStr);
//
//                            ProomDataCenter.getInstance().addSyncData("p_game", jsonObject.optJSONObject("p_game"));
//
//                            String userStr = readKeyData(R.raw.p_user);
//                            JSONObject jsonArray = new JSONObject(userStr);
//                            ProomDataCenter.getInstance().addSyncData("p_user", jsonArray.optJSONArray("p_user"));
//
//                            mHandler.sendEmptyMessage(MSG_UPDATE_VIEW);
//                        } catch (Exception e) {
//                            Log.e(TAG, "update", e);
//                        }
//
//                    }
//                }.start();
            }
        });

        findViewById(R.id.h5_update_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testScale();
//                try {
//                    String str = readKeyData(R.raw.h5_prop);
//                    JSONObject pObj = new JSONObject(str);
//                    ProomLayoutManager.getInstance().updateViewPropById("test_label", pObj);
//                } catch (Exception e) {
//                    Log.e(TAG, "update-h5-prop", e);
//                }


//                new Thread() {
//                    @Override
//                    public void run() {
//                        try {
//                            String str = readKeyData(R.raw.h5_data);
//                            JSONObject pObj = new JSONObject(str);
//                            ProomLayoutManager.getInstance().updateViewDataById("test_wrap_label", pObj);
//                        } catch (Exception e) {
//                            Log.e(TAG, "update-h5-data", e);
//                        }
//
//                    }
//                }.start();

//                new Thread() {
//                    @Override
//                    public void run() {
//                        try {
//                            String str = readKeyData(R.raw.h5_view);
//                            JSONObject vObj = new JSONObject(str);
//                            ProomLayoutManager.getInstance().addViewByJSON("test_container", 10, vObj);
//                        } catch (Exception e) {
//                            Log.e(TAG, "update-h5-view", e);
//                        }
//
//                    }
//                }.start();
            }
        });
//
//        TextView tv = findViewById(R.id.test_text);
//        tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
//        tv.setSingleLine(true);
//        tv.setSelected(true);
//        tv.setFocusable(true);
//        tv.setFocusableInTouchMode(true);
//        tv.setMarqueeRepeatLimit(-1);
//
//        JSONObject obj = new JSONObject();
//        try {
//            obj.put("src", "true");
//            String rr = obj.optString("src", "");
//            boolean r = false;
//            if (!TextUtils.isEmpty(rr)) {
//                if (!"0".equals(rr) && !"false".equalsIgnoreCase(rr)) {
//                    r = true;
//                }
//            }
//            Log.d("wzt-type", "rr:"+rr+", r:"+r);
//        } catch (Exception e) {
//            Log.e("wzt-type", "error", e);
//        }


//        setBorder();

//        String src = "xx <aa <bbb> <bbb> aa> yy";
//
//        String regex = "<[^<>]*(((?'Open'<)[^<>]*)+((?'-Open'>)[^<>]*)+)*(?(Open)(?!))>";
//
//        Pattern r = Pattern.compile("<[^<>]*((\\(?'Open'<)[^<>]*)+(\\(?'-Open'>)[^<>]*\\)+\\)*\\(?(Open)(?!)\\)>");
//        Matcher m = r.matcher(src);
//        while (m.find()) {
//            String key = m.group(1);
//            Log.d("wzt-regex", "key:"+key);
//        }


        ProomDataCenter.getInstance().init();

        testMarquee();
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_LAYOUT:
                ProomRootView rootView = (ProomRootView) msg.obj;
                RelativeLayout container = findViewById(R.id.dynamic_container);
                container.addView(rootView.getView());
                ProomLayoutManager.getInstance().setRootView(rootView);
                mHandler.sendEmptyMessage(MSG_ON_ATTACH);
                break;
            case MSG_ON_ATTACH:
                ProomRootView rView = ProomLayoutManager.getInstance().getRootView();
                if (rView != null) {
                    rView.onAttach();
                }
                break;
            case MSG_UPDATE_VIEW:
                ProomLayoutManager.getInstance().updateViewByData();
                break;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ProomLayoutManager.getInstance().onDestroy();
        ProomDataCenter.getInstance().onDestroy();
    }

    private String readLayout() {
        String result = "";
        try {
            InputStream is = getResources().openRawResource(R.raw.layout);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            result = baos.toString();
            is.close();
        } catch (Exception e) {
            Log.e("wzt-layout", "read layout error", e);
        }

        return result;
    }

    private String readData() {
        String result = "";
        try {
            InputStream is = getResources().openRawResource(R.raw.data);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            result = baos.toString();
            is.close();
        } catch (Exception e) {
            Log.e("wzt-layout", "read layout error", e);
        }

        return result;
    }

    private String readKeyData(int data) {
        String result = "";
        try {
            InputStream is = getResources().openRawResource(data);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            result = baos.toString();
            is.close();
        } catch (Exception e) {
            Log.e("wzt-layout", "read layout error", e);
        }

        return result;
    }

    private void addSubView() {

//        View view = new View(this);
//        view.setBackgroundColor(Color.RED);
//        view.setId(ViewCompat.generateViewId());
//        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(100, 100);
//        lp.rightToRight = containerId;
//        lp.topToTop = containerId;
//        lp.bottomToBottom = containerId;
//        lp.leftToLeft = containerId;
//        view.setLayoutParams(lp);
//
//
//        View subView1 = new View(this);
//        subView1.setBackgroundColor(Color.GREEN);
////        subView1.setId();
//
//        container.addView(view);

        String layout = readLayout();
        try {
            JSONObject jsonObject = new JSONObject(layout);
            ProomRootView rootView = ProomLayoutManager.parseLayout(jsonObject, this);
            Message msg = mHandler.obtainMessage(MSG_LAYOUT);
            msg.obj = rootView;
            mHandler.sendMessage(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setBorder() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.GRAY);
//        drawable.setSize(UIUtils.dp2px(100), UIUtils.dp2px(100));
        drawable.setStroke(UIUtils.dp2px(2), Color.RED);
        drawable.setCornerRadius(UIUtils.dp2px(5));

        findViewById(R.id.test_view).setBackground(drawable);

    }

    SimpleDraweeView imageView;
    private void testImage() {
        if (imageView == null) {
            imageView = new SimpleDraweeView(this);
            imageView.setBackgroundColor(Color.RED);
            int size = UIUtils.dp2px(80);
            int border = UIUtils.dp2px(1);
            int radius = UIUtils.dp2px(15);
            imageView.setLayoutParams(new RelativeLayout.LayoutParams(size, size));
            GenericDraweeHierarchy hierarchy = imageView.getHierarchy();
            if (hierarchy != null) {
//                hierarchy.setFailureImage(R.drawable.icon_bar_full_heart3x);
//                hierarchy.setPlaceholderImage(R.drawable.ic_launcher);
                hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
                RoundingParams params = RoundingParams.fromCornersRadius(radius);
//                params.setBorder(Color.GREEN, border);
                hierarchy.setRoundingParams(params);
            }
            RelativeLayout container = findViewById(R.id.dynamic_container);
            container.addView(imageView);
        }
        UIUtils.displayImage(imageView, "http://p1.qhimg.com/t01e48912fed40b0890.jpg");
    }

    private void emptyImage() {
        if (imageView != null) {
//            imageView.setImageResource(0);
//            imageView.setImageDrawable(null);
            GenericDraweeHierarchy hierarchy = imageView.getHierarchy();
            if (hierarchy != null) {
//                hierarchy.setFailureImage(R.drawable.icon_bar_full_heart3x);
//                hierarchy.setPlaceholderImage(R.drawable.ic_launcher);
                hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
            }
        }
    }

    private void testScale() {
        View container = findViewById(R.id.dynamic_test_layout);
        container.setPivotX(0f);
        container.setPivotY(0f);

        container.setScaleX(1.5f);
        container.setScaleY(1.5f);
    }

    private void testV8() {
        V8 runtime = V8.createV8Runtime();

        runtime.release();
    }

    private void testMarquee() {
        TextView marqueeView = findViewById(R.id.test_marquee);
        String m1 = "aaaaaaaaaaaaaaaaaaaaa";
        String m2 = "bbbbbbbbbbbbbbbbbbbbb";
        String m3 = "ccccccccccccccccccccc";
        String m4 = "ddddddddddddddddddddd";
        String m5 = "eeeeeeeeeeeeeeeeeeeee";
        String blank = "     ";
        StringBuilder sb = new StringBuilder();
        sb.append(m1);
        sb.append(blank);
        sb.append(m2);
        sb.append(blank);
        sb.append(m3);
        sb.append(blank);
        sb.append(m4);
        sb.append(blank);
        sb.append(m5);

        String res = sb.toString();
        SpannableString spanStr = new SpannableString(res);
        spanStr.setSpan(new MarqueeClickSpan(m1), 0, m1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new MarqueeClickSpan(m2), res.indexOf(m2), res.indexOf(m2) + m2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new MarqueeClickSpan(m3), res.indexOf(m3), res.indexOf(m3) + m3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new MarqueeClickSpan(m4), res.indexOf(m4), res.indexOf(m4) + m4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new MarqueeClickSpan(m5), res.indexOf(m5), res.indexOf(m5) + m5.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        marqueeView.setSelected(true);
        marqueeView.setFocusable(true);
        marqueeView.setFocusableInTouchMode(true);
        marqueeView.setClickable(true);
//        marqueeView.setMovementMethod(LinkMovementMethod.getInstance());
        marqueeView.setText(spanStr);


    }

    public static class MarqueeClickSpan extends ClickableSpan {

        private String value;
        public MarqueeClickSpan(String str) {
            this.value = str;
        }

        @Override
        public void onClick(@NonNull View widget) {
            Log.i("click-marquee", "value:"+value);
        }
    }
}
