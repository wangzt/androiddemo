package com.test;

import java.util.concurrent.CountDownLatch;

/**
 * Created by wangzhitao on 2019/12/17
 **/
public class CountDownLatchDemo {

    public static void main(String[] args) {
        final CountDownLatch latch = new CountDownLatch(3);

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep((long)Math.random()*10000);
                    System.out.println("子任务B"+Thread.currentThread().getName()+"正在执行");
                    Thread.sleep((long)Math.random()*10000);
                    System.out.println("子任务B"+Thread.currentThread().getName()+"执行完毕");
                    // 倒计时减掉1
                    latch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep((long)Math.random()*10000);
                    System.out.println("子任务C"+Thread.currentThread().getName()+"正在执行");
                    Thread.sleep((long)Math.random()*10000);
                    System.out.println("子任务C"+Thread.currentThread().getName()+"执行完毕");
                    // 倒计时减掉1
                    latch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep((long)Math.random()*10000);
                    System.out.println("子任务D"+Thread.currentThread().getName()+"正在执行");
                    Thread.sleep((long)Math.random()*10000);
                    System.out.println("子任务D"+Thread.currentThread().getName()+"执行完毕");
                    // 倒计时减掉1
                    latch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        // main 线程为主任务A
        System.out.println("等待3个子任务执行完毕"+Thread.currentThread().getName()+"主任务才开始执行");

        try {
            // 等待子任务执行完毕 此时阻塞
            latch.await();
            System.out.println("说明BCD三个子任务已经执行完毕");
            // 继续执行主任务
            System.out.println("继续执行主任务：" + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
