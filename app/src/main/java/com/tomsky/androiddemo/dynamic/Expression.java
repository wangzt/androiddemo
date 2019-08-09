package com.tomsky.androiddemo.dynamic;

import android.util.Log;

import com.tomsky.androiddemo.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Expression {

    private static final String TAG = "wzt-exp";
    private String src; // sync:p_game.scores[uid=$sync:p_user[pos=0].uid].nickname  原始数据 sync.p_game.scores.2.nickname

    private StackFrame mStackFrame;

    public Expression(String src) {
        this.src = src;
    }

    public void parse(JSONObject data) {
//        String[] splits = src.split(":|\\.|\\[|\\]|\\=");
//        for (String str: splits) {
//            Log.d(TAG, str);
//        }


        StackFrame currentFrame = new StackFrame();
        StringBuilder sb = new StringBuilder();

        Stack<StackFrame> stack = new Stack<>();


        char[] keyArray = src.toCharArray();
        for (char c:keyArray) {
            if (':' == c) {
                currentFrame.add(sb.toString());
                sb = new StringBuilder();
            } else if ('.' == c) {
                if (sb.length() > 0) {
                    currentFrame.add(sb.toString());
                    sb = new StringBuilder();
                }
            } else if ('[' == c) {
                currentFrame.add(sb.toString());
                stack.push(currentFrame);
                currentFrame = new StackFrame();
                sb = new StringBuilder();
            }  else if ('=' == c) {
                currentFrame.key = sb.toString();
                sb = new StringBuilder();
            }  else if ('$' == c) {
                currentFrame.shouldCalc = true;
            } else if (']' == c) {
                String value = sb.toString();
                if (!currentFrame.shouldCalc) {
                    currentFrame.value = value;
                    StackFrame stackFrame = stack.pop();
                    if (stackFrame.shouldCalc || currentFrame.key == null) {
                        stackFrame.parseJSON(data, currentFrame);
                    }
                    currentFrame = stackFrame;
                } else {
                    currentFrame.add(value);
                    currentFrame.calcValue(data);
                    if (!stack.isEmpty()) {
                        StackFrame stackFrame = stack.pop();
                        stackFrame.parseJSON(data, currentFrame);
                        currentFrame = stackFrame;
                    }
                }
                sb = new StringBuilder();

            } else {
                sb.append(c);
            }

        }

        if (sb.length() > 0) {
            currentFrame.add(sb.toString());
        }

        mStackFrame = currentFrame;

        if (mStackFrame != null) {
            Log.d(TAG, mStackFrame.propString());
            Log.d(TAG, "result:"+mStackFrame.calcValue(data));
        }

    }


    public class StackFrame {
        boolean shouldCalc = false;
        String key;
        String value;
        List<String> propList = new ArrayList<>();

        public int size() {
            return propList.size();
        }

        public void add(String key) {
            propList.add(key);
        }

        public void parseJSON(JSONObject dataObject, StackFrame subFrame) {
            JSONObject jsonObject = dataObject;
            String val = "";
            for (String prop: propList) {
                Object obj = jsonObject.opt(prop);
                if (obj instanceof JSONObject) {
                    jsonObject = (JSONObject) obj;
                } else if (obj instanceof JSONArray) {
                    if (subFrame == null) break;
                    String key = subFrame.key;
                    String value = subFrame.getValue();
                    if (value == null) break;

                    if (key == null) { // value[2]
                        val = value;
                        break;
                    } else { // value[key=2]
                        JSONArray array = (JSONArray)obj;
                        int size = array.length();
                        for (int i = 0; i < size; i++) {
                            JSONObject subObj = array.optJSONObject(i);
                            if (subObj != null && subObj.has(key)) {
                                if (value.equals(subObj.optString(key))) {
                                    val = ""+i;
                                    break;
                                }
                            }
                        }
                    }

                }
            }

            if (!"".equals(val)) {
                propList.add(val);
            }
        }

        public String calcValue(JSONObject dataObject) {
            Object obj = dataObject;
            int len = propList.size();
            try {
                for (int i = 0; i < len; i++) {
                    String prop = propList.get(i);
                    if (obj instanceof JSONObject) {
                        obj = ((JSONObject)obj).opt(prop);
                    } else if (obj instanceof JSONArray) {
                        int index = Integer.valueOf(prop);
                        obj = ((JSONArray)obj).opt(index);
                    } else if (i == len - 1) {
                        value = String.valueOf(obj);
                    }
                }
                if (value == null && obj != null) {
                    value = String.valueOf(obj);
                }
            } catch (Exception e) {
                Log.e(TAG, "error", e);
            }


            return value;
        }

        public String getValue() {
            return value;
        }

        public String propString() {
            return StringUtil.listToString(propList, '.');
        }

    }

}
