package com.test;

import java.util.concurrent.FutureTask;

/**
 * Created by j-wangzhitao on 17-1-26.
 */

public class MyThread {

    static class Thread1 extends Thread {
        private int count;

        @Override
        public void run() {
            while (count <= 10) {
                System.out.print("Counter:"+count+" \n");
                count++;
            }
            System.out.println();
        }
    }

    static class Thread2 extends Thread {
        private int count;

        @Override
        public void run() {
            while (count <= 10) {
                System.out.print("-----i:"+count+" \n");
                count++;
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
//        Thread1 t1 = new Thread1();
//        Thread t2 = new Thread2();
//
//        try {
//            t1.start();
//            t1.join(); // join() 方法是让调用该方法的主线程执行run()时暂时卡住，等run()执行完成后， 主线程再调用执行join()后面的代码
//            t2.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        FutureTask
    }
}
