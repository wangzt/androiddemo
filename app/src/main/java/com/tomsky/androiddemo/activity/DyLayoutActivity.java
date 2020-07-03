package com.tomsky.androiddemo.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.dylayout.DyManager;
import com.tomsky.androiddemo.dylayout.virtual.DyExpression;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by wangzhitao on 2019/12/19
 **/
public class DyLayoutActivity extends FragmentActivity {

    private static final String TAG = "dy_layout";

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

        findViewById(R.id.expression_btn).setOnClickListener(v -> {
            testExpression();
        });
    }

    private void testExpression() {
        try {
            JSONObject dataObj = new JSONObject();

            JSONObject syncObj = new JSONObject();
            dataObj.put("sync", syncObj);
            JSONObject gameObj = new JSONObject(readKeyData(R.raw.p_game));
            JSONObject userObj = new JSONObject(readKeyData(R.raw.p_user));
            syncObj.put("p_game", gameObj.optJSONObject("p_game"));
            syncObj.put("p_user", userObj.optJSONArray("p_user"));

            JSONObject apiObj = new JSONObject();
            JSONObject feedObj = new JSONObject();
            JSONObject relayObj = new JSONObject();

            dataObj.put("api", apiObj);
            apiObj.put("feedinfo", feedObj);
            feedObj.put("relay", relayObj);
            relayObj.put("sn", "sn1234353452345");

            String src = "sync(proomid):p_game.scores[uid=$sync(liveid):p_user[pos=1].uid].nickname";
//            String src = "api:feedinfo.relay.sn";
            DyExpression expression = new DyExpression("text", src);
            expression.parseKey();
            expression.parseValue(dataObj);

            Log.i(TAG, "value:"+expression.getValue()+", sync: "+expression.getSyncObservable()+", api: "+expression.getApiObservable());
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
