package com.manta.flo.ui.lyric

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.manta.flo.R
import com.manta.flo.databinding.ActivityLyricBinding
import com.manta.flo.model.SongResponse
import com.manta.flo.ui.musicPlayer.LyricView2
import com.manta.flo.ui.musicPlayer.MusicPlayerView
import com.manta.flo.utill.Constants.EXTRA_SONGDATA
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LyricActivity : AppCompatActivity() {

    private val mBinding by lazy {
        DataBindingUtil.setContentView<ActivityLyricBinding>(this, R.layout.activity_lyric)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyric)

        val songData = intent.getSerializableExtra(EXTRA_SONGDATA) as? SongResponse ?: return

        mBinding.lifecycleOwner = this
        mBinding.song = songData


        mBinding.btnLyricSelectMode.setOnClickListener {
            mBinding.lyricView.setSelectMode()

            mBinding.btnLyricSelectMode.background =
                if(mBinding.lyricView.mSelectMode)
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_playlist_play_selected_24, theme)
                else
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_playlist_play_24, theme)

        }


    }




}