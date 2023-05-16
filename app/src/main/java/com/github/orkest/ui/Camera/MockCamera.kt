package com.github.orkest.ui.Camera

import android.os.Bundle
import androidx.activity.ComponentActivity

/**
 * This class represents a mock camera component activity for testing
 */
class MockCamera : ComponentActivity() {
    lateinit var cameraView: CameraView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraView = CameraView()
    }
}