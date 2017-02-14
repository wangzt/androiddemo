package com.tomsky.androiddemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.util.StringUtil;
import com.tomsky.androiddemo.view.gridpasswordview.GridPasswordView;

/**
 * Created by j-wangzhitao on 17-1-3.
 */

public class GridPasswordActivity extends FragmentActivity {

    private static final String TAG = "wzt-pwd";
    private GridPasswordView gpvNormalTwice;
    private boolean isFirst;
    private String firstPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordview);

        gpvNormalTwice = (GridPasswordView) findViewById(R.id.grid_pwd_view);

        onPwdChangedTest();
    }

    void onPwdChangedTest(){
        gpvNormalTwice.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
                Log.d(TAG, "====The password is: " + psw);

                if (psw.length() == 5 && isFirst){
                    gpvNormalTwice.clearPassword();
                    isFirst = false;
                    firstPwd = psw;
                }else if (psw.length() == 5 && !isFirst){
                    if (psw.equals(firstPwd)){
                        Log.d(TAG, "The password is: " + psw);
                    }else {
                        Log.d(TAG, "password doesn't match the previous one, try again!");
                        gpvNormalTwice.clearPassword();
                        isFirst = true;
                    }
                }
            }

            @Override
            public void onInputFinish(String psw) { }
        });
    }
}
