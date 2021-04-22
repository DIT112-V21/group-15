package com.example.shadowapp


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import kotlinx.android.synthetic.main.activity_main.*
import org.eclipse.paho.client.mqttv3.*

class MainActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mediaPlayer = MediaPlayer.create(this, R.raw.hornsound)
        mediaPlayer?.setOnPreparedListener{
            println("READY")

        }
        getReady.setOnTouchListener { _, event ->
            handleTouch(event)
            Animatoo.animateSplit(this)
            startActivity(Intent( this, ControllerPage::class.java))
            finish()
            true

        }
    }
    private fun handleTouch(event: MotionEvent?) {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                println("Car horn shrieks!")
                Thread.sleep(15)
                mediaPlayer?.start()
                Thread.sleep(800)
                mediaPlayer?.pause()
                mediaPlayer?.stop()
                println("Car horn shut up.")
            }
        }
    }

}