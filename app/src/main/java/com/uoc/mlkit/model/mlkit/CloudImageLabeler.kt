package com.uoc.mlkit.model.mlkit

import java.util.concurrent.TimeUnit
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.uoc.mlkit.model.services.ImageAnalyzer
import io.reactivex.subjects.PublishSubject

class CloudImageLabeler(
    initOptions: ImageLabelerOptions? = null
): ImageAnalyzer(), ImageAnalyzer.ImageAnalyzerContract {
    private val defaultOptions = ImageLabelerOptions.DEFAULT_OPTIONS
    private var options: ImageLabelerOptions
    val labelSubscription = PublishSubject.create<List<ImageLabel>>()

    var lastAnalizedTimestamp = 0.toLong()

    init {
        options = initOptions ?: defaultOptions
    }

    override fun imageProcessor(image: InputImage, imageProxy: ImageProxy) {
        val labeler = ImageLabeling.getClient(options)
        val currentTimestamp = System.currentTimeMillis()

        if(currentTimestamp - lastAnalizedTimestamp >= TimeUnit.SECONDS.toMillis(2)) {
            lastAnalizedTimestamp = currentTimestamp
            labeler.process(image)
                .addOnSuccessListener { labels -> labelSubscription.onNext(labels) }
                .addOnFailureListener { e ->  labelSubscription.onError(e) }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }


}