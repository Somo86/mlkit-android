package com.uoc.mlkit.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.View
import com.google.mlkit.vision.objects.DetectedObject

class DrawBox(context: Context, var visionObjects: List<DetectedObject>): View(context) {
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val pen = Paint()
        for (item in visionObjects) {
            // draw bounding box
            Log.v("bound", item.boundingBox.toString())
            pen.color = Color.RED
            pen.strokeWidth = 8F
            pen.style = Paint.Style.STROKE
            val box = item.boundingBox
            canvas?.drawRect(box, pen)
        }
    }
}