package com.manta.flo.ui.musicPlayer

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.core.content.res.ResourcesCompat
import com.manta.flo.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MusicPlayerView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val mView: View = View.inflate(context, R.layout.music_player_view, this)
    private val mSeekBar = mView.findViewById<SeekBar>(R.id.seekbar_music)
    private val mPlayButton = mView.findViewById<ImageButton>(R.id.btn_play)
    private var mMusicPlayerListener: MusicPlayerListener? = null;

    private val mPauseDrawble = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_pause_24, context.theme)
    private val mPlayDrawble = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_play_arrow_24, context.theme)

    companion object {
        private var mMediaPlayer: MediaPlayer? = null
    }

    var file_uri : String? = null
        set(value){
            if(value != null)
                onSetMusic(value)
            field = value
        }


    interface MusicPlayerListener {
        fun onPlay()
        fun onComplete()

        /**
         * 사용자에 의해 특정 구간으로 점프했을때
         */
        fun onSeekTo(msec: Int)
    }


    init {
        //노래 재생
        mPlayButton.setOnClickListener {
            val mediaPlayer = mMediaPlayer ?: return@setOnClickListener;
            switchPlayButtonImage(mediaPlayer.isPlaying)

            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            } else {
                playMusic();
            }
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

    private fun switchPlayButtonImage(isPlaying: Boolean) {
        if (isPlaying)
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
        //음악의 총 길이를 Seekbar에 설정
        mSeekBar.max = mMediaPlayer!!.duration

        if (mMediaPlayer!!.isPlaying) {
            playMusic()
        }
    }


}




