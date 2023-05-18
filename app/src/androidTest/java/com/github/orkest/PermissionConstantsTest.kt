package com.github.orkest

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.orkest.ui.PermissionConstants
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import android.Manifest
import android.app.Instrumentation
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class PermissionConstantsTest {

    private lateinit var context: Context

    @Rule
    val cameraPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA)
    @Rule
    val audioPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.RECORD_AUDIO)

    @Before
    fun setup() {
        context = getApplicationContext()
    }

    @Test
    fun recordPermissionGranted_shouldReturnTrueIfPermissionGranted() {
        // Arrange
        val expected = PackageManager.PERMISSION_GRANTED

        // Act
        val permission = PermissionConstants.recordPermissionGranted(context)
        val result = if(permission) 0 else -1
        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun cameraPermissionIsGranted_shouldReturnTrueIfPermissionGranted() {
        // Arrange
        val expected = PackageManager.PERMISSION_GRANTED

        // Act
        val permission = PermissionConstants.cameraPermissionIsGranted(context)
        val result = if(permission) 0 else -1

        // Assert
        assertEquals(expected, result)
    }

}
