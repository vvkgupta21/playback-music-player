package com.example.musicplayer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.widget.Toast

class MyPlayService : Service(), MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
    MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {

    private lateinit var mediaPlayer : MediaPlayer
    private var link : String? = null
    private lateinit var musicStoppedListener: MusicStoppedListener


    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        mediaPlayer.setOnCompletionListener(this)
        mediaPlayer.setOnPreparedListener(this)
        mediaPlayer.setOnErrorListener(this)
        mediaPlayer.setOnSeekCompleteListener(this)
        mediaPlayer.setOnInfoListener(this)
        mediaPlayer.setOnBufferingUpdateListener(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        link = intent?.getStringExtra("AudiLink")
        mediaPlayer.reset()
        musicStoppedListener = ApplicationClass.appContext as MusicStoppedListener
        if (!mediaPlayer.isPlaying){
            try {
                mediaPlayer.setDataSource(link)
                mediaPlayer.prepareAsync()
            }catch (e:Exception)
            {
                Toast.makeText(this, "Error" + e.message, Toast.LENGTH_SHORT).show()
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying){
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }

    override fun onCompletion(p0: MediaPlayer?) {
        if (p0!!.isPlaying){
            p0.stop()
        }
        musicStoppedListener.onMusicStopped()
        stopSelf()
    }

    override fun onPrepared(mp: MediaPlayer?) {
        if (!mp!!.isPlaying){
            mp.start()
        }
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        when(what){
            MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK -> Toast.makeText(this, "MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK", Toast.LENGTH_SHORT).show()
            MediaPlayer.MEDIA_ERROR_SERVER_DIED -> Toast.makeText(this, "MEDIA_ERROR_SERVER_DIED", Toast.LENGTH_SHORT).show()
            MediaPlayer.MEDIA_ERROR_UNKNOWN -> Toast.makeText(this, "MEDIA_ERROR_UNKNOWN", Toast.LENGTH_SHORT).show()
        }
        return false
    }

    override fun onSeekComplete(p0: MediaPlayer?) {
        TODO("Not yet implemented")
    }

    override fun onInfo(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun onBufferingUpdate(p0: MediaPlayer?, p1: Int) {
        Toast.makeText(this, "Buffering", Toast.LENGTH_SHORT).show()
    }
}