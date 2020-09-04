package com.uoc.mlkit.model.mlkit

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.camera.core.ImageProxy
import androidx.camera.core.internal.utils.ImageUtil
import com.google.mlkit.vision.common.InputImage
import com.uoc.mlkit.model.services.ImageAnalyzer
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class CustomImageLabeler(
    val classifier: ImageClassifier
): ImageAnalyzer(), ImageAnalyzer.ImageAnalyzerContract {

    val labelSubscription = PublishSubject.create<List<String>>()
    var lastAnalizedTimestamp = 0.toLong()

    @SuppressLint("RestrictedApi")
    private fun mediaToBitmap(image: ImageProxy): Bitmap? {
        var bitmap: Bitmap? = null
        val bytes = ImageUtil.imageToJpegByteArray(image)
        if(bytes != null) {
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            bitmap = Bitmap.createScaledBitmap(bitmap,
                ImageClassifier.DIM_IMG_SIZE_X,
                ImageClassifier.DIM_IMG_SIZE_Y, true)
        }

        return bitmap
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun imageProcessor(image: InputImage, imageProxy: ImageProxy) {
        val currentTimestamp = System.currentTimeMillis()

        if(currentTimestamp - lastAnalizedTimestamp >= TimeUnit.SECONDS.toMillis(2)) {

            val bitmap = mediaToBitmap(imageProxy)
            if (bitmap != null) {
                try {
                    val text = classifier.classifyFrame(bitmap)
                    labelSubscription.onNext(text.split("\n"))
                } catch (e: Throwable) {
                    labelSubscription.onError(e)
                }
            }

            lastAnalizedTimestamp = currentTimestamp

        } else {
            imageProxy.close()
        }
    }
}