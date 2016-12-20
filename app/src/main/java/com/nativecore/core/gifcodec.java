package com.nativecore.core;

import java.nio.ByteBuffer;

public class gifcodec {

    private static final String TAG = "gifcodec";

    static {
        System.loadLibrary("gifproc");
    }

    public gifcodec() {
    }

    public int encode(byte[] buf, int i_nWidth, int i_nHeight, int i_nPitch, long i_nPts, int i_nFlag) {
        return encodex(buf, i_nWidth, i_nHeight, i_nPitch, i_nPts, i_nFlag);
    }

    public native int init(String jFileUrl, int i_nWidth, int i_nHeight, int i_nFormat);

    public native int seteof(int i_nWidth, int i_nHeight, long i_nPts, int i_nFlag);

    public native int release();

    public native int encode(ByteBuffer jbuf, int i_nWidth, int i_nHeight, int i_nPitch, long i_nPts, int i_nFlag);

    private native int encodex(byte[] buf, int i_nWidth, int i_nHeight, int i_nPitch, long i_nPts, int i_nFlag);

}