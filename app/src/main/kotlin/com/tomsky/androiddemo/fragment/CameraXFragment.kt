package com.tomsky.androiddemo.fragment

import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.camera.core.*
import androidx.core.view.setPadding
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.concurrent.futures.await
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tomsky.androiddemo.BaseApplication
import com.tomsky.androiddemo.R
import com.tomsky.androiddemo.activity.KEY_EVENT_EXTRA
import com.tomsky.androiddemo.databinding.CameraxUiContainerBinding
import com.tomsky.androiddemo.databinding.FragmentCameraxBinding
import com.tomsky.androiddemo.util.UIUtils
import com.tomsky.androiddemo.utils.MediaStoreUtils
import com.tomsky.androiddemo.views.ANIMATION_FAST_MILLIS
import com.tomsky.androiddemo.views.ANIMATION_SLOW_MILLIS
import com.tomsky.androiddemo.views.simulateClick
import kotlinx.coroutines.launch
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/** Helper type alias used for analysis use case callbacks */
typealias LumaListener = (luma: Double) -> Unit

/**
 *
 * Created by wangzhitao on 2023/03/13
 *
 **/
class CameraXFragment: Fragment() {

    private var _fragmentCameraBinding: FragmentCameraxBinding? = null

    private val fragmentCameraBinding get() = _fragmentCameraBinding!!

    private var cameraUiContainerBinding: CameraxUiContainerBinding? = null

    private lateinit var mediaStoreUtils: MediaStoreUtils

    private lateinit var broadcastManager: LocalBroadcastManager

    private var displayId: Int = -1
    private var lensFacing: Int = CameraSelector.LENS_FACING_FRONT
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalysis: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
//    private lateinit var windowManager: WindowManager

    private val displayManager by lazy {
        requireContext().getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }

    private lateinit var cameraExecutor: ExecutorService

