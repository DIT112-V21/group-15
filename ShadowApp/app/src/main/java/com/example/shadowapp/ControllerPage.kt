package com.example.shadowapp

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_option_page.*
import kotlinx.android.synthetic.main.controllerpage.*

class ControllerPage : ConnectionMovement() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.controllerpage)
        mMqttClient = MqttClient(applicationContext, MQTT_SERVER, TAG)
        mCameraView = findViewById(R.id.imageView)
        connectToMqttBroker()
        button5.setOnClickListener {
            val intent = Intent(this, VoiceCommand::class.java)
            startActivity(intent)

        }

        forward.setOnClickListener { forward() }
        reverse.setOnClickListener { reverse() }
        right.setOnClickListener {right()}
        left.setOnClickListener { left() }
        stop.setOnClickListener { stop() }
    }

}


