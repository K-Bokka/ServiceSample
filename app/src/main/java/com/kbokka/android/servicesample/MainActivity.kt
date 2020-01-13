package com.kbokka.android.servicesample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  fun onPlayButtonClick(view: View) {
    val intent = Intent(applicationContext, SoundManageService::class.java)
    startService(intent)

    findViewById<Button>(R.id.btPlay).isEnabled = false
    findViewById<Button>(R.id.btStop).isEnabled = true
  }

  fun onStopButtonClick(view: View) {
    val intent = Intent(applicationContext, SoundManageService::class.java)
    stopService(intent)

    findViewById<Button>(R.id.btPlay).isEnabled = true
    findViewById<Button>(R.id.btStop).isEnabled = false
  }
}