    private val volumeDownReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getIntExtra(KEY_EVENT_EXTRA, KeyEvent.KEYCODE_UNKNOWN)) {
                KeyEvent.KEYCODE_VOLUME_DOWN -> {
                    // When the volume down button is pressed, simulate a shutter button click
                    cameraUiContainerBinding?.cameraCaptureButton?.simulateClick()
                }
            }
        }
    }

    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) = Unit
        override fun onDisplayRemoved(displayId: Int) = Unit
        override fun onDisplayChanged(displayId: Int) = view?.let {view ->
            if (displayId == this@CameraXFragment.displayId) {
                Log.d(TAG, "Rotation changed: ${view.display.rotation}")
                imageCapture?.targetRotation = view.display.rotation
                imageAnalysis?.targetRotation = view.display.rotation
            }
        } ?: Unit
    }

    override fun onResume() {
        super.onResume()
        if (!CameraPermissionFragment.hasPermissions(requireContext())) {
            Navigation.findNavController(requireActivity(), R.id.camerax_fragment_container).navigate(
                CameraXFragmentDirections.actionCameraToPermissions()
            )
        }
    }

    override fun onDestroyView() {
        _fragmentCameraBinding = null
        super.onDestroyView()
        cameraExecutor.shutdown()

        broadcastManager.unregisterReceiver(volumeDownReceiver)
        displayManager.unregisterDisplayListener(displayListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentCameraBinding = FragmentCameraxBinding.inflate(inflater, container, false)
        return fragmentCameraBinding.root
    }

    private fun setGalleryThumbnail(filename: String) {
        cameraUiContainerBinding?.photoViewButton?.let { photoViewButton ->
            photoViewButton.post {
                photoViewButton.setPadding(resources.getDimension(R.dimen.stroke_small).toInt())

                Glide.with(photoViewButton)
                    .load(filename)
                    .apply(RequestOptions.circleCropTransform())
                    .into(photoViewButton)
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraExecutor = Executors.newSingleThreadExecutor()
        broadcastManager = LocalBroadcastManager.getInstance(view.context)

        val filter = IntentFilter().apply { addAction(KEY_EVENT_EXTRA) }
        broadcastManager.registerReceiver(volumeDownReceiver, filter)

        // Every time the orientation of device changes, update rotation for use cases
        displayManager.registerDisplayListener(displayListener, null)

//        windowManager = WindowManager(view.context)

        // Initialize MediaStoreUtils for fetching this app's images
        mediaStoreUtils = MediaStoreUtils(requireContext())

        fragmentCameraBinding.viewFinder.post {
            displayId = fragmentCameraBinding.viewFinder.display.displayId

            updateCameraUi()

            lifecycleScope.launch {
                setUpCamera()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        bindCameraUseCases()

        updateCameraSwitchButton()
    }

    private suspend fun setUpCamera() {
        cameraProvider = ProcessCameraProvider.getInstance(requireContext()).await()

        lensFacing = when {
            hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
            hasBackCamera() -> CameraSelector.LENS_FACING_BACK
            else -> throw  java.lang.IllegalStateException("Front and back camera are unavailable")
        }

        // Enable or disable switching between cameras
        updateCameraSwitchButton()

        // Build and bind the camera use cases
        bindCameraUseCases()
    }

    /** Declare and bind preview, capture and analysis use cases */
    private fun bindCameraUseCases() {
        val metrics = BaseApplication.getContext().resources.displayMetrics
        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        val rotation = fragmentCameraBinding.viewFinder.display.rotation

        val cameraProvider = cameraProvider
            ?: throw java.lang.IllegalStateException("Camera initialization failed")

        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        preview = Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()

        imageAnalysis = ImageAnalysis.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()
            .also {
                it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { luma ->
//                    Log.d(TAG, "Aversage luminosity: $luma")
                })
            }

        cameraProvider.unbindAll()

        if (camera != null) {
            removeCameraStateObservers(camera!!.cameraInfo)
        }

        try {
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture, imageAnalysis)

            preview?.setSurfaceProvider(fragmentCameraBinding.viewFinder.surfaceProvider)
            observeCameraState(camera?.cameraInfo!!)
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "Use case binding failed", e)
        }
    }

    private fun removeCameraStateObservers(cameraInfo: CameraInfo) {
        cameraInfo.cameraState.removeObservers(viewLifecycleOwner)
    }

    private fun observeCameraState(cameraInfo: CameraInfo) {
        cameraInfo.cameraState.observe(viewLifecycleOwner) { cameraState ->
            run {
                when (cameraState.type) {
                    CameraState.Type.PENDING_OPEN -> {
                        Log.d(TAG, "CameraState: Pending Open")
                    }
                    CameraState.Type.OPENING -> {
                        Log.d(TAG, "CameraState: Opening")
                    }
                    CameraState.Type.OPEN -> {
                        Log.d(TAG, "CameraState: Open")
                    }
                    CameraState.Type.CLOSING -> {
                        Log.d(TAG, "CameraState: Closing")
                    }
                    CameraState.Type.CLOSED -> {
                        Log.d(TAG, "CameraState: Closed")
                    }
                }
            }

            cameraState.error?.let { error ->
                when (error.code) {
                    CameraState.ERROR_STREAM_CONFIG -> {
                        Log.d(TAG, "Stream config error")
                    }
                    CameraState.ERROR_CAMERA_IN_USE -> {
                        Log.d(TAG, "Camera in use")
                    }
                    CameraState.ERROR_MAX_CAMERAS_IN_USE -> {
                        Log.d(TAG, "Max cameras in use")
                    }
                    CameraState.ERROR_OTHER_RECOVERABLE_ERROR -> {
                        Log.d(TAG, "Other recoverable error")
                    }
                    CameraState.ERROR_CAMERA_DISABLED -> {
                        Log.d(TAG, "Camera disabled")
                    }
                    CameraState.ERROR_CAMERA_FATAL_ERROR -> {
                        Log.d(TAG, "Fatal error")
                    }
                    CameraState.ERROR_DO_NOT_DISTURB_MODE_ENABLED -> {
                        Log.d(TAG, "Do not disturb mode enabled")
                    }
                }
            }
        }
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    private fun updateCameraUi() {
        cameraUiContainerBinding?.root?.let {
            fragmentCameraBinding.root.removeView(it)
        }

        cameraUiContainerBinding = CameraxUiContainerBinding.inflate(
            LayoutInflater.from(requireContext()),
            fragmentCameraBinding.root,
            true
        )

        lifecycleScope.launch {
            val thumbnailUri = mediaStoreUtils.getLatestImageFilename()
            thumbnailUri?.let {
                setGalleryThumbnail(it)
            }
        }

        cameraUiContainerBinding?.cameraCaptureButton?.setOnClickListener {
            imageCapture?.let {imageCapture ->
                val name = SimpleDateFormat(FILENAME, Locale.CHINA)
                    .format(System.currentTimeMillis())
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                    put(MediaStore.MediaColumns.MIME_TYPE, PHOTO_TYPE)
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                        val appName = requireContext().resources.getString(R.string.app_name)
                        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/${appName}")
                    }
                }

                val outputOptions = ImageCapture.OutputFileOptions
                    .Builder(requireContext().contentResolver,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues)
                    .build()

                imageCapture.takePicture(
                    outputOptions, cameraExecutor, object : ImageCapture.OnImageSavedCallback {
                        override fun onError(exception: ImageCaptureException) {
                            Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                        }

                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            val savedUri = output.savedUri

                            // We can only change the foreground Drawable using API level 23+ API
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                // Update the gallery thumbnail with latest picture taken
                                setGalleryThumbnail(savedUri.toString())
                            }

                            // Implicit broadcasts will be ignored for devices running API level >= 24
                            // so if you only target API level 24+ you can remove this statement
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                                // Suppress deprecated Camera usage needed for API level 23 and below
                                @Suppress("DEPRECATION")
                                requireActivity().sendBroadcast(
                                    Intent(android.hardware.Camera.ACTION_NEW_PICTURE, savedUri)
                                )
                            }
                        }

                    }
                )

                // We can only change the foreground Drawable using API level 23+ API
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    // Display flash animation to indicate that photo was captured
                    fragmentCameraBinding.root.postDelayed({
                        fragmentCameraBinding.root.foreground = ColorDrawable(Color.WHITE)
                        fragmentCameraBinding.root.postDelayed({
                            fragmentCameraBinding.root.foreground = null }, ANIMATION_FAST_MILLIS)
                    }, ANIMATION_SLOW_MILLIS)
                }
            }
        }

        cameraUiContainerBinding?.cameraSwitchButton?.let {
            it.isEnabled = false
            it.setOnClickListener {
                lensFacing = if (CameraSelector.LENS_FACING_FRONT == lensFacing) {
                    CameraSelector.LENS_FACING_BACK
                } else {
                    CameraSelector.LENS_FACING_FRONT
                }

                bindCameraUseCases()
            }
        }

        cameraUiContainerBinding?.photoViewButton?.setOnClickListener {
//            lifecycleScope.launch {
//                if (mediaStoreUtils.getImages().isNotEmpty()) {
//                    Navigation.findNavController(requireActivity(), R.id.camerax_fragment_container)
//                        .navigate(CameraXFragmentDirections.actionCameraToGallery(
//                            mediaStoreUtils.mediaStoreCollection.toString()
//                        ))
//                }
//            }
        }
    }

    private fun updateCameraSwitchButton() {
        try {
            cameraUiContainerBinding?.cameraSwitchButton?.isEnabled = hasBackCamera() && hasFrontCamera()
        } catch (e: CameraInfoUnavailableException) {
            cameraUiContainerBinding?.cameraSwitchButton?.isEnabled = false
        }
    }

    private fun hasBackCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
    }

    private fun hasFrontCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
    }

    /**
     * Our custom image analysis class.
     *
     * <p>All we need to do is override the function `analyze` with our desired operations. Here,
     * we compute the average luminosity of the image by looking at the Y plane of the YUV frame.
     */
    private class LuminosityAnalyzer(listener: LumaListener? = null) : ImageAnalysis.Analyzer {
        private val frameRateWindow = 8
        private val frameTimestamps = ArrayDeque<Long>(5)
        private val listeners = ArrayList<LumaListener>().apply { listener?.let { add(it) } }
        private var lastAnalyzedTimestamp = 0L
        var framesPerSecond: Double = -1.0
            private set

        /**
         * Helper extension function used to extract a byte array from an image plane buffer
         */
        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        /**
         * Analyzes an image to produce a result.
         *
         * <p>The caller is responsible for ensuring this analysis method can be executed quickly
         * enough to prevent stalls in the image acquisition pipeline. Otherwise, newly available
         * images will not be acquired and analyzed.
         *
         * <p>The image passed to this method becomes invalid after this method returns. The caller
         * should not store external references to this image, as these references will become
         * invalid.
         *
         * @param image image being analyzed VERY IMPORTANT: Analyzer method implementation must
         * call image.close() on received images when finished using them. Otherwise, new images
         * may not be received or the camera may stall, depending on back pressure setting.
         *
         */
        override fun analyze(image: ImageProxy) {
            // If there are no listeners attached, we don't need to perform analysis
            if (listeners.isEmpty()) {
                image.close()
                return
            }

            // Keep track of frames analyzed
            val currentTime = System.currentTimeMillis()
            frameTimestamps.push(currentTime)

            // Compute the FPS using a moving average
            while (frameTimestamps.size >= frameRateWindow) frameTimestamps.removeLast()
            val timestampFirst = frameTimestamps.peekFirst() ?: currentTime
            val timestampLast = frameTimestamps.peekLast() ?: currentTime
            framesPerSecond = 1.0 / ((timestampFirst - timestampLast) /
                    frameTimestamps.size.coerceAtLeast(1).toDouble()) * 1000.0

            // Analysis could take an arbitrarily long amount of time
            // Since we are running in a different thread, it won't stall other use cases

            lastAnalyzedTimestamp = frameTimestamps.first

            // Since format in ImageAnalysis is YUV, image.planes[0] contains the luminance plane
            val buffer = image.planes[0].buffer

            // Extract image data from callback object
            val data = buffer.toByteArray()

            // Convert the data into an array of pixel values ranging 0-255
            val pixels = data.map { it.toInt() and 0xFF }

            // Compute average luminance for the image
            val luma = pixels.average()

            // Call all listeners with new value
            listeners.forEach { it(luma) }

            image.close()
        }
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_TYPE = "image/jpeg"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }
}