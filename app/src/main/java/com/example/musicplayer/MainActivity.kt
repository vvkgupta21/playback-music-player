package com.example.musicplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),MusicStoppedListener {

    private lateinit var dataBinding: ActivityMainBinding
    val audioLink =
        "https://dl.dropbox.com/scl/fi/av97elfixfzanf21nmkct/Tokyo-Drift_320-PagalWorld.mp3?rlkey=dskdu9yabanqjncvfw4hw06lp&dl=0"
    var musicPlayer = false
    lateinit var serviceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        dataBinding.playStop.setBackgroundResource(R.drawable.play_circle_filled_48)
        serviceIntent = Intent(this, MyPlayService::class.java)

        ApplicationClass.appContext = this

        setOnClickListener()
    }

    private fun setOnClickListener() {
        dataBinding.playStop.setOnClickListener {
            if (!musicPlayer) {
                playAudio()
                dataBinding.playStop.setBackgroundResource(R.drawable.pause_48)
                musicPlayer = true
            } else {
                stopAudio()
                dataBinding.playStop.setBackgroundResource(R.drawable.play_circle_filled_48)
            }
        }
    }

    private fun stopAudio() {
        try {
            stopService(serviceIntent)
        }catch (e:Exception){
            Toast.makeText(this, "Error" + e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun playAudio() {
        serviceIntent.putExtra("AudiLink", audioLink)
        try {
            startService(serviceIntent)
        } catch (e: Exception) {
            Toast.makeText(this, "Error" + e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMusicStopped() {
        dataBinding.playStop.setBackgroundResource(R.drawable.play_circle_filled_48)
        musicPlayer = false
    }
}