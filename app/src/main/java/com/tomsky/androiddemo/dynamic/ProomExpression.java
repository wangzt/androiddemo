package com.tomsky.androiddemo.dynamic;

import android.util.Log;

import com.tomsky.androiddemo.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ProomExpression {

    private static final String TAG = "wzt-exp";

    private String prop;
    private String src; // sync:p_game.scores[uid=$sync:p_user[pos=0].uid].nickname  原始数据 sync.p_game.scores.2.nickname

    private String value; // 最终算出来的值

    private List<String> observable = new ArrayList<>();

    public ProomExpression(String key, String src) {
        this.prop = key;
        this.src = src;
    }

    public String getProp() {
        return prop;
    }

    public String getValue() {
        return value;
    }

    public ProomExpression parseKey() {
        char[] keyArray = src.toCharArray();
        StringBuilder sb = new StringBuilder();
        boolean isKey = false;
        for (char c:keyArray) {
            if (':' == c) {
                sb = new StringBuilder();
                isKey = true;
            } else if ('.' == c) {
                if (sb.length() > 0) {
                    if (isKey) {
                        observable.add(sb.toString());
                    }
                    sb = new StringBuilder();
                }
                isKey = false;
            } else if ('[' == c) {
                if (sb.length() > 0) {
                    if (isKey) {
                        observable.add(sb.toString());
                    }
                    sb = new StringBuilder();
                }
                isKey = false;
            } else if (']' == c) {

            } else if ('$' == c) {

            } else {
                sb.append(c);
            }
        }

        return this;
    }

    public void parseValue(JSONObject data) {
        if (data == null) return;

        StackFrame currentFrame = new StackFrame();
        StringBuilder sb = new StringBuilder();

        Stack<StackFrame> stack = new Stack<>();

        boolean canCalc = true;

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
                    if (!currentFrame.canCalc) {
                        canCalc = false;
                        break;
                    }
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



        if (canCalc) {
            if (currentFrame != null) {
                String result = currentFrame.calcValue(data);
                Log.d(TAG, currentFrame.propString()+" : " + result);
                value = result;
            }
        }

    }

    public boolean hasKeyChanged(String key) {
        return observable.contains(key);
    }

    public static class StackFrame {
        boolean canCalc = true;
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
            if (dataObject == null) {
                canCalc = false;
                return;
            }
            canCalc = true;
            JSONObject jsonObject = dataObject;
            String val = "";
            for (String prop: propList) {
                Object obj = jsonObject.opt(prop);
                if (obj == null) {
                    canCalc = false;
                    break;
                }
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

            if (!"".equals(val) && canCalc) {
                propList.add(val);
            }
        }

        public String calcValue(JSONObject dataObject) {
            if (canCalc) {
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
