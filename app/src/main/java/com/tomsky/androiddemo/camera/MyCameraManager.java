package com.tomsky.androiddemo.camera;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.os.Build;
import android.text.TextUtils;
import android.view.TextureView;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MyCameraManager implements PreviewCallback {
	
	private static final int FRAME_BUFFER_NUM = 4;

    //界面
    private SurfaceTexture mSurfaceTexture = null;

    /********************摄像头参数******************/
    private Camera mCamera = null;
    private CameraHelper mCameraHelper = null;
    private int mCameraId = -1;//打开摄像头的id
    private int mDisplayOrientation = 0;
    private int mCodeOrientation = 0;//编码角度
    private WeakReference<Activity> weak = null;


    //摄像头配置
    private CameraConfig mVideoConfig = null;
    private List<Camera.Size> mCameraSizeList = null;//摄像头size支持列表

    private LinkedBlockingQueue<byte[]> mDataQueue;// 正在处理的队列
    /** 用于处理实时视频的线程池，不用线程池会导致预览画面卡顿 **/
	private ExecutorService mVideoEncodeExecutor;
	private boolean isEncoding = false;
	private boolean isDefaultFront = true;
    
    public static final boolean bAutoFocus; //对焦方式
    static
    {
        if (((Build.MODEL.contains("GT-I9505")) || (Build.MODEL.contains("GT-I9506")) ||
                (Build.MODEL.contains("GT-I9500")) || (Build.MODEL.contains("SGH-I337")) ||
                (Build.MODEL.contains("SGH-M919")) || (Build.MODEL.contains("SCH-I545")) ||
                (Build.MODEL.contains("SPH-L720")) || (Build.MODEL.contains("GT-I9508")) ||
                (Build.MODEL.contains("SHV-E300")) || (Build.MODEL.contains("SCH-R970")) ||
                (Build.MODEL.contains("SM-N900")) || (Build.MODEL.contains("LG-D801"))) &&
                (!Build.MODEL.contains("SM-N9008")))
        {
            bAutoFocus = true;
        }
        else
        {
            bAutoFocus = false;
        }
    }

    private static MyCameraManager myCameraManager = null;

    public static MyCameraManager getInstance(WeakReference<Activity> weak){
        if(weak == null||weak.get() == null){
            return null;
        }
        if(myCameraManager == null){
            myCameraManager = new MyCameraManager(weak);
        }
        return myCameraManager;
    }

    public MyCameraManager(WeakReference<Activity> weak){
        if(weak == null||weak.get() == null){
            return;
        }
        this.weak = weak;
        mCameraHelper = new CameraHelper();
        mVideoConfig = new CameraConfig();
    }

    /**
     * 回收资源使用，方式占用context
     */
    public void release(){
        if(myCameraManager != null){
            myCameraManager.closeCamera();
            if (myCameraManager.mDataQueue != null) {
            	myCameraManager.mDataQueue.clear();
            	myCameraManager.mDataQueue = null;
    		}
            mCameraId = -1;
            myCameraManager = null;
        }
    }


    /**
     * 切换摄像头
     */
    public void switchCamera()
    {
        closeCamera();
        if (mCameraId == mCameraHelper
                .getCameraId(CameraInfo.CAMERA_FACING_BACK))
        {
            mCameraId = mCameraHelper
                    .getCameraId(CameraInfo.CAMERA_FACING_FRONT);
        }
        else
        {
            mCameraId = mCameraHelper
                    .getCameraId(CameraInfo.CAMERA_FACING_BACK);
        }
        mCameraSizeList = null;
        StartCameraPreview();
    }

    public int getDisplayOrientation() {
    	return mDisplayOrientation;
	}

    public int getCameraOrientation(){
        if (mCameraId < 0 || mCameraId >= Camera.getNumberOfCameras()) {
            return 0;
        }
        int result = 0;
        CameraHelper.CameraInfo2 info = new CameraHelper.CameraInfo2();
        getCameraInfo(mCameraId, info);
        return info.orientation;
    }

    private boolean StartCameraPreview()
    {
    	boolean isNewOpen = false;
    	if(mCamera == null){
    		isNewOpen = true;
    	}
        openCamera();
        if (mCamera == null)
        {
            return false;
        }

        Parameters parameters = mCamera.getParameters();// 获得相机参数
        mCameraSizeList = parameters.getSupportedPreviewSizes();//获取支持的分辨率

        if(mCameraSizeList == null|| mCameraSizeList.size() == 0){
        	// 摄像头不支持
        	return false;
        }

        if (initCameraPreviewSize() == 0)
        {
            return false;
        }
        setCameraFocusMode(mCamera, parameters);
        parameters.setPreviewSize(mVideoConfig.width, mVideoConfig.height);
        parameters.setPreviewFormat(ImageFormat.NV21);
        mCamera.setParameters(parameters);
        try
        {
        	mCamera.setPreviewCallbackWithBuffer(this);
        	if (mVideoEncodeExecutor == null) {
        		//预览时判断，如果是空的则处理
    			mVideoEncodeExecutor = Executors.newSingleThreadExecutor();
    		}
            if(mSurfaceTexture != null){
                mCamera.setPreviewTexture(mSurfaceTexture);
                mCamera.startPreview();
            }
        }
        catch (Throwable e)
        {
            mCamera.release();
            mCamera = null;
            e.printStackTrace();
            return false;
        }
        if(isNewOpen){
        	//如果是重新打开的，要判断一下预览尺寸
        	if(mDataQueue != null){
        		mDataQueue.clear();
        	} else {
        		mDataQueue = new LinkedBlockingQueue<byte[]>(FRAME_BUFFER_NUM);
        	}
        	for (int i = 0; i < FRAME_BUFFER_NUM; i++) {
    			byte[] data = new byte[mVideoConfig.width*mVideoConfig.height*3 / 2];
    			mCamera.addCallbackBuffer(data);
    		}
        	if(mPreviewSizeChangeListener != null){
        		if(mDisplayOrientation%180==0){
        			mPreviewSizeChangeListener.onChange(mVideoConfig.width, mVideoConfig.height);
        		} else {
        			mPreviewSizeChangeListener.onChange(mVideoConfig.height, mVideoConfig.width);
        		}
            }
        }
        return true;
    }



    public void  setDisplayView(TextureView view){
        //mTextureView = view;//设置预览的view
    }

    public void setPreviewTexture(SurfaceTexture surfaceTexture){
        mSurfaceTexture = surfaceTexture;
        StartCameraPreview();
    }

    //private boolean isNewOpen = false;//是不是重新打开的

    private void openCamera()
    {
        if (mCamera == null)
        {
            CameraHelper camera_helper = new CameraHelper();
            if (mCameraId != -1)
            {
                mCamera = camera_helper.openCamera(mCameraId);
            }
            else
            {
            	if(isDefaultFront&& CameraHelper.hasFrontCamera()){
            		mCamera = camera_helper.openFrontCamera();
            		mCameraId = CameraHelper.getCameraId(CameraInfo.CAMERA_FACING_FRONT);
            	} else {
            		mCamera = camera_helper.openDefaultCamera();
            		mCameraId = CameraHelper.getCameraId(CameraInfo.CAMERA_FACING_BACK);
            	}

            }
            if (mCamera == null)
            {
                // 开启摄像头失败回调
                if(mOnCameraCallback != null){
                    mOnCameraCallback.onOpenCameraFailed();
                }
                return;
            }

            try
            {
                if(weak != null&&weak.get() != null){
                	mDisplayOrientation = CameraHelper.getCameraDisplayOrientation(
                            weak.get(), myCameraManager.mCameraId);
                    mCamera.setDisplayOrientation(mDisplayOrientation);
                    if(isFrontCamera()){
                    	mCodeOrientation = (mDisplayOrientation + 180) % 360;
                    } else {
                    	mCodeOrientation = mDisplayOrientation;
                    }

                    // 开启摄像头成功回调
                    if(mOnCameraCallback != null){
                        mOnCameraCallback.onOpenCameraSuccess();
                    }

//                    isEncoding = true;
                } else {
                    closeCamera();
                    // 开启摄像头失败回调
                    if(mOnCameraCallback != null){
                        mOnCameraCallback.onOpenCameraFailed();
                    }
                }

            }
            catch (Throwable e)
            {
                closeCamera();
                // 开启摄像头失败回调
                if(mOnCameraCallback != null){
                    mOnCameraCallback.onOpenCameraFailed();
                }
            }
        }
    }

    public Camera getCamera() {
		return mCamera;
	}

    public void closeCamera()
    {
    	isEncoding = false;
        if (mCamera != null)
        {
            try
            {
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
            }
            catch (Throwable e)
            {
            }
            try
            {
                mCamera.release();
            }
            catch (Throwable e)
            {
            }
            mCamera = null;
        }
        if (mVideoEncodeExecutor != null) {
			mVideoEncodeExecutor.shutdown();
			try {
				mVideoEncodeExecutor.awaitTermination(Long.MAX_VALUE,
						TimeUnit.DAYS);
			} catch (Exception e) {
				e.printStackTrace();
			}
			mVideoEncodeExecutor = null;
		}

		if (mDataQueue != null) {
			mDataQueue.clear();
			//mDataQueue = null;
		}
    }


    public CameraConfig getVideoConfig() {
    	return mVideoConfig;
	}
    
    public void setCameraConfig(CameraConfig videoConfig){
    	if(mVideoConfig != null&&videoConfig != null){
    		//只要初始化，mVideoConfig应该就不会为null
    		mVideoConfig.DEFULT_HEIGHT = videoConfig.DEFULT_HEIGHT;
        	mVideoConfig.DEFULT_VIDEO_HEIGHT = videoConfig.DEFULT_VIDEO_HEIGHT;
        	mVideoConfig.DEFULT_VIDEO_WIDTH = videoConfig.DEFULT_VIDEO_WIDTH;
        	mVideoConfig.DEFULT_WIDTH = videoConfig.DEFULT_WIDTH;
    	}
    }

    public boolean isFrontCamera() {
    	if (myCameraManager == null||myCameraManager.mCameraId < 0 || myCameraManager.mCameraId >= Camera.getNumberOfCameras()) {
            return false;
        }
        CameraHelper.CameraInfo2 info = new CameraHelper.CameraInfo2();
		getCameraInfo(myCameraManager.mCameraId, info);
        if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
            return true;
        }
        return false;
	}

    /**
     * 设置对焦模式
     * @param paramCamera
     * @param paramParameters
     * @return
     */
    private boolean setCameraFocusMode(Camera paramCamera, Parameters paramParameters)
    {
        List localList = paramParameters.getSupportedFocusModes();
        if (localList == null)
        {
            return false;
        }
        String focusMode = null;
        if (bAutoFocus && localList.contains(Parameters.FOCUS_MODE_AUTO))
        {
            focusMode = Parameters.FOCUS_MODE_AUTO;
        }
        else if (localList.contains(Parameters.FOCUS_MODE_CONTINUOUS_VIDEO))
        {
            focusMode = Parameters.FOCUS_MODE_CONTINUOUS_VIDEO;
        }
        else if (localList.contains(Parameters.FOCUS_MODE_INFINITY))
        {
            focusMode = Parameters.FOCUS_MODE_INFINITY;
        }
        try
        {
            if (!TextUtils.isEmpty(focusMode))
            {
                paramParameters.setFocusMode(focusMode);
                paramCamera.setParameters(paramParameters);
                return true;
            }
        }
        catch (Exception localException)
        {
            paramParameters.setFocusMode(paramParameters.getFocusMode());
        }
        return false;
    }



    //取得合适preview尺寸，同时也是编码后的视频尺寸，这里统一要16:9的，具体大小根据网络状况来定
    private int initCameraPreviewSize()
    {
        Camera.Size size_ret = null;//替补size
        if (mCameraSizeList == null || mCameraSizeList.size() == 0)
        {
            //走默认
            return 0;
        }

        //第一次进行精准，我测试了10多款手机没有一个不匹配的，所以第一步只找完全匹配的即可，这样会增加大部分手机的效率
        for (int i = 0; i < mCameraSizeList.size(); i++)
        {
            Camera.Size size = mCameraSizeList.get(i);
            if (size.width == mVideoConfig.DEFULT_WIDTH && size.height == mVideoConfig.DEFULT_HEIGHT)
            {
                //如果完全匹配，则默认配置就可用，跳出即可,正常情况就应该有1280X720
                mVideoConfig.width = mVideoConfig.DEFULT_WIDTH;
                mVideoConfig.height = mVideoConfig.DEFULT_HEIGHT;
                mVideoConfig.videoWidth = mVideoConfig.DEFULT_VIDEO_WIDTH;
                mVideoConfig.videoHeight = mVideoConfig.DEFULT_VIDEO_HEIGHT;
                return 1;
            }
        }

        //如果上边没有找到，说明肯定没有完全匹配的，所以找同比例的
        for (int i = 0; i < mCameraSizeList.size(); i++)
        {
            Camera.Size size = mCameraSizeList.get(i);
            if ((size.width * mVideoConfig.DEFULT_HEIGHT) == (size.height * mVideoConfig.DEFULT_WIDTH))
            {
                if (size_ret == null)
                {
                    size_ret = size;
                }
                else if (size.width > size_ret.width)
                {
                    //如果不是空，说明同比例有多个;如果新的比旧的大，则选新的
                    size_ret = size;
                }
            }
        }

        if (size_ret != null)
        {
            //如果不为空，说明说明找到同比例的，就走同比例的
            mVideoConfig.width = size_ret.width;
            mVideoConfig.height = size_ret.height;
            mVideoConfig.videoWidth = mVideoConfig.DEFULT_VIDEO_WIDTH;
            mVideoConfig.videoHeight = mVideoConfig.DEFULT_VIDEO_HEIGHT;
            return 1;
        }
        else
        {
            //如果还是空，说明没有同比例的，需要重新找了
            for (int i = 0; i < mCameraSizeList.size(); i++)
            {
                Camera.Size size = mCameraSizeList.get(i);
                if (size.width >= mVideoConfig.DEFULT_VIDEO_HEIGHT && size.height <= mVideoConfig.DEFULT_VIDEO_HEIGHT)
                { //找到一个最接近宽度尺寸的一组
                    size_ret = size;
                    mVideoConfig.width = size.width;
                    mVideoConfig.height = size.height;
                    mVideoConfig.videoWidth = size.height;
                    mVideoConfig.videoHeight = size.width;
                    return 1;
                }
            }
        }

        if (size_ret == null)
        {
            //这时候还找不到，基本不可能了；所以如果还找不到，就随便来一个得了
            size_ret = mCameraSizeList.get(0);
            mVideoConfig.width = size_ret.width;
            mVideoConfig.height = size_ret.height;
            mVideoConfig.videoWidth = size_ret.width;
            mVideoConfig.videoHeight = size_ret.height;
        }
        return 1;
    }

    public synchronized static void getCameraInfo(final int cameraId, final CameraHelper.CameraInfo2 cameraInfo) {
        if (cameraId < 0 || cameraId >= Camera.getNumberOfCameras() || cameraInfo == null) {
            return;
        }
        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        cameraInfo.facing = info.facing;
        cameraInfo.orientation = info.orientation;
    }

    /******* 回调 *****/
    private PreviewSizeChangeListener mPreviewSizeChangeListener = null;
    public interface PreviewSizeChangeListener{
        void onChange(int width, int height);
    }
    public void setOnPreviewSizeChange(PreviewSizeChangeListener listener) {
        this.mPreviewSizeChangeListener = listener;
    }

    public class PreviewSize
    {
        public int width;
        public int height;
    }
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		if (data == null || mCamera == null)
			return;

		if(mOnPreviewCallBack != null){
			//原始回调
			if(mOnPreviewCallBack.onOriginalFrame(data,mVideoConfig.width,mVideoConfig.height)){
                if(camera != null){
                    camera.addCallbackBuffer(data);
                }
                return;
            }
		}
		if (isEncoding && mDataQueue != null) {
//			if (mDataQueue.size() < FRAME_BUFFER_NUM - 1) {
//				mDataQueue.offer(data);//压入
//				if (mVideoEncodeExecutor != null
//						&& !mVideoEncodeExecutor.isShutdown()) {
//					final long timestamp = System.currentTimeMillis();
//					mVideoEncodeExecutor.execute(new Runnable() {
//						public void run() {
//							if (isEncoding) {
//								byte[] dataTmp = (byte[]) mDataQueue.poll();// 取出缓存
//								if(mOnPreviewCallBack != null){
//									//TODO 做一系列翻转，将目标尺寸返回回去
//									if(targetData == null||targetData.length != mVideoConfig.videoWidth*mVideoConfig.videoHeight*3/2){
//										targetData = new byte[mVideoConfig.videoWidth*mVideoConfig.videoHeight*3/2];
//									}
//									LibYuv.nv21ScaleRotationI420(dataTmp, mVideoConfig.width, mVideoConfig.height, targetData, mVideoConfig.videoWidth, mVideoConfig.videoHeight, mCodeOrientation);
//									mOnPreviewCallBack.onTargetFrame(targetData, mVideoConfig.videoWidth, mVideoConfig.videoHeight);
//								}
//								if(mCamera != null){
//									mCamera.addCallbackBuffer(dataTmp);
//								}
//							}
//						}
//					});
//				}
//			} else {
//				mCamera.addCallbackBuffer(data);
//			}
		} else {
			// 如果不编码，直接塞回去
			mCamera.addCallbackBuffer(data);
		}

	}

	private byte[] targetData = null;


	public boolean isSupportedFlash(){
		boolean isSupported = false;
		if (mCamera != null)
		{
		    try
		    {
		        List<String> list_ret = mCamera.getParameters().getSupportedFlashModes();
		        for (int i = 0; i < list_ret.size(); ++i)
		        {
		            if (list_ret.get(i).equalsIgnoreCase(Parameters.FLASH_MODE_AUTO) ||
		                    list_ret.get(i).equalsIgnoreCase(Parameters.FLASH_MODE_TORCH))
		            {
		            	isSupported = true;
		                break;
		            }
		        }
		    }
		    catch (Throwable e)
		    {
		    	isSupported = false;
		    }
		}
		return isSupported;
	}


	/**
     * 打开闪光灯
     */
    public void turnOnFlash(){
    	if(mCamera != null){
    		try {
				Parameters parameter = mCamera.getParameters();
				parameter.setFlashMode(Parameters.FLASH_MODE_TORCH);
				mCamera.setParameters(parameter);
			} catch (Exception e) {
			}
        }
    }

    /**
     * 关闭闪光灯
     */
    public void turnOffFlash(){
    	if(mCamera != null){
    		try {
				Parameters parameter = mCamera.getParameters();
				parameter.setFlashMode(Parameters.FLASH_MODE_OFF);
				mCamera.setParameters(parameter);
			} catch (Exception e) {
			}
        }
    }


    public boolean isZoomSupported() {
        try{
            if(mCamera != null){
                return mCamera.getParameters().isZoomSupported();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


    public int getMaxZoom() {
        try{
            if(mCamera != null){
                return mCamera.getParameters().getMaxZoom();
            }
        }catch (Exception e){

        }
        return 0;
    }

    public int getZoom() {
        try{
            if(mCamera != null){
                return mCamera.getParameters().getZoom();
            }
        }catch (Exception e){

        }
        return 0;
    }

    public boolean setZoom(int value) {
        try{
            if (mCamera != null && mCamera.getParameters().isZoomSupported())
            {
                if (value < 0)
                {
                    value = 0;
                } else if(value > mCamera.getParameters().getMaxZoom()){
                    value = mCamera.getParameters().getMaxZoom();
                }
                Parameters param = mCamera.getParameters();
                param.setZoom(value);
                mCamera.setParameters(param);
                return true;
            }
        }catch (Exception e){

        }
        return false;
    }
    
    /**
     * 这是默认打开前置摄像头，如果没有则打开后置摄像头
     * @param isFront
     */
    public void setDefaultCamraFront(boolean isFront){
    	isDefaultFront = isFront;
    }

    public List<Camera.Size> getCameraSizeList(){
        if(mCameraSizeList != null){
            return mCameraSizeList;
        }else{
            if(mCamera != null){
                Parameters parameters = mCamera.getParameters();// 获得相机参数
                return parameters.getSupportedPreviewSizes();//获取支持的分辨率
            }
        }

        return null;
    }

    public void setCameraId(int cameraId){
        mCameraId = cameraId;
    }

	/****************************回调接口******************************/
	private OnPreviewCallBack mOnPreviewCallBack = null;

    /****************************开启摄像头回调接口******************************/
    private OnCameraCallback mOnCameraCallback;
	/**
	 * @author chengxiangyu
	 * 数据回调
	 */
	public interface OnPreviewCallBack{
		void onTargetFrame(byte[] data, int width, int height);

        /**
         *
         * @param data
         * @param width
         * @param height
         * @return 不需要缩放了返回true，否则返回false
         */
		boolean onOriginalFrame(byte[] data, int width, int height);
	}
	
	public void setOnPreviewCallBack(OnPreviewCallBack callBack) {
		mOnPreviewCallBack = callBack;
	}

    /**
     * 开启摄像头回调
     */
    public interface  OnCameraCallback{
        /**
         * 开启摄像头成功
         */
        void onOpenCameraSuccess();

        /**
         * 开启摄像头失败
         */
        void onOpenCameraFailed();
    }

    public void setOnCameraCallback(OnCameraCallback callback){
        this.mOnCameraCallback = callback;
    }
	
}
