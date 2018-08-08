package com.dagger2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tomsky.androiddemo.R;

import javax.inject.Inject;

public class MyActivity extends AppCompatActivity {

    @Inject
    A a1;

    @Inject
    A a2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        DaggerMainComponent.create().inject(this);

//        DaggerMainComponent.builder().build().inject(this);

//        a.eat();
        Log.d("wzt-dagger", "a1:"+a1);
        Log.d("wzt-dagger", "a2:"+a2);
    }
}
