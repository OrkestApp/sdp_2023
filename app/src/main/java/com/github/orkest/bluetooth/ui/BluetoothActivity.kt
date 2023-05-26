package com.github.orkest.bluetooth.ui

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.orkest.R
import com.github.orkest.bluetooth.data.BluetoothServiceManager
import com.github.orkest.bluetooth.data.OrkestDevice
import com.github.orkest.bluetooth.domain.BluetoothConstants
import com.github.orkest.bluetooth.domain.BluetoothInterface
import com.github.orkest.bluetooth.domain.Device
import com.github.orkest.bluetooth.ui.ui.theme.OrkestTheme
import com.github.orkest.data.Constants
import com.github.orkest.domain.FireStoreDatabaseAPI
import com.github.orkest.ui.MainActivity
import com.github.orkest.ui.profile.ProfileActivity
import com.github.orkest.ui.profile.ProfileViewModel
import com.github.orkest.ui.search.SearchUserView
import java.util.concurrent.CompletableFuture

class BluetoothActivity(private val mock:Boolean =false, devices: MutableList<Device> = mutableListOf()) : ComponentActivity() {

    private var bluetoothServiceManager: BluetoothInterface? = null
    private var sender = true
    private var update = mutableStateOf(devices)

    private var requestBluetooth = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            //granted
            Log.d("BluetoothActivity", "Bluetooth granted")
        } else {
            //deny
            Log.d("BluetoothActivity", "Bluetooth denied")
        }
    }

    var discovery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
        // discoverable
        bluetoothServiceManager?.discoverDevices(this, receiver, requestBluetooth)
    }


    fun onReceiveHandle(intent: Intent, bluetoothServiceManager: BluetoothInterface? = this.bluetoothServiceManager) {
        intent.action?.let { Log.d("BluetoothActivity", it) }
        when(intent.action) {

            BluetoothDevice.ACTION_FOUND -> {
                Log.d("BluetoothActivity", "BluetoothDevice.ACTION_FOUND")
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                val device: BluetoothDevice? =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                val deviceName = if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    return
                } else {
                    if (device != null && device.name != "") {
                        bluetoothServiceManager?.addDevice(OrkestDevice(device))
                        Log.d("BluetoothActivity", "Device name: ${device?.name}")
                        if (bluetoothServiceManager != null ) {
                            update.value = bluetoothServiceManager.devices
                        }
                        if (bluetoothServiceManager != null) {
                            Log.d("LIST", bluetoothServiceManager.devices.toString())
                        }
                    }
                    Log.d("BluetoothActivity", "nothing")

                }
            }
        }
    }

    fun setServiceManager(manager: BluetoothInterface) {
        bluetoothServiceManager = manager
    }

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            intent.action?.let { Log.d("BluetoothActivity", it) }
            onReceiveHandle(intent)
        }
    }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // -------------------------------------------------------------------------------------

        setContent {
            OrkestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    BluetoothActivityStart(activity = this)
                }
            }
        }
    }

    //----------------------------------------UI-------------------------------------------

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {

                BluetoothConstants.MESSAGE_WRITE -> {
                    // construct a string from the buffer
                }
                BluetoothConstants.MESSAGE_READ -> {
                    // construct a string from the valid bytes in the buffer
                    val msgReceived = String(msg.obj as ByteArray, 0, msg.arg1)
                    val follow = ProfileViewModel(msgReceived)
                    follow.updateCurrentUserFollowings(true)
                    follow.updateUserFollowers(true)
                    Toast.makeText(this@BluetoothActivity,
                        "You and $msgReceived are now Friends",Toast.LENGTH_LONG).show()

                }
                BluetoothConstants.MESSAGE_TOAST -> {
                    val msgReceived= msg.data.getString("toast").toString()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @Composable
    fun BluetoothActivityStart(
        bluetoothServiceManager : BluetoothInterface = BluetoothServiceManager(handler),
        activity: BluetoothActivity){
        // check for permissions
        val permissions = bluetoothServiceManager.checkPermissionGranted(LocalContext.current)
        if (!permissions && !mock) {
            bluetoothServiceManager.askBluetoothPermission(activity)
        }
        val discoverableIntent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
            putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
        }
        activity.setServiceManager(bluetoothServiceManager)
        if(!mock){
        activity.discovery.launch(discoverableIntent)}



        createUI(bluetoothServiceManager)
    }


    @Composable
    fun createUI(bluetoothServiceManager: BluetoothInterface) {
        val recompose by rememberUpdatedState(update)

        //bluetoothServiceManager.acceptConnections()
        Box() {
            Column {
                //Title of the page
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(start = 20.dp),
                    text = "Follow Nearby Users",
                    style = TextStyle(fontSize = 35.sp, fontFamily = Constants.FONT_MARKER),
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(20.dp))

                //List of nearby users
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(start = 20.dp),
                    text = "Click on a user to follow them",
                    style = TextStyle(fontSize = 20.sp, fontFamily = Constants.FONT_MARKER),
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(20.dp))

                

                LazyColumn {
                    items(update.value) { device ->
                        if (device.getName() != "") {
                            Button(onClick = {
                                if (sender) {
                                    bluetoothServiceManager.connectToDevice(device)
                                }
                            }) {
                                Text(text = device.getName())
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            //Button to start Receiving -------

            Column(Modifier.align(Alignment.BottomCenter)
                .padding(20.dp)) {
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(10.dp)
                        .testTag("RECEIVE"),
                    backgroundColor = Color.White,
                    onClick = {
                        bluetoothServiceManager.acceptConnections()
                    }) {
                    Column() {
                        Image(
                            painter = painterResource(id = R.drawable.bluetooth),
                            contentDescription = "RECEIVE"
                        )
                    }
                }
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "START RECEIVING",
                    style = TextStyle(fontSize = 20.sp),
                    color = Color.Black
                )
            }
                //End button Receiving -------------


        }
    }


    @Composable
    fun toastinette(){
        Toast.makeText(LocalContext.current,"You",Toast.LENGTH_SHORT).show()
    }




}














