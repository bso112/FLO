package com.manta.flo.ui.musicPlayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.manta.flo.R
import com.manta.flo.databinding.ActivityMusicPlayerBinding
import com.manta.flo.ui.MusicPlayerView
import dagger.hilt.android.AndroidEntryPoint

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

    }

}