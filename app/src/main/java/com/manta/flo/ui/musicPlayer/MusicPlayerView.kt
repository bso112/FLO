package com.manta.flo.ui.musicPlayer

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.annotation.MainThread
import androidx.core.content.res.ResourcesCompat
import com.manta.flo.R
import kotlinx.coroutines.*


class MusicPlayerView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val mView: View = View.inflate(context, R.layout.music_player_view, this)
    private val mSeekBar = mView.findViewById<SeekBar>(R.id.seekbar_music)
    private val mPlayButton = mView.findViewById<ImageButton>(R.id.btn_play)

    private val mPauseDrawble = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_pause_24, context.theme)
    private val mPlayDrawble = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_play_arrow_24, context.theme)


    var file_uri : String? = null
        set(value){
            if(value != null)
                onSetMusic(value)
            field = value
        }


    init {
      initUI()
    }

    fun resetUI(){
        mSeekBar.max = mMediaPlayer?.duration ?: 0
        mSeekBar.progress = mMediaPlayer?.currentPosition ?: 0
        switchPlayButtonImage()
    }


    fun getTimeStamp() = mMediaPlayer?.currentPosition ?: 0
    fun isPlaying() = mMediaPlayer?.isPlaying ?: false;
    fun getDuation() = mMediaPlayer?.duration ?: 0

    fun setMusicPlayerListener(listener: MusicPlayerListener) {
        mMusicPlayerListener = listener
    }


    private fun playMusic() {
        val mediaPlayer = mMediaPlayer ?: return;
        mediaPlayer.start()
        mMusicPlayerListener?.onPlay()
        showProgress();
    }

    @MainThread
    private fun initUI(){
        //노래 재생
        mPlayButton.setOnClickListener {
            val mediaPlayer = mMediaPlayer ?: return@setOnClickListener;

            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            } else {
                playMusic();
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
                    mMediaPlayer?.seekTo(seekBar.progress)
                    mMusicPlayerListener?.onSeekTo(seekBar.progress)
                }
            }

        })

    }

    @MainThread
    private fun switchPlayButtonImage() {
        if (mMediaPlayer?.isPlaying ?: false)
            mPlayButton.background = mPauseDrawble
        else
            mPlayButton.background = mPlayDrawble
    }

    private fun showProgress() {
        CoroutineScope(Dispatchers.Default).launch {
            while (isPlaying()) {
                mSeekBar.progress = mMediaPlayer?.currentPosition ?: 0
                delay(1000)
            }
        }
    }


    private fun onSetMusic(fileUri : String) {
        mMediaPlayer = mMediaPlayer ?: MediaPlayer.create(context, Uri.parse(fileUri)) ?: return
        //음악이 끝날때
        mMediaPlayer!!.setOnCompletionListener { mMusicPlayerListener?.onComplete() }

        resetUI()

        if (mMediaPlayer!!.isPlaying) {
            playMusic()
        }
    }


}




