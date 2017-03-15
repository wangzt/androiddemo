package com.tomsky.androiddemo.camera;

public class CameraConfig
{
    public int DEFULT_WIDTH = 1280;//预览尺寸
    public int DEFULT_HEIGHT = 720;//预览尺寸
    public int DEFULT_VIDEO_WIDTH = 360;//视频尺寸
    public int DEFULT_VIDEO_HEIGHT = 640;//视频尺寸

    public int width = 1280;//预览尺寸
    public int height = 720;//预览尺寸
    public int videoWidth = 360;//视频尺寸
    public int videoHeight = 640;//视频尺寸
    public int fps = 15;//帧率
    public int bitrate = 600*1024;//码率
    public int turnOnCABAC = 1;//编码是否开启CABAC

    /**
     * 硬编码需要对齐32像素
     * @param px 原始像素
     * @return 对齐后的像素
     */
    public synchronized  static int align32Pixels(int px){
        int dec = px;
        int tmp = px%32;
        if (tmp>16){
            //如果余数超过16像素，就要放大像素
            dec = px + 32 - tmp;
        } else {
            dec = px -tmp;
        }
        return dec;
    }

}
