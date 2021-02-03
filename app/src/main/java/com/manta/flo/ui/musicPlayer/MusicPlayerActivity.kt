package com.manta.flo.ui.musicPlayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.manta.flo.R
import com.manta.flo.databinding.ActivityMusicPlayerBinding
import com.manta.flo.ui.MusicPlayer
import com.manta.flo.ui.lyric.LyricActivity
import com.manta.flo.utill.Constants.EXTRA_SONGDATA
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class MusicPlayerActivity : AppCompatActivity() {

    private val musicPlayerViewModel: MusicPlayerViewModel by viewModels()
    private val mBinding by lazy {
        DataBindingUtil.setContentView<ActivityMusicPlayerBinding>(
            this,
            R.layout.activity_music_player
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        mBinding.lifecycleOwner = this;
        mBinding.viewModel = musicPlayerViewModel;


        musicPlayerViewModel.getSongData()


        mBinding.lyricView.getChildAt(0).setOnClickListener {
            Intent(this, LyricActivity::class.java).apply {
                putExtra(EXTRA_SONGDATA, musicPlayerViewModel.songLiveData.value)
                startActivity(this)
            }
        }


        mBinding.musicPlayer.setMusicPlayerListener(object : MusicPlayerView.MusicPlayerListener {
            //일정 간격으로 다음가사를 출력해야하는지 확인하고, 변경한다.
            override fun onPlay() {
                lifecycleScope.launch {
                    showLyric()
                }
            }

            override fun onComplete() {
            }

            override fun onSeekTo(msec: Int) {
                mBinding.lyricView.jumpLyricTo(msec)
            }
        })
    }



    private suspend fun showLyric() {
        while (mBinding.musicPlayer.isPlaying()) {
            mBinding.lyricView.showLyric(mBinding.musicPlayer.getTimeStamp())
            delay(100)
        }
    }


}