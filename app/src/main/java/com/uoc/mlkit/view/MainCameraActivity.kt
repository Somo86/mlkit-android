package com.uoc.mlkit.view

import com.uoc.mlkit.model.services.ImageAnalyzer

interface MainCameraActivity: CameraFragment.CameraFragmentListener  {
    var analyzer: ImageAnalyzer
}