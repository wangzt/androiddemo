package com.tomsky.androiddemo.activity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.dynamic.ProomLayoutManager;
import com.tomsky.androiddemo.dynamic.virtualview.ProomRootView;
import com.tomsky.androiddemo.util.UIUtils;
import com.tomsky.androiddemo.util.WeakHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class DynamicUIActivity extends FragmentActivity implements WeakHandler.IHandler {

//    private ConstraintLayout container;
//    private int containerId = R.id.dynamic_container;

    private boolean hasAdd = false;

    private WeakHandler mHandler = new WeakHandler(this);
    private static final int MSG_LAYOUT = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);
//        container = findViewById(R.id.dynamic_container);
//
        findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasAdd) return;
                hasAdd = true;

                new Thread() {
                    @Override
                    public void run() {
                        addSubView();
                    }
                }.start();
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
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == MSG_LAYOUT) {
            ProomRootView rootView = (ProomRootView) msg.obj;
            RelativeLayout container = findViewById(R.id.dynamic_container);
            container.addView(rootView.getView());
        }
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
}
