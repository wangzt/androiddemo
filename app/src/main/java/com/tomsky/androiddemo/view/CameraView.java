package com.tomsky.androiddemo.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.TextureView;
import android.widget.RelativeLayout;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.camera.CameraConfig;
import com.tomsky.androiddemo.camera.CameraHelper;
import com.tomsky.androiddemo.camera.MyCameraManager;
import com.tomsky.androiddemo.util.LogUtils;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by j-wangzhitao on 17-2-14.
 */

public class CameraView extends RelativeLayout implements TextureView.SurfaceTextureListener,MyCameraManager.PreviewSizeChangeListener {

    private static final String TAG = "rotation";

    public static final int SCALETYPE_CENTER_INSIDE = 0;//内部补黑边
    public static final int SCALETYPE_CENTER_OUTSIDE = 1;  //扩充出屏幕外
    public static final int SCALETYPE_FIT_XY = 2;	   //拉伸


    private TextureView cameraPreview;
    private SurfaceTexture cameraPreviewSurface;
    private MyCameraManager myCameraManager = null;
    private WeakReference<Activity> weak = null;
    private Context mContext = null;

    private CameraConfig mVideoConfig = null;
    private boolean isDefaultFront = true;
    private int scaleType = SCALETYPE_CENTER_OUTSIDE;


    private int viewWidth = 0;   //控件宽度
    private int viewHeight = 0;  //控件高度
    private int cameraWidth = 0; //摄像头采集的宽度
    private int cameraHeight = 0;//摄像头采集的高度

    private boolean mAutoStart = true; // 自动开始录制

    private ITextureListener mListener;

