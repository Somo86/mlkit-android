package com.uoc.mlkit.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.view.PreviewView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.mlkit.vision.objects.defaults.PredefinedCategory
import com.uoc.mlkit.R
import com.uoc.mlkit.helpers.CameraKit
import com.uoc.mlkit.model.mlkit.CloudImageLabeler
import com.uoc.mlkit.model.mlkit.CloudObjectTrack
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity()
{
    private lateinit var CameraKitView: CameraKit
    private lateinit var CloudImageLabeler: CloudImageLabeler
    private lateinit var CloudObjectTrack: CloudObjectTrack

    private lateinit var capture_image_btn: FloatingActionButton

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        val viewFinder = findViewById<PreviewView>(R.id.cameraPreview)
        val textInfo = findViewById<TextView>(R.id.info_text)

        CloudObjectTrack = CloudObjectTrack()

        CameraKitView =
            CameraKit(viewFinder, CloudObjectTrack,this)


        CameraKitView.onStartPreview()

        CloudObjectTrack
            .labelSubscription
            .subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeBy (
                onNext = {
                    val drawingView = DrawBox(this, it)
                    for (item in it) {
                        for (label in item.labels) {
                            if(PredefinedCategory.PLANT == label.text) {
                                textInfo.text = "Yes is a plant!!!!"
                            } else {
                                textInfo.text = "Sorry :("
                            }

                        }
                    }
                }
            )

    }

}
