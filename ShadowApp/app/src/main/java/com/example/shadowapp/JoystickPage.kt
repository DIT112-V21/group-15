package com.example.shadowapp


import android.os.Bundle
import android.view.View
import android.widget.TextView
import io.github.controlwear.virtual.joystick.android.JoystickView


class JoystickPage : ConnectionMovement() {
    // joystick taken from: https://github.com/controlwear/virtual-joystick-android

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
        return if (angle >= 90 && angle <= 180) { // left
            90 - angle
        } else if (angle < 90 && angle >= 0) { // right
            90 - angle
        } else if (angle > 0 && angle >= 270) { // back right
            angle - 270
        } else { // back left
            angle - 270
        }
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

    }
