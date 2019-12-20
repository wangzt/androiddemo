package com.tomsky.androiddemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.dylayout.DyManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by wangzhitao on 2019/12/19
 **/
public class DyLayoutActivity extends FragmentActivity {

    private DyManager dyManager = new DyManager();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dylayout);

        dyManager.setContainerView(findViewById(R.id.dylayout_container), this);

        findViewById(R.id.server_btn).setOnClickListener(v -> {

            String data = readKeyData(R.raw.layout_test);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (jsonObject != null) {
                dyManager.parseFullLayout(jsonObject);
            }

        });

        findViewById(R.id.h5_btn).setOnClickListener(v -> {
            String data = readKeyData(R.raw.h5_prop);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (jsonObject != null) {
                dyManager.invokeLayoutFromH5(jsonObject);
            }
        });
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
}
