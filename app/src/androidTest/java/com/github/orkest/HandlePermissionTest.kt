package com.github.orkest

import android.content.pm.PackageManager
import com.github.orkest.ui.MainActivity
import com.github.orkest.ui.PermissionConstants
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class HandlePermissionTest {
    @Test
    fun testHandlePermissionsResult() {
        val activity = mock(MainActivity::class.java)

        `when`(activity.handlePermissionsResult(
            PermissionConstants.AUDIO_PERMISSION_REQUEST_CODE,
            intArrayOf(PackageManager.PERMISSION_GRANTED)
        )).thenReturn("AUDIO_PERMISSION_GRANTED")

        `when`(activity.handlePermissionsResult(
            PermissionConstants.CAMERA_PERMISSION_REQUEST_CODE,
            intArrayOf(PackageManager.PERMISSION_DENIED)
        )).thenReturn("CAMERA_PERMISSION_DENIED")

        var requestCode = PermissionConstants.AUDIO_PERMISSION_REQUEST_CODE
        var grantResults = intArrayOf(PackageManager.PERMISSION_GRANTED)
        var result = activity.handlePermissionsResult(requestCode, grantResults)
        assertEquals("AUDIO_PERMISSION_GRANTED", result)

        requestCode = PermissionConstants.CAMERA_PERMISSION_REQUEST_CODE
        grantResults = intArrayOf(PackageManager.PERMISSION_DENIED)
        result = activity.handlePermissionsResult(requestCode, grantResults)
        assertEquals("CAMERA_PERMISSION_DENIED", result)
    }
}
