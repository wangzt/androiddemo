package com.tomsky.androiddemo.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;

import com.tomsky.androiddemo.BuildConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 单线程任务执行者,循环利用线程,避免频繁创建线程
 */
public class JobWorker {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 5;
    private static final int KEEP_ALIVE = 1;

    private static class ExecutorHolder {
        static ExecutorService EXECUTOR_IO = Executors.newFixedThreadPool(MAXIMUM_POOL_SIZE);
        static ExecutorService EXECUTOR_SINGLE = Executors.newSingleThreadExecutor();
        static Handler HANDLER = new Handler(Looper.getMainLooper());
    }

    public static <T> void submit(Task<T> task) {
        submit_IO(task);
    }

    public static <T> void submit_IO(Task<T> task) {
        if (ExecutorHolder.EXECUTOR_IO.isShutdown()) {
            return;
        }
        ExecutorHolder.EXECUTOR_IO.submit(task);
    }

    public static <T> void submit_SingleThread(Task<T> task) {
        if (ExecutorHolder.EXECUTOR_SINGLE.isShutdown()) {
            return;
        }
        ExecutorHolder.EXECUTOR_SINGLE.submit(task);
    }

    public static Future submit_IO(Runnable runnable) {
        if (ExecutorHolder.EXECUTOR_IO.isShutdown()) {
            return null;
        }
        return ExecutorHolder.EXECUTOR_IO.submit(runnable);
    }

    /**
     * 主线程执行
     * @param action
     * @param <T>
     */
    public static <T> void submitOnUiThread(Task<T> action) {
        if (ThreadUtils.getCurThreadId() == Looper.getMainLooper().getThread().getId()) {
            action.run();
        } else {
            ExecutorHolder.HANDLER.post(action);
        }
    }

//    public static <T> void submit_CPU(Task<T> task) {
//        if (ExecutorHolder.EXECUTOR_CPU.isShutdown()) {
//            return;
//        }
//        ExecutorHolder.EXECUTOR_CPU.submit(task);
//    }

    public static void shutdown() {
        ExecutorHolder.EXECUTOR_IO.shutdown();
//        ExecutorHolder.EXECUTOR_CPU.shutdown();
    }

    public static abstract class Task<T> implements Runnable {
        private boolean needCallback = true;

        protected Task() {
            this(true);
        }

        public Task(boolean needCallback) {
            this.needCallback = needCallback;
        }


        @UiThread
        public void onStart() {
            //empty for override
        }

        @WorkerThread
        public T doInBackground() {
            return null;
        }

        @Override
        public final void run() {
            if (needCallback) {
                ExecutorHolder.HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        onStart();
                    }
                });
            }
            T result = null;
            if(BuildConfig.DEBUG){
                result = doInBackground();
            } else {
                try {
                    result = doInBackground();
                } catch (Exception e) {
                    Log.e("jobworker", "error", e);
                }
            }
            if (needCallback) {
                final T callbackResult = result;
                ExecutorHolder.HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        onComplete(callbackResult);
                    }
                });
            }
        }

        @UiThread
        public void onComplete(T result) {
            //empty for override
        }
    }


}
