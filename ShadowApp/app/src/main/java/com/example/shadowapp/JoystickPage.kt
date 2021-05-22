/*package com.example.shadowapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlinx.android.synthetic.main.joystick_view.*
import org.eclipse.paho.client.mqttv3.*


class JoystickPage : AppCompatActivity() {
    // joystick taken from: https://github.com/controlwear/virtual-joystick-android

    private val TAG = "ShadowApp"
    private val EXTERNAL_MQTT_BROKER = "aerostun.dev"
    private val MQTT_SERVER = "tcp://$EXTERNAL_MQTT_BROKER:1883"
    private val THROTTLE_CONTROL = "/smartcar/control/throttle"
    private val STEERING_CONTROL = "/smartcar/control/steering"
    private val SPEED_TOPIC = "/smartcar/control/speed"
    private val TURNING_TOPIC = "/smartcar/control/turning"
    private val LIMITER = -1000
    private val MOVEMENT_SPEED = 70
    private val REVERSE_CAR_MOVEMENT = -1
    private val IDLE_SPEED = 0
    private val STRAIGHT_ANGLE = 0
    private val STEERING_ANGLE = 50
    private val QOS = 1
    private val IMAGE_WIDTH = 320 //320
    private val IMAGE_HEIGHT = 240 //240
    private var mMqttClient: MqttClient? = null
    private var isConnected = false
    private var mCameraView: ImageView? = null


    private var mTextViewAngleLeft: TextView? = null //joystick
    private var mTextViewStrengthLeft: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.joystick_view)
        mMqttClient = MqttClient(applicationContext, MQTT_SERVER, TAG)
        mCameraView = findViewById(R.id.imageView)

        mTextViewAngleLeft = findViewById<View>(R.id.textView_angle_left) as TextView //joystick
        mTextViewStrengthLeft =
            findViewById<View>(R.id.textView_strength_left) as TextView //joystick

        connectToMqttBroker()

        val joystick = findViewById<View>(R.id.joystickView_left) as JoystickView //joystick
        joystick.setOnMoveListener { angle, strength ->
            mTextViewAngleLeft!!.text = "$angle°"
            mTextViewStrengthLeft!!.text = "$strength%"
            var previousAngle: Int = LIMITER
            var previousSpeed: Int = LIMITER

            fun onMove(angle: Int, strength: Int) {
                val adjustedAngle = angleChange(angle)
                val adjustedSpeed = speedChange(strength, angle)
                turnCar(adjustedSpeed, adjustedAngle, previousAngle, previousSpeed)
            }
        }
    }

    private fun angleChange(angle: Int): Int {
        val adjustedAngle: Int
        adjustedAngle = if (angle >= 90 && angle <= 180) { // left
            90 - angle
        } else if (angle < 90 && angle >= 0) { // right
            90 - angle
        } else if (angle > 0 && angle >= 270) { // back right
            angle - 270
        } else { // back left
            angle - 270
        }
        return adjustedAngle
    }

    private fun speedChange(strength: Int, angle: Int): Int {
        val adjustedSpeed: Double
        if (angle <= 180) {
            adjustedSpeed = strength * 0.6
        } else {
            adjustedSpeed = strength * 0.6 * REVERSE_CAR_MOVEMENT
        }
        return adjustedSpeed.toInt()
    }

    private fun turnCar(
        adjustedSpeed: Int,
        adjustedAngle: Int,
        previousAngle: Int,
        previousSpeed: Int
    ) {
        var adjustedAngle = adjustedAngle
        if (adjustedAngle != previousAngle || adjustedSpeed != previousSpeed) {
            if (adjustedSpeed == 0) adjustedAngle = 0
            mMqttClient?.publish(TURNING_TOPIC, Integer.toString(adjustedAngle), QOS, null)
            mMqttClient?.publish(SPEED_TOPIC, Integer.toString(adjustedSpeed), QOS, null)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this@JoystickPage, MainActivity::class.java) //possible issue here
        intent.putExtra("Restrict back", true)
        startActivity(intent)
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

    private fun connectToMqttBroker() {
        if (!isConnected) {
            mMqttClient?.connect(TAG, "", object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    isConnected = true
                    val successfulConnection = "Connected to MQTT broker"
                    Log.i(TAG, successfulConnection)
                    Toast.makeText(applicationContext, successfulConnection, Toast.LENGTH_SHORT)
                        ?.show()
                    mMqttClient?.subscribe("/smartcar/ultrasound/front", QOS, null)
                    mMqttClient?.subscribe("/smartcar/camera", QOS, null)
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    val failedConnection = "Failed to connect to MQTT broker"
                    Log.e(TAG, failedConnection)
                    Toast.makeText(applicationContext, failedConnection, Toast.LENGTH_SHORT)?.show()
                }
            }, object : MqttCallback {
                override fun connectionLost(cause: Throwable) {
                    isConnected = false
                    val connectionLost = "Connection to MQTT broker lost"
                    Log.w(TAG, connectionLost)
                    Toast.makeText(applicationContext, connectionLost, Toast.LENGTH_SHORT)?.show()
                }

                @Throws(Exception::class)
                override fun messageArrived(topic: String, message: MqttMessage) {
                    if (topic == "/smartcar/camera") {
                        val bm = Bitmap.createBitmap(
                            IMAGE_WIDTH,
                            IMAGE_HEIGHT,
                            Bitmap.Config.ARGB_8888
                        )
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

    private fun drive(throttleSpeed: Int?, steeringAngle: Int?, actionDescription: String) {
        if (!isConnected) {
            val notConnected = "Not connected (yet)"
            Log.e(TAG, notConnected)
            Toast.makeText(applicationContext, notConnected, Toast.LENGTH_SHORT).show()
            return
        }
        Log.i(TAG, actionDescription)
        mMqttClient?.publish(THROTTLE_CONTROL, throttleSpeed.toString(), QOS, null)
        mMqttClient?.publish(STEERING_CONTROL, steeringAngle.toString(), QOS, null)
    }

}*/
