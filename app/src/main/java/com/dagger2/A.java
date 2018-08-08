package com.dagger2;

import android.util.Log;

public class A {

    private B b;

    public A() {
    }

    public A(B b) {
        this.b = b;
    }

    public void eat() {
        if (b != null) {
            b.eat();
        } else {
            Log.d("wzt-dagger", "A, 该吃饭了");
        }
    }
}
