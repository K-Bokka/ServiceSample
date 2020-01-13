package com.kbokka.android.servicesample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import java.io.IOException

class SoundManageService : Service() {

  override fun onBind(intent: Intent): IBinder {
    TODO("Return the communication channel to the service.")
  }

  private var _player: MediaPlayer? = null

  override fun onCreate() {
    _player = MediaPlayer()

    val id = "sound_manager_service_notification_channel"
    val name = getString(R.string.msg_notice_channel_name)
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(id, name, importance)

    val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    manager.createNotificationChannel(channel)
  }

  override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
    val mediaFileUriStr = "android.resource://${packageName}/${R.raw.summer_mountain}"
    val mediaFileUri = Uri.parse(mediaFileUriStr)

    try {
      _player?.setDataSource(applicationContext, mediaFileUri)
      _player?.setOnPreparedListener(PlayPreparedListener())
      _player?.setOnCompletionListener(PlayCompletionListener())
      _player?.prepareAsync()
    } catch (ex: IllegalArgumentException) {
      Log.e("MediaSample", "メディアプレイヤー準備時の例外発生", ex)
    } catch (ex: IOException) {
      Log.e("MediaSample", "メディアプレイヤー準備時の例外発生", ex)
    }

    return START_NOT_STICKY
  }

  override fun onDestroy() {
    _player?.let {
      if (it.isPlaying) {
        it.stop()
      }
      it.release()
      _player = null
    }
  }

  private inner class PlayPreparedListener : MediaPlayer.OnPreparedListener {
    override fun onPrepared(mp: MediaPlayer) {
      mp.start()
    }
  }

  private inner class PlayCompletionListener : MediaPlayer.OnCompletionListener {
    override fun onCompletion(mp: MediaPlayer) {
      val builder = NotificationCompat.Builder(
        applicationContext,
        "sound_manager_service_notification_channel"
      )
      builder.setSmallIcon(android.R.drawable.ic_dialog_info)
      builder.setContentTitle(getString(R.string.msg_notice_title_finish))
      builder.setContentText(getString(R.string.msg_notice_text_finish))
      val notification = builder.build()

      val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      manager.notify(0, notification)

      stopSelf()
    }
  }
}
