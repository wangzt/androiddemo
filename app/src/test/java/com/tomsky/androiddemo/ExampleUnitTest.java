package com.tomsky.androiddemo;

import org.junit.Test;

import master.flame.danmaku.danmaku.util.SystemClock;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        System.out.println("------addition_isCorrect called------");
        assertEquals(4, 2 + 2);
    }

    @Test
    public void method2() {
        System.out.println("---------------method2 called---------------");
    }
}