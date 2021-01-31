package com.manta.flo.ui.musicPlayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.manta.flo.R
import com.manta.flo.databinding.ActivityMusicPlayerBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MusicPlayerActivity : AppCompatActivity() {

    private val musicPlayerViewModel: MusicPlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMusicPlayerBinding>(
            this,
            R.layout.activity_music_player
        )
        binding.lifecycleOwner = this;
        binding.viewModel = musicPlayerViewModel;


        musicPlayerViewModel.getSongData()

        val lyricView = findViewById<LyricView>(R.id.lyric_view)
        val musicPlayerView = findViewById<MusicPlayerView>(R.id.music_player)

        musicPlayerView.setMusicPlayerListener(object : MusicPlayerView.MusicPlayerListener{
            override fun onPlay() {
                CoroutineScope(Dispatchers.Default).launch {
                    while (musicPlayerView.isPlaying()) {
                        lyricView.showLyric(musicPlayerView.getTimeStamp())
                        delay(1)
                    }
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()

    }

}