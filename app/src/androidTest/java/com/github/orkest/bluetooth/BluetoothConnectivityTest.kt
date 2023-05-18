package com.github.orkest.bluetooth


import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.github.orkest.bluetooth.data.BluetoothServiceManager
import com.github.orkest.bluetooth.ui.BluetoothActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BluetoothConnectivityTest {

    @get:Rule
    var permissionAudio: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.BLUETOOTH,
        android.Manifest.permission.BLUETOOTH_CONNECT,
        android.Manifest.permission.BLUETOOTH_SCAN,
        android.Manifest.permission.BLUETOOTH_ADVERTISE,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION)




    @Test
    fun testBluetoothReceptionHandling() {
        // create intent with Bluetooth ACTION_FOUND action
        val intent = android.content.Intent(android.bluetooth.BluetoothDevice.ACTION_FOUND)

        // instantiate a Mock BluetoothServiceManager
        val bluetoothServiceManager = BluetoothServiceManager()

        // launch bluetooth activity
        val scenario = androidx.test.core.app.ActivityScenario.launch(BluetoothActivity::class.java)
        scenario.onActivity { activity ->

            // call the onReceiveHandle method
            activity.onReceiveHandle(intent, bluetoothServiceManager)

            bluetoothServiceManager.devices.forEach { device ->
                // check if the device is in the list of devices
                assert(device.value == "test")
            }

        }

    }






}
