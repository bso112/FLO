package com.manta.flo.ui

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

object MusicPlayer {

    var mMediaPlayer: MediaPlayer? = null
        private set;

    private val mListeners = mutableListOf<MusicPlayerListener>()

    fun ifMediaPlayerNotNull(function: (MediaPlayer) -> Unit) {
        mMediaPlayer ?: return
        function(mMediaPlayer!!)
    }

    fun setMusic(context: Context, uri: String) {
        if (mMediaPlayer == null)
            mMediaPlayer = MediaPlayer.create(context, Uri.parse(uri))
    }


    fun seekTo(ms: Int) {
        ifMediaPlayerNotNull {
            it.seekTo(ms)
            mListeners.forEach { listener -> listener.onMusicSeekTo(ms) }
        }
    }

    fun start() {
        ifMediaPlayerNotNull {
            it.start()
            mListeners.forEach { listener -> listener.onMusicStart() }
        }
    }

    fun pause() {
        ifMediaPlayerNotNull {
            it.pause()
            mListeners.forEach { listener -> listener.onMusicPause() }
        }
    }


    fun register(listener: MusicPlayerListener) {
        mListeners.add(listener)
    }

    fun unRegister(listener: MusicPlayerListener) {
        mListeners.remove(listener)
    }


}