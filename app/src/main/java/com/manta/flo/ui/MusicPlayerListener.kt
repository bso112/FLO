package com.manta.flo.ui

interface MusicPlayerListener {
    fun onMusicSeekTo(ms : Int){}
    fun onMusicStart(){}
    fun onMusicPause(){}
    fun onMusicChange(){}
}