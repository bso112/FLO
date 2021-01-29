package com.manta.flo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.manta.flo.databinding.ActivityMusicPlayerBinding
import com.manta.flo.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MusicPlayerActivity : AppCompatActivity() {

    private val mainViewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMusicPlayerBinding>(this, R.layout.activity_music_player)
        binding.lifecycleOwner = this;
        binding.viewModel = mainViewModel;

        mainViewModel.getSongData()

    }

}