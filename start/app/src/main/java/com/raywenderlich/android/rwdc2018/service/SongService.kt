package com.raywenderlich.android.rwdc2018.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import com.raywenderlich.android.rwdc2018.R
import com.raywenderlich.android.rwdc2018.app.RWDC2018Application
import com.raywenderlich.android.rwdc2018.app.SongUtils
import com.raywenderlich.android.rwdc2018.ui.main.MainActivity

/// foreground service play song in background 
class SongService : Service() {

    companion object{
        private const val CHANNEL_ID = "media_playback_channel"
    }

    private lateinit var player: MediaPlayer

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        RWDC2018Application.isPlayingSong = true
        player = MediaPlayer.create(this, Uri.fromFile(SongUtils.songFile()))
        player.isLooping = true
        player.start()
        startForeground(1000, createNotification())
    }

    override fun onDestroy() {
        super.onDestroy()
        RWDC2018Application.isPlayingSong = false
        player.isLooping = false
        player.stop()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(){
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(CHANNEL_ID, getString(R.string.media_playback),
                NotificationManager.IMPORTANCE_LOW)
        channel.setShowBadge(false)
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification() : Notification {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel()
        }
        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val intent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        return NotificationCompat.Builder(this, CHANNEL_ID)
                .setColor(ContextCompat.getColor(this, android.R.color.background_dark))
                .setContentIntent(intent)
                .setSmallIcon(R.drawable.rwdevcon_logo_24px)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOnlyAlertOnce(true)
                .setContentTitle(getString(R.string.challenge))
                .build()
    }
}
