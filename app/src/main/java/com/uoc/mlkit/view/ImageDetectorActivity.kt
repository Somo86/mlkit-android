package com.uoc.mlkit.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.google.android.material.chip.Chip
import com.google.mlkit.vision.objects.DetectedObject
import com.uoc.mlkit.R
import com.uoc.mlkit.model.mlkit.CloudObjectTrack
import com.uoc.mlkit.model.services.ImageAnalyzer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class ImageDetectorActivity : AppCompatActivity(), MainCameraActivity {

    override lateinit var analyzer: ImageAnalyzer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detector)

        analyzer = CloudObjectTrack()
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
        (analyzer as CloudObjectTrack)
            .labelSubscription
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith (object: DisposableObserver<List<DetectedObject>>() {
                override fun onNext(t: List<DetectedObject>) {
                    val textInfo = findViewById<LinearLayout>(R.id.info_text)
                    textInfo.removeAllViews()
                    for (item in t) {
                        for (label in item.labels) {
                            val chip = Chip(this@ImageDetectorActivity)
                            chip.text = label.text
                            textInfo.addView(chip)
                        }
                    }
                }

                override fun onComplete() {}
                override fun onError(e: Throwable) {}
            }
                 
            )
    }
}
