package com.uoc.mlkit.services

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage

open class ImageAnalyzer: ImageAnalysis.Analyzer {

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        val degrees = imageProxy.imageInfo.rotationDegrees

        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, degrees)
            imageProcessor(image, imageProxy)
        }
    }

    open fun imageProcessor(image: InputImage, imageProxy: ImageProxy) {}

    interface ImageAnalyzerContract{
        fun imageProcessor(image: InputImage, imageProxy: ImageProxy) {}
    }
}