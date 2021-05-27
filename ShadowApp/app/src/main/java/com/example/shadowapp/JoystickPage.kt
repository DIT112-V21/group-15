package com.example.shadowapp

import android.os.Bundle
import android.view.View
import android.widget.TextView
import io.github.controlwear.virtual.joystick.android.JoystickView

class JoystickPage : ConnectionMovement() {

        private var mTextViewAngleLeft: TextView? = null
        private var mTextViewStrengthLeft: TextView? = null
        private var leftJoystick: JoystickView? = null

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.joystick_view)
                mMqttClient = MqttClient(applicationContext, MQTT_SERVER, TAG)
                mCameraView = findViewById(R.id.imageView)
                connectToMqttBroker()

                val joystick = findViewById<View>(R.id.joystickView_left) as JoystickView //joystick
                joystick.setOnMoveListener { angle, strength ->
                        //mTextViewAngleLeft!!.text = "$angle°"   commenting it out will break the code
                        //mTextViewStrengthLeft!!.text = "$strength%"  commenting it out will break the code
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

        fun JoystickPage(controllerActivity: ConnectionMovement) {
                mTextViewAngleLeft = controllerActivity.findViewById(R.id.textView_angle_left)
                mTextViewStrengthLeft = controllerActivity.findViewById(R.id.textView_strength_left)

                leftJoystick = controllerActivity.findViewById(R.id.joystickView_left)

                val tmpLeftAngleTextView = mTextViewAngleLeft
                val tmpLeftStrenghtTextView = mTextViewStrengthLeft

                leftJoystick!!.setOnMoveListener { angle, strength ->
                        tmpLeftAngleTextView!!.text = "$angle°"
                        tmpLeftStrenghtTextView!!.text = "$strength%"
                }
        }

}