package com.manta.flo.model

import com.google.gson.annotations.SerializedName


data class SongResponse(
    @SerializedName("singer")
    val singer: String,
    @SerializedName("album")
    val album: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("image")
    val imageLink: String,
    @SerializedName("file")
    val fileLink: String,
    @SerializedName("lyrics")
    val lyrics: String
)


