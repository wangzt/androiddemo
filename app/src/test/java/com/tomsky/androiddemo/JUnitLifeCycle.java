package com.tomsky.androiddemo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by j-wangzhitao on 17-6-13.
 */

public class JUnitLifeCycle {
    @Before
    public void init(){
        System.out.println("------method init called------");
    }

    @BeforeClass
    public static void prepareDataForTest(){
        System.out.println("------method prepareDataForTest called------\n");
    }

    @Test
    public void test1(){
        System.out.println("------method test1 called------");
    }

    @Test
    public void test2() {
        System.out.println("------method test2 called------");
    }

    @Test
    public void test3() {
        System.out.println("------method test3 called------");
    }

    @After
    public void clearDataForTest(){
        System.out.println("------method clearDataForTest called------");
    }

    @AfterClass
    public static void finish(){
        System.out.println("------method finish called------");
    }
}
