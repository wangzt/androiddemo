package com.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by wangzhitao on 2019/12/17
 **/
public class SemaphoreDemo {

    class MyTask implements Runnable {

        private Semaphore semaphore;
        private int user;

        public MyTask(Semaphore semaphore, int user) {
            this.semaphore = semaphore;
            this.user = user;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();

                System.out.print("用户"+user+" 进入窗口，准备买票...\n");
                Thread.sleep((long)Math.random()*10000); // 模拟买票时间
                System.out.println("用户"+ user + "买票完成，准备离开...\n");
                Thread.sleep((long)Math.random()*10000);
                System.out.println("用户"+ user + "离开售票窗口...\n");

                semaphore.release();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void execute() {
        // 定义窗口个数
        final Semaphore s = new Semaphore(2);
        ExecutorService threadPool = Executors.newCachedThreadPool();

        for (int i = 0; i < 20; i++) {
            threadPool.execute(new MyTask(s, i + 1));
        }

        threadPool.shutdown();
    }

    public static void main(String[] args) {
        SemaphoreDemo demo = new SemaphoreDemo();
        demo.execute();
    }
}
