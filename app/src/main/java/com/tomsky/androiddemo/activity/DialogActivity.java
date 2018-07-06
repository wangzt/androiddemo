package com.tomsky.androiddemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.tomsky.androiddemo.R;

public class DialogActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity);

        View rootView = findViewById(R.id.root_layout);
        ViewGroup.LayoutParams rlp = rootView.getLayoutParams();
        int width = getResources().getDisplayMetrics().widthPixels;
        int maxWidth = width;
        rlp.width = maxWidth;
        rlp.height = maxWidth;
        rootView.setLayoutParams(rlp);

        findViewById(R.id.btn_close).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                finish();
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.scale_out);
    }
}
