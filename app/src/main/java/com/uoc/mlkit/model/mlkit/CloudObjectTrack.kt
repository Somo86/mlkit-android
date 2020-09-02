package com.uoc.mlkit.model.mlkit

import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.uoc.mlkit.services.ImageAnalyzer
import io.reactivex.subjects.PublishSubject

class CloudObjectTrack : ImageAnalyzer(), ImageAnalyzer.ImageAnalyzerContract {

    val labelSubscription = PublishSubject.create<List<DetectedObject>>()
    val options = ObjectDetectorOptions.Builder()
        .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
        .enableClassification()  // Optional
        .build()

    override fun imageProcessor(image: InputImage, imageProxy: ImageProxy) {
        val objectDetector = ObjectDetection.getClient(options)

        objectDetector.process(image)
            .addOnSuccessListener {
                labels -> labelSubscription.onNext(labels)
            }
            .addOnFailureListener { e ->  labelSubscription.onError(e) }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
}