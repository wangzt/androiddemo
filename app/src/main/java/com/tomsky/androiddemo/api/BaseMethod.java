package com.tomsky.androiddemo.api;

import com.tomsky.androiddemo.util.LogUtils;

/**
 * Created by j-wangzhitao on 17-9-27.
 */

public class BaseMethod {

    public static void printBaseBean(BaseBean bean) {
        LogUtils.d("wzt-api", "bean:"+bean+", uid:"+bean.getUid()+", uname:"+bean.getUname());
    }
    
}
