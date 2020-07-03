package com.tomsky.androiddemo.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.widget.RecyclerPannel;

/**
 * Created by j-wangzhitao on 16-12-2.
 */

public class RecycleActivity extends FragmentActivity {

    private RecyclerPannel mPannel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);

        String value = getIntent().getStringExtra("my_extra_2999");
        Log.d("wzt-extra", "value:"+value);
        findViewById(R.id.show_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPannel == null) {
                    mPannel = new RecyclerPannel(RecycleActivity.this);
                }
                mPannel.show();
            }
        });
    }

}
