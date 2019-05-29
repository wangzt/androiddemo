package com.tomsky.androiddemo.util;

import android.net.Uri;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
    public static void matchDollar() {
        Map<String, String> map = new HashMap<>();
        map.put("user_name", "wangzhitao");
        map.put("date", "2019-1-3");

        StringBuffer sb = new StringBuffer();
//        String line = "start${user_name}生日快乐${date}end";
        String line = "${user_name}生日快乐${date}end";
        int appendPos = 0;
        int start = 0;
        int end = 0;
        int lineSize = line.length();

        String pattern = "\\$\\{([^}]*)\\}";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        while (m.find()) {
            start = m.start();
            end = m.end();
            Log.d("wzt-regex", "start:"+start+", end:"+end);
            sb.append(line.substring(appendPos, start));
            appendPos = end;
            String key = m.group(1);
            String value = map.get(key);
            if (value != null) {
                sb.append(value);
            } else {
                sb.append("${"+key+"}");
            }
        }
        Log.d("wzt-regex", "appendPos:"+appendPos+", size:"+lineSize);
        if (appendPos < lineSize) {
            sb.append(line.substring(appendPos));
        }
        Log.d("wzt-regex", "result:"+sb);

//        String nameReg = "${user_name}";
//        String dateReg = "\\$\\{date\\}";
//        Log.d("wzt-regex", "contain:" + line.contains(nameReg));
//        Log.d("wzt-regex", "replace:" + line.replaceAll(dateReg, "2019-1-3"));

//        String photo1 = "${user_photo}";
//        String photo2 = "${anchor_photo}";
//        Log.d("wzt-regex", "1:"+photo1.equals("${user_photo}") +", 2:"+photo2.equals("${anchor_photo}"));
    }

    public static void testComma() {
        String str = "你好啊,我很好,Ta也很好";
        String repStr = str.replace(",", "\\,");
        String uriExp = Uri.encode(str);
        Log.d("wzt-comma", "repStr:"+repStr);
        String repDecode = repStr.replace("\\," ,",");
        Log.d("wzt-comma", "repDecode:"+repDecode);

        Log.d("wzt-comma", "uriExp:"+uriExp);
        String uriDecode = Uri.decode(uriExp);
        Log.d("wzt-comma", "uriDecode:"+uriDecode);


    }
}
