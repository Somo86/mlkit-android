package com.uoc.mlkit.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.google.android.material.chip.Chip
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.objects.DetectedObject
import com.uoc.mlkit.R
import com.uoc.mlkit.model.mlkit.CloudImageLabeler
import com.uoc.mlkit.model.mlkit.CloudObjectTrack
import com.uoc.mlkit.model.services.ImageAnalyzer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class ImageLabelingActivity : AppCompatActivity(), MainCameraActivity {

    override lateinit var analyzer: ImageAnalyzer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_labeling)

        analyzer = CloudImageLabeler()
    }

    override fun onStart() {
        super.onStart()
        loadCameraFragment()
    }

    override fun onCameraPermissionResult(hasPermission: Boolean) {
        if(hasPermission) startCameraTrack()
    }

    private fun loadCameraFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val fragment = CameraFragment()
        fragmentTransaction.add(R.id.camera_fragment_container, fragment)
        fragmentTransaction.commit()
    }

    private fun startCameraTrack() {
        (analyzer as CloudImageLabeler)
            .labelSubscription
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith (object: DisposableObserver<List<ImageLabel>>() {
                override fun onNext(t: List<ImageLabel>) {
                    val textInfo = findViewById<LinearLayout>(R.id.info_text)
                    textInfo.removeAllViews()
                    for (label in t) {
                        val chip = Chip(this@ImageLabelingActivity)
                        chip.text = label.text
                        textInfo.addView(chip)
                    }
                }

                override fun onComplete() {}
                override fun onError(e: Throwable) {}
            }
            )
    }
}
