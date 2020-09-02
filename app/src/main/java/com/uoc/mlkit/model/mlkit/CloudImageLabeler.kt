package com.uoc.mlkit.model.mlkit

import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.uoc.mlkit.services.ImageAnalyzer
import io.reactivex.subjects.PublishSubject

class CloudImageLabeler(
    initOptions: ImageLabelerOptions? = null
): ImageAnalyzer(), ImageAnalyzer.ImageAnalyzerContract {
    private val defaultOptions = ImageLabelerOptions.DEFAULT_OPTIONS
    private var options: ImageLabelerOptions
    private val labelSubscription = PublishSubject.create<List<ImageLabel>>()

    private lateinit var inputImage: InputImage
    private lateinit var imageProxy: ImageProxy


    init {
        options = initOptions ?: defaultOptions
    }

    override fun imageProcessor(image: InputImage, imageProxy: ImageProxy) {
        inputImage = image
        this.imageProxy = imageProxy
    }

    open fun getData(): PublishSubject<List<ImageLabel>> {
        val labeler = ImageLabeling.getClient(options)

        labeler.process(inputImage)
            .addOnSuccessListener { labels -> labelSubscription.onNext(labels) }
            .addOnFailureListener { e ->  labelSubscription.onError(e) }
            .addOnCanceledListener { imageProxy.close() }


        return labelSubscription
    }
}