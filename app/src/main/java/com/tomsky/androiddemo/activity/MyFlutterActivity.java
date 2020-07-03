package com.tomsky.androiddemo.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.tomsky.androiddemo.R;


public class MyFlutterActivity extends FragmentActivity {

    private static final String TAG = "my_flutter";

    private View flutterView1;
    private View flutterView2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flutter);

        findViewById(R.id.flutter_top_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (flutterView1 == null) {
//                    flutterView1 = Flutter.createView(
//                            MyFlutterActivity.this,
//                            getLifecycle(),
//                            "route1"
//                    );
//                    FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                    FrameLayout container = findViewById(R.id.flutter_container_1);
//                    container.addView(flutterView1, layout);
//                } else if (flutterView2 == null) {
//                    flutterView2 = Flutter.createView(
//                            MyFlutterActivity.this,
//                            getLifecycle(),
//                            "route2"
//                    );
//                    FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                    FrameLayout container = findViewById(R.id.flutter_container_2);
//                    container.addView(flutterView2, layout);
//                }
            }
        });

        findViewById(R.id.flutter_bottom_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "flutter click bottom native button");
            }
        });


    }
}
