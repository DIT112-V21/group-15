package com.example.shadowapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.controllerpage.*
import kotlinx.android.synthetic.main.get_ready.*
import org.eclipse.paho.client.mqttv3.*
import java.util.*

class ControllerPage(intent: Intent) : AppCompatActivity() {
    private val TAG = "ShadowApp"
    private val EXTERNAL_MQTT_BROKER = "aerostun.dev"
    private val MQTT_SERVER = "tcp://$EXTERNAL_MQTT_BROKER:1883"
    private val THROTTLE_CONTROL = "/smartcar/control/throttle"
    private val STEERING_CONTROL = "/smartcar/control/steering"
    private val MOVEMENT_SPEED = 70
    private val IDLE_SPEED = 0
    private val STRAIGHT_ANGLE = 0
    private val STEERING_ANGLE = 50
    private val QOS = 1
    private val IMAGE_WIDTH = 320 //320
    private val IMAGE_HEIGHT = 240 //240
    private var mMqttClient: MqttClient? = null
    private var isConnected = false
    private var mCameraView: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.controllerpage)
        mMqttClient = MqttClient(applicationContext, MQTT_SERVER, TAG)
        mCameraView = findViewById(R.id.imageView)
        connectToMqttBroker()
        button2.setOnClickListener {
            val intent = Intent(this, VoiceCommand::class.java)
            startActivity(intent)

        }

    }



    fun returnHome (view: View) {
        val obj = ProgressButton(this@ControllerPage, view)
        obj.ButtonActivated()
        onPause()
        startActivity(Intent(this, GetReady::class.java ))
        obj.ButtonFinished()
    }

    fun forward() {
        drive(MOVEMENT_SPEED, STRAIGHT_ANGLE, "Forward!")
    }
    fun left() {
        drive(MOVEMENT_SPEED, -STEERING_ANGLE, "Lefty!")
    }
    fun stop() {
        drive(IDLE_SPEED, STRAIGHT_ANGLE, "Stopped :(")
    }
    fun right() {
        drive(MOVEMENT_SPEED, STEERING_ANGLE, "Righty!")
    }
    fun reverse() {
        drive(-MOVEMENT_SPEED, STRAIGHT_ANGLE, "Reverse")
    }
    override fun onResume() {

        super.onResume()
        connectToMqttBroker()
    }
    override fun onPause() {

        super.onPause()
        mMqttClient?.disconnect(object: IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken) {
                Log.i(TAG, "Disconnected from broker")
            }

            override fun onFailure(asyncActionToken: IMqttToken, exception:Throwable) {}
        })
    }
    private fun connectToMqttBroker() {
        if (!isConnected)
        {
            mMqttClient?.connect(TAG, "", object: IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    isConnected = true
                    val successfulConnection = "Connected to MQTT broker"
                    Log.i(TAG, successfulConnection)
                    Toast.makeText(applicationContext, successfulConnection, Toast.LENGTH_SHORT)?.show()
                    mMqttClient?.subscribe("/smartcar/ultrasound/front", QOS, null)
                    mMqttClient?.subscribe("/smartcar/camera", QOS, null)
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception:Throwable) {
                    val failedConnection = "Failed to connect to MQTT broker"
                    Log.e(TAG, failedConnection)
                    Toast.makeText(applicationContext, failedConnection, Toast.LENGTH_SHORT)?.show()
                }
            }, object: MqttCallback {
                override fun connectionLost(cause:Throwable) {
                    isConnected = false
                    val connectionLost = "Connection to MQTT broker lost"
                    Log.w(TAG, connectionLost)
                    Toast.makeText(applicationContext, connectionLost, Toast.LENGTH_SHORT)?.show()
                }

                @Throws(Exception::class)
                override fun messageArrived(topic:String, message: MqttMessage) {
                    if (topic == "/smartcar/camera") {
                        val bm = Bitmap.createBitmap(IMAGE_WIDTH, IMAGE_HEIGHT, Bitmap.Config.ARGB_8888)
                        val payload = message.payload
                        val colors = IntArray(IMAGE_WIDTH * IMAGE_HEIGHT)
                        for (ci in colors.indices) {
                            val r = payload[3 * ci]
                            val g = payload[3 * ci + 1]
                            val b = payload[3 * ci + 2]
                            colors[ci] = Color.rgb(r.toInt(), g.toInt(), b.toInt()) //camera related
                        }
                        bm.setPixels(colors, 0, IMAGE_WIDTH, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT)
                        mCameraView?.setImageBitmap(bm)
                    } else {
                        Log.i(TAG, "[MQTT] Topic: $topic | Message: $message")
                    }
                }

                override fun deliveryComplete(token: IMqttDeliveryToken) {
                    Log.d(TAG, "Message delivered")
                }
            })
        }
    }
    private fun drive(throttleSpeed:Int?, steeringAngle:Int?, actionDescription:String) {
        if (!isConnected)
        {
            val notConnected = "Not connected (yet)"
            Log.e(TAG, notConnected)
            Toast.makeText(applicationContext, notConnected, Toast.LENGTH_SHORT).show()
            return
        }
        Log.i(TAG, actionDescription)
        mMqttClient?.publish(THROTTLE_CONTROL, throttleSpeed.toString(), QOS, null)
        mMqttClient?.publish(STEERING_CONTROL, steeringAngle.toString(), QOS, null)
    }






    }


