package com.manta.flo.utill

interface MusicPlayerListener {
    fun onMusicSeekTo(ms : Int)
    fun onMusicStart()
    fun onMusicChange()

    //optional
    fun onMusicEnd(){}
    fun onMusicPause(){}
}