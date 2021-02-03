package com.manta.flo.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.manta.flo.R
import com.manta.flo.ui.musicPlayer.MusicPlayerActivity
import com.manta.flo.ui.musicPlayer.MusicPlayerView
import kotlinx.coroutines.delay

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launchWhenCreated {
            delay(2000)
            startActivity(Intent(this@SplashActivity, MusicPlayerActivity::class.java))
        }
    }
}