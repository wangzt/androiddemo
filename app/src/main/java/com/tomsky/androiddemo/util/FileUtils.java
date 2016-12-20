package com.tomsky.androiddemo.util;

import android.content.Context;
import android.os.Environment;

import com.tomsky.androiddemo.BaseApplication;

import java.io.File;

/**
 * Created by j-wangzhitao on 16-12-20.
 */

public class FileUtils {

    private static String BASE_ROOT_PATH = null;

    public static String getRootPath(){
        if(BASE_ROOT_PATH == null){
            initRootPath();
        }
        return BASE_ROOT_PATH;
    }

    private static void initRootPath() {
        Context context = BaseApplication.getContext();
        if (BASE_ROOT_PATH == null) {
            File file = null;
            try {
                file = Environment.getExternalStorageDirectory();
                if (file.exists() && file.canRead() && file.canWrite()) {
                    //如果可读写，则使用此目录
                    String path = file.getAbsolutePath();
                    if (path.endsWith("/")) {
                        BASE_ROOT_PATH = file.getAbsolutePath() + "tomsky/";
                    } else {
                        BASE_ROOT_PATH = file.getAbsolutePath() + "/tomsky/";
                    }
                }
            } catch (Exception e) {

            }
            if (BASE_ROOT_PATH == null) {
                //如果走到这里，说明外置sd卡不可用
                if (context != null) {
                    file = context.getFilesDir();
                    String path = file.getAbsolutePath();
                    if (path.endsWith("/")) {
                        BASE_ROOT_PATH = file.getAbsolutePath() + "tomsky/";
                    } else {
                        BASE_ROOT_PATH = file.getAbsolutePath() + "/tomsky/";
                    }
                } else {
                    BASE_ROOT_PATH = "/sdcard/tomsky/";
                }
            }
        }
        File file = new File(BASE_ROOT_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static String getCapturePath() {
        String capturePath = getRootPath() + "capture/";
        File file = new File(capturePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return capturePath;
    }
}
