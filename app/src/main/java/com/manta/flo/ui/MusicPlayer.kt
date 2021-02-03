package com.manta.flo.ui

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

object MusicPlayer {

    private var mMediaPlayer: MediaPlayer? = null
    private val mListeners = mutableListOf<MusicPlayerListener>()

    fun isMusicReady() = mMediaPlayer != null

    fun setMusic(context : Context, uri : String){
        mMediaPlayer = MediaPlayer.create(context, Uri.parse(uri))
    }

    fun seekTo(ms : Int){
        mMediaPlayer?.seekTo(ms)
        mListeners.forEach { it.OnSeekTo(ms) }
    }

    fun register(listener: MusicPlayerListener){
        mListeners.add(listener)
    }

    fun unRegister(listener: MusicPlayerListener){
        mListeners.remove(listener)
    }



}