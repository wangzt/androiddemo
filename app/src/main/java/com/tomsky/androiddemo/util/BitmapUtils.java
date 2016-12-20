package com.tomsky.androiddemo.util;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by j-wangzhitao on 16-12-20.
 */

public class BitmapUtils {

    public static boolean writeToFile(Bitmap bitmap, File outFile, int quality, boolean recycle) {
        boolean success = false;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outFile);
            success = bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
            out.close();
        } catch (IOException e) {
            // success is already false
        } finally {
            try {
                if (out != null) {
                    out.close();
                    if (recycle) {
                        bitmap.recycle();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return success;
    }
}
