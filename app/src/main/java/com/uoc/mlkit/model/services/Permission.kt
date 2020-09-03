package com.uoc.mlkit.model.services

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

const val CAMERA_PERMISSION_CODE = 100

class Permission(private val activityContext: Activity) {
    fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(activityContext, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(activityContext, arrayOf(permission), requestCode)
    }
}