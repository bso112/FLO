package com.manta.flo.network

import com.manta.flo.model.SongResponse
import retrofit2.Response
import retrofit2.http.GET

interface FloService {
    @GET("song.json")
    suspend fun getSong() : Response<SongResponse>
}