package com.tomsky.androiddemo.dynamic;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class ProomDataCenter {

    private static final String TAG = "ProomDataCenter";

    private static ProomDataCenter instance;

    private JSONObject data;

    private JSONObject syncData;

    private Set<ProomDataObsever> obsevers = new HashSet<>();

    private ProomDataCenter() {
    }

    public static ProomDataCenter getInstance() {
        if (instance == null) {
            synchronized (ProomDataCenter.class) {
                if (instance == null) {
                    instance = new ProomDataCenter();
                }
            }
        }

        return instance;
    }

    public JSONObject getData() {
        return data;
    }

    public boolean addSyncData(String key, JSONObject jsonObject) {
        boolean result = false;
        if (data == null || syncData == null) {
            return result;
        }

        if (TextUtils.isEmpty(key) || jsonObject == null) {
            return result;
        }
        try {
            syncData.put(key, jsonObject);
            onDataChanged(key);
            result = true;
        } catch (Exception e) {
            Log.e(TAG, "addSyncData-obj", e);
        }

        return result;
    }

    public boolean addSyncData(String key, JSONArray jsonArray) {
        boolean result = false;
        if (data == null || syncData == null) {
            return result;
        }
        if (TextUtils.isEmpty(key) || jsonArray == null) {
            return result;
        }
        try {
            syncData.put(key, jsonArray);
            onDataChanged(key);
            result = true;
        } catch (Exception e) {
            Log.e(TAG, "addSyncData-array", e);
        }
        return result;
    }

    public void addObserver(ProomDataObsever obsever) {
        obsevers.add(obsever);
    }

    public void clearObserver() {
        obsevers.clear();
    }

    public void removeObserver(ProomDataObsever obsever) {
        obsevers.remove(obsever);
    }

    private void onDataChanged(String key) {
        for (ProomDataObsever obsever: obsevers) {
            obsever.onDataChanged(key);
        }
    }

    public void init() {
        data = new JSONObject();
        syncData = new JSONObject();
        try {
            data.put("sync", syncData);
        } catch (Exception e) {
            Log.e(TAG, "init-error", e);
        }
    }

    public void onDestroy() {
        data = null;
        syncData = null;
        obsevers.clear();
    }

    public static void parseKey(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String src = "sync:p_test.user.anchor.avatar";
//            String key = "sync:p_game.scores[2].cc.value[1]";
//            String key = "sync:p_game.scores[uid=$sync:p_user[pos=2].uid].cc.value[1]";
//            String key = "sync:p_game.scores[uid=$sync:p_user[pos=2].uid].nickname";
            Expression exp = new Expression("aa", src);
            exp.parseValue(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



}
