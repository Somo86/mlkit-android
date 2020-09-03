package com.uoc.mlkit.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.view.PreviewView
import androidx.fragment.app.Fragment
import com.uoc.mlkit.R
import com.uoc.mlkit.helpers.CameraKit
import com.uoc.mlkit.model.services.CAMERA_PERMISSION_CODE
import com.uoc.mlkit.model.services.Permission

class CameraFragment: Fragment() {

    private lateinit var CameraKitView: CameraKit
    private lateinit var permission: Permission
    private lateinit var activity: MainCameraActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.camera_fragment_layout, container, false)
    }

    override fun onStart() {
        super.onStart()

        val viewFinder = view?.findViewById<PreviewView>(R.id.cameraPreview)
        this.activity = getActivity() as MainCameraActivity

        CameraKitView = CameraKit(viewFinder, activity.analyzer, activity as Context)
        permission = Permission(activity as Activity)

        askForPermission()
    }

    private fun askForPermission() {
        if(!permission.hasPermission(android.Manifest.permission.CAMERA)) {
            permission.requestPermission(android.Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)
        } else {
            CameraKitView.onStart()
            activity.onCameraPermissionResult(true)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(permission.hasPermission(android.Manifest.permission.CAMERA)) {
            CameraKitView.onStart()
            activity.onCameraPermissionResult(true)
        }

    }

    interface CameraFragmentListener {
        fun onCameraPermissionResult(hasPermission: Boolean)
    }
}