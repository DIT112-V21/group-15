package com.example.shadowapp

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_voice_command.*
import java.util.*

class VoiceCommand : ConnectionMovement() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice_command)
        mMqttClient = MqttClient(applicationContext, MQTT_SERVER, TAG)
        mCameraView = findViewById(R.id.imageView)
        connectToMqttBroker()
        val actionBar = supportActionBar

        actionBar!!.title = "Voice Command"
        actionBar.setDisplayHomeAsUpEnabled(true)
        imageButton.setOnClickListener{
            speechToText()
        }
    }
    private fun speechToText() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)){
            Toast.makeText(this,"Failed Recognizing", Toast.LENGTH_SHORT).show()
        } else{
            val r = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            r.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            r.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
            r.putExtra(RecognizerIntent.EXTRA_PROMPT,"Command The Car")

            startActivityForResult(r,100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 || data != null) {
            val result: ArrayList<String> = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
            println(result[0])
            val vCommand=result[0]
            textView.text = result[0]

            if(vCommand.contains("forward")){
                forward()
            } else if (vCommand.contains("turn right")){
                right()
            } else if (vCommand.contains("turn left")){
                left()
            } else if (vCommand.contains("backward")){
                reverse()
            } else if (vCommand.contains("stop")){
                stop()
            }else{
                Toast.makeText(applicationContext,"Invalid Command",Toast.LENGTH_SHORT).show()
            }

        }
    }


}