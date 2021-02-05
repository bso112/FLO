package com.manta.flo.utill

interface MusicPlayerListener {
    fun onMusicSeekTo(ms : Int){}
    fun onMusicStart(){}
    fun onMusicPause(){}
    fun onMusicChange(){}
    fun onMusicEnd(){}
}