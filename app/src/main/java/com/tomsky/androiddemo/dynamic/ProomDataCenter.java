package com.tomsky.androiddemo.dynamic;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ProomDataCenter {

    private static ProomDataCenter instance;

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

    public static void parseKey(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String key = "sync:p_game.scores[2].cc.value[1]";
//            String key = "sync:p_game.scores[uid=$sync:p_user[pos=2].uid].cc.value[1]";
//            String key = "sync:p_game.scores[uid=$sync:p_user[pos=2].uid].nickname";
            Expression exp = new Expression(key);
            exp.parse(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



}
