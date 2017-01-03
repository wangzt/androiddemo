package com.tomsky.androiddemo.util;

import android.graphics.Bitmap;
import android.util.Log;

import com.nativecore.core.gifcodec;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by j-wangzhitao on 16-12-20.
 */

public class GifRecorder {

    private boolean DEBUG = true;
    private static final String TAG = "wzt-gif";

    public static final int GIF_MAX_WIDTH = 360;

    public static final int CAPTURE_QUEUE_SIZE = 5;

    private LinkedBlockingQueue<Frame> captureQueue = new LinkedBlockingQueue<>(CAPTURE_QUEUE_SIZE);

    private String gifFilePath;
    private int targetWidth, targetHeight;

    private EncodeThread encodeThread;
    public GifRecorder() {
        gifFilePath = FileUtils.getCapturePath() +  "capture.gif";
    }

    public Bitmap scaleBitmap(Bitmap bmp) {
        if (bmp.getWidth() > GIF_MAX_WIDTH) {
            int width = GIF_MAX_WIDTH;
            int height = (int) (1f * GIF_MAX_WIDTH * bmp.getHeight() / bmp.getWidth());
            return Bitmap.createScaledBitmap(bmp, width, height, true);
        }
        return bmp;
    }

    public void onImageAvailable(Bitmap bmp) {
        int bytes = bmp.getByteCount();
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        ByteBuffer buf = ByteBuffer.allocate(bytes);
        bmp.copyPixelsToBuffer(buf);


        targetWidth = width;
        targetHeight = height;

        int stride = width * 4;
        Frame frame = new Frame()
                .setBuffer(buf)
                .setWidth(width)
                .setHeight(height)
                .setRowStride(stride)
                .setTs(System.currentTimeMillis());
        captureQueue.offer(frame);

    }

    public void start() {
        if (encodeThread != null) {
            encodeThread.exit();
        }

        encodeThread = new EncodeThread();
        encodeThread.start();
    }

    public void stop() {
        if (encodeThread != null) {
            encodeThread.exit();
        }
        captureQueue.clear();
    }


    /**
     * 编码线程
     */
    private class EncodeThread extends Thread {
        private int count = 0;
        @Override
        public void run() {
//            final File gifFile = new File(FileUtils.getCapturePath(), System.currentTimeMillis() + ".gif");
//            gifFilePath = gifFile.getAbsolutePath();

            try {
//                if (!gifFile.exists()) {
//                    gifFile.createNewFile();
//                }
                gifcodec gifCodec = new gifcodec();
                gifCodec.init(gifFilePath, targetWidth, targetHeight, 0);

                long ts = 0;
                while (true) {
                    log(" encodeQueue size =" + captureQueue.size());
                    if ((count >= CAPTURE_QUEUE_SIZE || stop) && captureQueue.isEmpty()) {
                        break;
                    }
//                    if (gifFile.length() + getFrameSize() > maxFileSize) {
//                        log("reach maxFileSize ,exit.");
//                        reachMaxFileSize();
//                        break;
//                    }
                    try {
                        log(" GifEncodeThread try to take ByteBuffer from encodeQueue");
                        Frame frame = captureQueue.take();
                        log(" GifEncodeThread encode ");

                        count++;
                        gifCodec.encode(frame.getBuffer(), frame.getWidth(), frame.getHeight(), frame.getRowStride(), frame.getTs(), 0);

                        frame.setBuffer(null);
//                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                    }
                }
                //finishEncode();
                //callback

                if (gifCodec.release() == 0) {
                    gifCodec.seteof(targetWidth, targetHeight, ts, 0);
//                    sendResult(Status.OK, gifFilePath);
                    log("================== GifEncodeThread encode success====================");
                } else {
//                    sendResult(Status.ENCODE_FAILED, null);
                    log("================== GifEncodeThread encode failed====================");
                }

            } catch (Exception e) {
                //
            }
        }


//        private void reachMaxFileSize() {
//            started.set(false);
//            encodeQueue.clear();
//            captureQueue.clear();
//            workHandler.removeCallbacks(autoStopTask);//如果是手动停止,remove 自动停止
//            if (processThread != null) {
//                processThread.forceExit();
//            }
//
//            releaseMediaProjection();
//            lastTs = 0;
//        }

        private volatile boolean stop = false;

        private void exit() {
            if (captureQueue.isEmpty()) {
                this.stop = true;
                this.interrupt();
            } else {
                this.stop = true;
            }
        }

        private void waitFor(final Thread thread) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    exit();
                }
            }).start();
        }
    }

    private static class Frame {
        private int rowStride;
        private int width;
        private int height;
        private ByteBuffer buffer;
        private long ts;

        public int getRowStride() {
            return rowStride;
        }

        public Frame setRowStride(int rowStride) {
            this.rowStride = rowStride;
            return this;
        }

        public int getWidth() {
            return width;
        }

        public Frame setWidth(int width) {
            this.width = width;
            return this;
        }

        public int getHeight() {
            return height;
        }

        public Frame setHeight(int height) {
            this.height = height;
            return this;
        }

        public ByteBuffer getBuffer() {
            return buffer;
        }

        public Frame setBuffer(ByteBuffer buffer) {
            this.buffer = buffer;
            return this;
        }

        public long getTs() {
            return ts;
        }

        public Frame setTs(long ts) {
            this.ts = ts;
            return this;
        }

        @Override
        public String toString() {
            return "Frame{" +
                    "rowStride=" + rowStride +
                    ", targetWidth=" + width +
                    ", targetHeight=" + height +
                    ", buffer=" + buffer +
                    ", ts=" + ts +
                    '}';
        }
    }

    private void log(String format, Object... args) {
        if (DEBUG) {
            Log.d(TAG, String.format(Locale.getDefault(), format, args));
        }

    }
}
