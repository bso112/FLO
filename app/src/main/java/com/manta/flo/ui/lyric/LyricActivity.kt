package com.manta.flo.ui.lyric

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.manta.flo.R
import com.manta.flo.databinding.ActivityLyricBinding
import com.manta.flo.model.SongResponse
import com.manta.flo.utill.Constants.EXTRA_SONGDATA

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


        mBinding.musicPlayer.registerLifecycleOwner(lifecycle)
        mBinding.lyricView.registerLifecycleOwner(lifecycle)

        mBinding.btnLyricSelectMode.setOnClickListener {
            mBinding.lyricView.setSelectMode()

            mBinding.btnLyricSelectMode.background =
                if(mBinding.lyricView.mSelectMode)
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_playlist_play_selected_24, theme)
                else
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_playlist_play_24, theme)

        }

        mBinding.lyricView.getChildAt(0).setOnClickListener {
            if(!mBinding.lyricView.mSelectMode)
                finish()
        }

        mBinding.btnClose.setOnClickListener {
            finish()
        }


    }




}