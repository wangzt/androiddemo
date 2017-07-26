package com.tomsky.androiddemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.bean.BaseBean;
import com.tomsky.androiddemo.bean.BeanA;
import com.tomsky.androiddemo.bean.BeanList;
import com.tomsky.androiddemo.bean.WraperBean;
import com.tomsky.androiddemo.util.LogUtils;

/**
 * Created by j-wangzhitao on 17-7-25.
 */

public class CreaterActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Intent intent = getIntent();
        if (intent != null) {
            BeanList list = intent.getParcelableExtra("my_list");
            if (list != null && list.beans != null) {
                for (BaseBean bean: list.beans) {
                    LogUtils.d("wzt-bean", "=="+bean.toString());
                }
            }

            BeanA singleA = intent.getParcelableExtra("single_a");
            LogUtils.d("wzt-bean", "--------"+singleA);

            WraperBean wraperBean = intent.getParcelableExtra("wraper_bean");
            LogUtils.d("wzt-bean", "=========="+wraperBean);
        }
    }
}
