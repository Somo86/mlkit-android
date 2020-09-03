package com.uoc.mlkit.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.uoc.mlkit.R


class MainActivity : AppCompatActivity()
{

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val detectionViewBtn = findViewById<Button>(R.id.detection_view)
        val labelingViewBtn = findViewById<Button>(R.id.labeling_view)

        detectionViewBtn.setOnClickListener {
            val intent = Intent(this, ImageDetectorActivity::class.java)
            startActivity(intent)
        }

        labelingViewBtn.setOnClickListener {
            val intent = Intent(this, ImageLabelingActivity::class.java)
            startActivity(intent)
        }
    }

}
