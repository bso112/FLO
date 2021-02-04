package com.manta.flo.ui.musicPlayer

import androidx.annotation.MainThread

interface FloSeekbar{

    fun getTimeString(ms : Int) : String{
        val m = (ms / 1000) / 60 % 60
        val s = (ms / 1000) % 60
        return String.format("%02d:%02d", m, s);
    }

    @MainThread
    fun setProgress(ms: Int)

    @MainThread
    fun onMusicSet()

    fun isSeekBarPressed() : Boolean
}