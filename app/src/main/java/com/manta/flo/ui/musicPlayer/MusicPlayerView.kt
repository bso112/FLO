package com.manta.flo.ui.musicPlayer

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
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
    private var mMediaPlayer: MediaPlayer? = null
    private var mMusicPlayerListener: MusicPlayerListener? = null;

    interface MusicPlayerListener {
        fun onPlay()
        fun onComplete()

        /**
         * 사용자에 의해 특정 구간으로 점프했을때
         */
        fun onSeekTo(msec : Int)
    }

    var music_uri: String? = null
        set(value) {
            if (value != null){
               onSetMusic(value)
            }
            field = value;
        }


    init {

        val playBtn = mView.findViewById<ImageButton>(R.id.btn_play)
        //노래 재생
        playBtn.setOnClickListener {
            val mediaPlayer = mMediaPlayer ?: return@setOnClickListener
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                playBtn.background = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_pause_24, context.theme)
            } else {
                mediaPlayer.start()
                mMusicPlayerListener?.onPlay()
                playBtn.background = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_play_arrow_24, context.theme)

                showProgress();
            }
        }

        mSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if(seekBar != null){
                    mSeekBar.progress = seekBar.progress
                    mMediaPlayer?.seekTo(seekBar.progress)
                    mMusicPlayerListener?.onSeekTo(seekBar.progress)
                }
            }

        })


    }

    private fun showProgress(){
        CoroutineScope(Dispatchers.Default).launch {
            while (isPlaying()) {
                mSeekBar.progress = mMediaPlayer?.currentPosition ?: 0
                delay(1000)
            }
        }
    }

    private fun onSetMusic(music_uri : String){
        mMediaPlayer = MediaPlayer.create(context, Uri.parse(music_uri))
        //음악이 끝날때
        mMediaPlayer!!.setOnCompletionListener { mMusicPlayerListener?.onComplete() }
        //음악의 총 길이를 Seekbar에 설정
        mSeekBar.max = mMediaPlayer!!.duration
    }

    fun getTimeStamp() = mMediaPlayer?.currentPosition ?: 0
    fun isPlaying() = mMediaPlayer?.isPlaying ?: false;
    fun getDuation() = mMediaPlayer?.duration ?: 0

    fun setMusicPlayerListener(listener: MusicPlayerListener) {
        mMusicPlayerListener = listener
    }


}




