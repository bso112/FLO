package com.manta.flo.ui

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

object MusicPlayer {

    var mMediaPlayer: MediaPlayer? = null
        private set;

    var currentUri: String = ""

    private val mListeners = mutableListOf<MusicPlayerListener>()

    fun ifMediaPlayerNotNull(function: (MediaPlayer) -> Unit) {
        mMediaPlayer ?: return
        function(mMediaPlayer!!)
    }

    fun setMusic(context: Context, uri: String) {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(context, Uri.parse(uri))
            mMediaPlayer!!.setOnCompletionListener {
                    mListeners.forEach { listener -> listener.onMusicEnd() }
            }
        } else if (currentUri != uri) {
            mMediaPlayer!!.reset()
            mMediaPlayer!!.setDataSource(context, Uri.parse(uri))
            mMediaPlayer!!.prepare()
            mListeners.forEach { listener -> listener.onMusicChange() }
        }
        currentUri = uri
    }


    fun seekTo(ms: Int) {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.seekTo(ms)
            mListeners.forEach { listener -> listener.onMusicSeekTo(ms) }
        }

    }

    fun start() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.start()
            mListeners.forEach { listener -> listener.onMusicStart() }
        }
    }

    fun pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.pause()
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