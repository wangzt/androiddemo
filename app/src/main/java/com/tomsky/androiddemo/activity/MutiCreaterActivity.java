package com.tomsky.androiddemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.bean.BaseBean;
import com.tomsky.androiddemo.bean.BeanA;
import com.tomsky.androiddemo.bean.BeanB;
import com.tomsky.androiddemo.bean.BeanC;
import com.tomsky.androiddemo.bean.BeanList;
import com.tomsky.androiddemo.bean.WraperBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j-wangzhitao on 17-7-25.
 */

public class MutiCreaterActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_creater);

        findViewById(R.id.test_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testCreater();
            }
        });
    }

    private void testCreater() {
        List<BaseBean> beans = new ArrayList<>();

        BeanA a = new BeanA();
        a.valA = "valA";
        a.priceA = 10;

        BeanB b = new BeanB();
        b.valB = true;
        b.pB = "pmB";

        BeanC c = new BeanC();
        c.valC = "valueC";
        c.pC = false;

        beans.add(a);
        beans.add(b);
        beans.add(c);

        BeanList beanList = new BeanList();
        beanList.beans = beans;

        BeanA singleA = new BeanA();
        singleA.valA = "singleA";
        singleA.priceA = 200;

        WraperBean wraperBean = new WraperBean();
        wraperBean.name = "MyWraper";
        BeanB innerB = new BeanB();
        innerB.valB = false;
        innerB.pB = "innerB";
        wraperBean.baseBean = innerB;

        Intent intent = new Intent();
        intent.setClass(this, CreaterActivity.class);
        intent.putExtra("my_list", beanList);
        intent.putExtra("single_a", singleA);
        intent.putExtra("wraper_bean", wraperBean);
        startActivity(intent);
    }


}
