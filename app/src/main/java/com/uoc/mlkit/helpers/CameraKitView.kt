package com.uoc.mlkit.helpers

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.util.Log
import android.view.WindowManager
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.Executors


class CameraKit(
    private val viewFinder: PreviewView?,
    private val CustomImageAnalyzer: ImageAnalysis.Analyzer,
    private val context: Context
) {

    var rotation = (context.getSystemService(WINDOW_SERVICE) as WindowManager).defaultDisplay.rotation

    private var cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    private val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private val preview = buildPreview()
    private val imageAnalysis = ImageAnalysis.Builder()
        .build()
        .also {
            it.setAnalyzer(
                Executors.newSingleThreadExecutor(),
                CustomImageAnalyzer
            )
        }
    private val imageCapture = buildImageCapture()


    fun onStart() {
        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    context as LifecycleOwner, cameraSelector, imageAnalysis, imageCapture, preview)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(context))
    }

    private fun buildPreview(): Preview {
        return Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(viewFinder?.createSurfaceProvider())
            }
    }

    private fun buildImageCapture(): ImageCapture {
        return ImageCapture.Builder()
            .setTargetRotation(rotation)
            .build()
    }

}