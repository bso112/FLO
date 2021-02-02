package com.manta.flo.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


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
    val imageUri: String,
    @SerializedName("file")
    val fileUri: String,
    @SerializedName("lyrics")
    val lyrics: String
) : Serializable


