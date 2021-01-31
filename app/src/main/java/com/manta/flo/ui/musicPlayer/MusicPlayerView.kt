package com.manta.flo.ui.musicPlayer

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import com.manta.flo.R


class MusicPlayerView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val mView: View = View.inflate(context, R.layout.music_player_view, this)
    private var mMediaPlayer: MediaPlayer? = null
    private var mMusicPlayerListener : MusicPlayerListener? = null;

    interface MusicPlayerListener{
        fun onPlay()
    }

    var music_uri: String? = null
        set(value) {
            if(value != null)
                mMediaPlayer = MediaPlayer.create(context, Uri.parse(value))
            field = value;
        }


    init {
        //노래 재생
        mView.findViewById<ImageButton>(R.id.btn_play).setOnClickListener {
            val mediaPlayer = mMediaPlayer ?: return@setOnClickListener
            if (mediaPlayer.isPlaying){
                mediaPlayer.pause()
            }
            else{
                mediaPlayer.start()
                mMusicPlayerListener?.onPlay()
            }



        }
    }

    fun getTimeStamp() = mMediaPlayer?.currentPosition ?: 0
    fun isPlaying()  = mMediaPlayer?.isPlaying ?: false;

    fun setMusicPlayerListener(listener : MusicPlayerListener) {
        mMusicPlayerListener = listener
    }


}




