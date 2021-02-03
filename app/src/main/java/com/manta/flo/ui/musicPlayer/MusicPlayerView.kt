package com.manta.flo.ui.musicPlayer

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.annotation.MainThread
import androidx.core.content.res.ResourcesCompat
import com.manta.flo.R
import com.manta.flo.ui.MusicPlayer
import com.manta.flo.ui.MusicPlayerListener
import kotlinx.coroutines.*


class MusicPlayerView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs), MusicPlayerListener {

    private val mView: View = View.inflate(context, R.layout.music_player_view, this)
    private val mSeekBar = mView.findViewById<SeekBar>(R.id.seekbar_music)
    private val mPlayButton = mView.findViewById<ImageButton>(R.id.btn_play)

    private val mPauseDrawble = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_pause_24, context.theme)
    private val mPlayDrawble = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_play_arrow_24, context.theme)


    var file_uri: String? = null
        set(value) {
            if (value != null)
                onSetMusic(value)
            field = value
        }


    init {
        initUI()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        MusicPlayer.register(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        MusicPlayer.unRegister(this)
    }


    @MainThread
    private fun initUI() {
        //노래 재생
        mPlayButton.setOnClickListener {
            MusicPlayer.ifMediaPlayerNotNull {
                if (it.isPlaying)
                    MusicPlayer.pause()
                else
                    MusicPlayer.start()

            }
            switchPlayButtonImage()
        }

        mSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    mSeekBar.progress = seekBar.progress
                    MusicPlayer.seekTo(seekBar.progress)
                }
            }

        })

    }

    @MainThread
    private fun switchPlayButtonImage() {
        MusicPlayer.ifMediaPlayerNotNull {
            if (it.isPlaying)
                mPlayButton.background = mPauseDrawble
            else
                mPlayButton.background = mPlayDrawble
        }

    }

    private fun onSetMusic(fileUri: String) {
        MusicPlayer.setMusic(context, fileUri)
        mSeekBar.max = MusicPlayer.mMediaPlayer?.duration ?: 0
        mSeekBar.progress = MusicPlayer.mMediaPlayer?.currentPosition ?: 0
        switchPlayButtonImage()
    }

    override fun onMusicStart() {
        super.onMusicStart()
        MusicPlayer.ifMediaPlayerNotNull {
            GlobalScope.launch {
                while (it.isPlaying) {
                    mSeekBar.progress = it.currentPosition
                    delay(1000)
                }
            }
        }
    }


    override fun onMusicPause() {
        super.onMusicPause()
        switchPlayButtonImage()
    }

    override fun onMusicSeekTo(ms: Int) {
        super.onMusicSeekTo(ms)
        mSeekBar.progress = ms
    }
}




