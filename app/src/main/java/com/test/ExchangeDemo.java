package com.test;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wangzhitao on 2019/12/17
 **/
public class ExchangeDemo {
    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();
        ExecutorService threadPool = Executors.newCachedThreadPool();

        threadPool.execute(() -> {
            String renzhi = "B";
            try {
                String money = exchanger.exchange(renzhi);
                System.out.println("绑架者用B交换回："+money);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        threadPool.execute(() -> {
            String money = "100万";
            try {
                String renzhi = exchanger.exchange(money);
                System.out.println("A用100万换回："+renzhi);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        threadPool.shutdown();
    }
}
