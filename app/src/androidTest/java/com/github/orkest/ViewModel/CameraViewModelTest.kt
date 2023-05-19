package com.github.orkest.ViewModel

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.github.orkest.ui.Camera.CameraViewModel
import junit.framework.TestCase.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class CameraViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var cameraViewModel: CameraViewModel
    private lateinit var context: Context

    @Before
    fun setUp() {
        cameraViewModel = CameraViewModel()
        context = ApplicationProvider.getApplicationContext()
    }

    /**
     * This test checks that the cameraViewModel can generate an image preview
     * and return a valid URI for the preview.
     */
    @Test
    fun testImagePreview() {
        // Create a latch with a count of 1 to synchronize the test with the image preview callback
        val latch = CountDownLatch(1)

        cameraViewModel.captureImage({ uri ->
            assertNotNull(uri)
            latch.countDown()
        }, context)

        latch.await(5, TimeUnit.SECONDS)
    }

    /**
     * This test checks that the cameraViewModel can save a bitmap as a JPEG file in the Pictures directory of the device.
     */
    @Test
    fun testSavePicture() {
        // Create a bitmap object with dimensions 100 x 100
        val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        // Save the bitmap as a JPEG file and get the URI for the saved file
        val imageUri = saveBitmap(bitmap)

        runOnUiThread{
            cameraViewModel.savePicture(imageUri, context)
        }

        // Define the projection of the query
        val projection = arrayOf(
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.RELATIVE_PATH
        )

        //Define the selection for the query
        val selection = "${MediaStore.Images.Media.DISPLAY_NAME} = ?"
        val selectionArgs = arrayOf("captured_image.jpg")

        // Query the MediaStore for the saved file image
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )

        assertNotNull(cursor)
        if (cursor != null) { assertTrue(cursor.moveToFirst()) }

        // Get the MIME type of the saved image from the cursor and assert that it is "image/jpeg"
        val mimeTypeIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)
        val mimeType = mimeTypeIndex?.let { cursor.getString(it) }
        assertEquals("image/jpeg", mimeType)

        cursor?.close()
    }

    /**
     * This function saves a bitmap as a JPEG file in the Pictures directory of the device
     * and returns the URI of the saved file.
     */
    private fun saveBitmap(bitmap: Bitmap): Uri {
        // Define the content values for the file
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "test_image.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
        val contentResolver = context.contentResolver

        // Insert the file in the MediaStore and get the URI of the saved file
        val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        // Open an output stream to the file and compress the bitmap as JPEG with quality 100
        contentResolver.openOutputStream(imageUri!!)?.use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }

        return imageUri
    }
}
