package com.manta.flo.ui.musicPlayer

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.manta.flo.R
import com.manta.flo.databinding.ActivityMusicPlayerBinding
import com.manta.flo.ui.lyric.LyricActivity
import com.manta.flo.utill.Constants.EXTRA_SONGDATA
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MusicPlayerActivity : AppCompatActivity() {

    private val musicPlayerViewModel: MusicPlayerViewModel by viewModels()
    private val musicPlayerView by lazy{
        findViewById<MusicPlayerView>(R.id.music_player)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMusicPlayerBinding>(
            this,
            R.layout.activity_music_player
        )
        binding.lifecycleOwner = this;
        binding.viewModel = musicPlayerViewModel;


        musicPlayerViewModel.getSongData()


        val lyricView = findViewById<LyricView2>(R.id.lyric_view)

        lyricView.getChildAt(0).setOnClickListener {
            Intent(this, LyricActivity::class.java).apply {
                putExtra(EXTRA_SONGDATA, musicPlayerViewModel.songLiveData.value)
                startActivity(this)
            }
        }


        musicPlayerView.setMusicPlayerListener(object : MusicPlayerView.MusicPlayerListener {
            //일정 간격으로 다음가사를 출력해야하는지 확인하고, 변경한다.
            override fun onPlay() {
                CoroutineScope(Dispatchers.Default).launch {
                    while (musicPlayerView.isPlaying()) {
                        lyricView.showLyric(musicPlayerView.getTimeStamp())
                        delay(100)
                    }
                }
            }

            override fun onComplete() {
            }

            override fun onSeekTo(msec: Int) {
                lyricView.jumpLyricTo(msec)
            }
        })


    }

    override fun onStart() {
        super.onStart()
        musicPlayerView.resetUI()
    }

}