    public CameraView(Context context) {
        this(context, null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initData(context);
    }

    /**
     * 初始化页面数据
     * @param context
     */
    private void initView(Context context) {
        inflate(context, R.layout.camera_view,this);
        cameraPreview = (TextureView) findViewById(R.id.camera_texture_view);
        cameraPreview.setSurfaceTextureListener(this);
    }

    public void setTextureListener(ITextureListener listener) {
        mListener = listener;
    }

    /**
     * 初始化数据
     */
    private void initData(Context context) {
        mContext = context.getApplicationContext();
        if(context != null&&context instanceof Activity){
            weak = new WeakReference<Activity>((Activity)context);
            myCameraManager = MyCameraManager.getInstance(weak);
            myCameraManager.setOnPreviewSizeChange(this);//注册预览尺寸回调
        }
    }

    public void setCameraId(int cameraId){
        if(myCameraManager != null){
            myCameraManager.setCameraId(cameraId);
        }
    }

    /**
     * @param scaleType 预览样式
     */
    public void setScaleType(int scaleType) {
        this.scaleType = scaleType;
    }


    public Camera getCamera(){
        if(myCameraManager != null){
            return myCameraManager.getCamera();
        }
        return null;
    }

    public int getDisplayOrientation() {
        if(myCameraManager != null){
            return myCameraManager.getDisplayOrientation();
        }
        return 0;
    }

    public int getCameraOrientation(){
        if(myCameraManager != null){
            return myCameraManager.getCameraOrientation();
        }
        return 0;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        cameraPreviewSurface = surface;
        if (mAutoStart) {
            beginLive();
        }
        if (mListener != null) {
            mListener.onSurfaceTextureAvailable();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        LogUtils.d(TAG, "onSurfaceTextureSizeChanged, width:"+width+", height:"+height);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if(myCameraManager != null){
            //myCameraManager.setReopen(true);//如果页面消失，下次显示时需要重新打开
            myCameraManager.closeCamera();
            myCameraManager.setOnPreviewCallBack(null);
        }
        cameraPreviewSurface = null;
        LogUtils.d(TAG, "onSurfaceTextureDestroyed");
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.d(TAG, "cameraView onConfigurationChanged, orientation:"+newConfig.orientation);
    }

    public void beginLive() {
        if(myCameraManager == null || cameraPreviewSurface == null){
            return;
        }
        if(mVideoConfig != null){
            myCameraManager.setCameraConfig(mVideoConfig);
        }
        myCameraManager.setDefaultCamraFront(isDefaultFront);
        myCameraManager.setPreviewTexture(cameraPreviewSurface);
        myCameraManager.setOnPreviewCallBack(mOnPreviewCallBack);

        //mCamera = myCameraManager.getCamera();
        mVideoConfig = myCameraManager.getVideoConfig();
    }

    public void setCameraCondfig(int width,int height,int videoWidth,int videoHeight){
        if(mVideoConfig == null){
            mVideoConfig = new CameraConfig();
        }
        mVideoConfig.DEFULT_WIDTH = width;//预览尺寸
        mVideoConfig.DEFULT_HEIGHT = height;//预览尺寸
        mVideoConfig.DEFULT_VIDEO_WIDTH = videoWidth;//视频尺寸
        mVideoConfig.DEFULT_VIDEO_HEIGHT = videoHeight;//视频尺寸
    }

    /**
     * 摄像头开启选择的预览尺寸大小回调
     * @param width
     * @param height
     */
    @Override
    public void onChange(int width, int height) {
        cameraWidth = width;
        cameraHeight = height;
        LogUtils.d(TAG, "onChange, cameraWidth:"+cameraWidth+", cameraHeight:"+cameraHeight+", viewWidth:"+viewWidth+", viewHeight:"+viewHeight);
        setPreviewLayoutParams();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(viewWidth != getWidth()||viewHeight != getHeight()){
            viewWidth = getWidth();
            viewHeight = getHeight();
            LogUtils.d(TAG, "onLayout, cameraWidth:"+cameraWidth+", cameraHeight:"+cameraHeight+", viewWidth:"+viewWidth+", viewHeight:"+viewHeight);
            setPreviewLayoutParams();
        }
    }


    /**
     * 设置预览的大小，以便充满整个屏幕
     */
    private void setPreviewLayoutParams(){
        //Log.d("cxy", "viewWidth = "+viewWidth+",viewHeight="+viewHeight+",cameraWidth="+cameraWidth+",cameraHeight="+cameraHeight);
        if(viewWidth == 0||viewHeight == 0||cameraWidth == 0||cameraHeight == 0){
            return;
        }

        int setHeight = viewHeight;
        int setWidth = viewWidth;

        float scaleX = (float) setWidth / cameraWidth;
        float scaleY = (float) setHeight / cameraHeight;
        float scale = 0.0f;

        if (scaleX>=scaleY)
        {
            switch (scaleType) {
                case SCALETYPE_CENTER_INSIDE:
                    scale = scaleY;
                    break;
                case SCALETYPE_CENTER_OUTSIDE:
                    scale = scaleX;
                    break;
                case SCALETYPE_FIT_XY:
                    scale = 0.0f;
                    break;
                default:
                    break;
            }
        }
        else if (scaleX<scaleY)
        {
            switch (scaleType) {
                case SCALETYPE_CENTER_INSIDE:
                    scale = scaleX;
                    break;
                case SCALETYPE_CENTER_OUTSIDE:
                    scale = scaleY;
                    break;
                case SCALETYPE_FIT_XY:
                    scale = 0.0f;
                    break;
                default:
                    break;
            }
        }

        if(scale != 0.0f){
            setWidth = (int) (cameraWidth * scale);
            setHeight = (int) (cameraHeight * scale);
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                setWidth,setHeight);
        if(setWidth!=viewWidth){
            params.setMargins((viewWidth-setWidth)/2, 0, (viewWidth-setWidth)/2, 0);
        }
        if(setHeight!=viewHeight){
            params.setMargins( 0,(viewHeight-setHeight)/2, 0, (viewHeight-setHeight)/2);
        }
        LogUtils.d(TAG, "setPreviewLayoutParams, width:"+setWidth+", height:"+setHeight+", margin("+params.leftMargin +
                "," +params.topMargin+","+params.rightMargin+","+params.bottomMargin+")");
        if(cameraPreview != null)
            cameraPreview.setLayoutParams(params);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        releaseCameraManager();
    }

    public TextureView getTextureView() {
        return cameraPreview;
    }

    public void setAutoStart(boolean autoStart) {
        this.mAutoStart = autoStart;
    }

    public void updatePreViewTextureListener() {
        if (cameraPreview != null) {
            cameraPreview.setSurfaceTextureListener(this);
        }
    }

    /**************************以上是页面逻辑部分******************************/
    private MyCameraManager.OnPreviewCallBack mOnPreviewCallBack = null;
    public void setOnPreviewCallBack(MyCameraManager.OnPreviewCallBack callBack) {
        mOnPreviewCallBack = callBack;
        if(myCameraManager != null){
            myCameraManager.setOnPreviewCallBack(mOnPreviewCallBack);
        }
    }

    /**
     * 开启摄像头回调
     * @param callback
     */
    public void setOnCameraCallback(MyCameraManager.OnCameraCallback callback){
        if(myCameraManager != null){
            myCameraManager.setOnCameraCallback(callback);
        }
    }

    /************************** begin 操控  ***********************************/



    public void swichCamera(){
        if(myCameraManager != null){
            myCameraManager.switchCamera();
        }
    }

    public void closeCamera (){
        if(myCameraManager != null){
            myCameraManager.closeCamera();
        }
    }

    public void closeCameraAndReleaseManager (){
        closeCamera ();
        releaseSurface();
        releaseCameraManager();
    }

    public void releaseSurface(){
        if(myCameraManager != null){
            myCameraManager.setOnPreviewCallBack(null);
        }
        cameraPreviewSurface = null;
    }

    public void releaseCameraManager(){
        if(myCameraManager != null){
            myCameraManager.release();
            myCameraManager = null;
        }
    }

    /**
     * @return 当前摄像头是不是支持闪关灯
     */
    public boolean isSupportedFlash(){
        if(myCameraManager != null){
            return myCameraManager.isSupportedFlash();
        }
        return false;
    }

    /**
     * 打开闪光灯
     */
    public void turnOnFlash(){
        if(myCameraManager != null){
            myCameraManager.turnOnFlash();
        }
    }

    public void turnOffFlash(){
        if(myCameraManager != null){
            myCameraManager.turnOffFlash();
        }
    }

    /**
     * 有没有前置摄像头
     */
    public boolean hasFrontCamera(){
        return CameraHelper.hasFrontCamera();
    }

    public boolean isFrontCamera(){
        if(myCameraManager != null){
            return myCameraManager.isFrontCamera();
        }
        return false;
    }

    /**
     * 这是默认打开前置摄像头，如果没有则打开后置摄像头
     * @param isFront
     */
    public void setDefaultCamraFront(boolean isFront){
        isDefaultFront = isFront;
        if(myCameraManager != null){
            myCameraManager.setDefaultCamraFront(isFront);
        }
    }

    public boolean isSupport720p() {
        boolean isSupport = false;
        if(myCameraManager != null){
            List<Camera.Size> sizes = myCameraManager.getCameraSizeList();
            if (sizes != null && sizes.size() > 0) {
                for (Camera.Size size : sizes) {
                    if (Math.max(size.width, size.height) == 1280 && Math.min(size.width, size.height) == 720) {
                        isSupport = true;
                        break;
                    }
                }
            }
        }

        return isSupport;
    }


    public boolean isZoomSupported() {
        if(myCameraManager != null){
            return myCameraManager.isZoomSupported();
        }
        return false;
    }


    public int getMaxZoom() {
        if(myCameraManager != null){
            return myCameraManager.getMaxZoom();
        }
        return 0;
    }

    public int getZoom() {
        if(myCameraManager != null){
            return myCameraManager.getZoom();
        }
        return 0;
    }

    public boolean setZoom(int value) {
        if(myCameraManager != null){
            return myCameraManager.setZoom(value);
        }
        return false;
    }

    public interface ITextureListener {
        void onSurfaceTextureAvailable();
    }

}
