package com.kbokka.android.servicesample

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.util.Log
import java.io.IOException

class SoundManageService : Service() {

  override fun onBind(intent: Intent): IBinder {
    TODO("Return the communication channel to the service.")
  }

  private var _player: MediaPlayer? = null

  override fun onCreate() {
    _player = MediaPlayer()
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
      stopSelf()
    }
  }
}
