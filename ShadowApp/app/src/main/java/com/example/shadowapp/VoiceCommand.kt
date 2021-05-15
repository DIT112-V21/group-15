package com.example.shadowapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_voice_command.*
import org.eclipse.paho.client.mqttv3.*
import java.util.*

class VoiceCommand : AppCompatActivity() {
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

            when (vCommand){
                "move forward"  -> forward()
                "turn right" -> right()
                "turn left" -> left()
                "move backwards" -> reverse()
                "stop" -> stop()
                else -> Toast.makeText(applicationContext,"Invalid Command",Toast.LENGTH_SHORT).show()

            }
        }
}
    private fun forward() {
        drive(MOVEMENT_SPEED, STRAIGHT_ANGLE, "Forward!")
    }
    private fun left() {
        drive(MOVEMENT_SPEED, -STEERING_ANGLE, "Lefty!")
    }
    private fun stop() {
        drive(IDLE_SPEED, STRAIGHT_ANGLE, "Stopped :(")
    }
    private fun right() {
        drive(MOVEMENT_SPEED, STEERING_ANGLE, "Righty!")
    }
    private fun reverse() {
        drive(-MOVEMENT_SPEED, STRAIGHT_ANGLE, "Reverse")
    }
    override fun onResume() {

        super.onResume()
        connectToMqttBroker()
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