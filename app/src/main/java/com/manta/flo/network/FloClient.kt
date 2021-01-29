package com.manta.flo.network

import com.manta.flo.model.SongResponse
import retrofit2.Response
import javax.inject.Inject

class FloClient @Inject constructor(
    private val floService: FloService
){
    suspend fun getSong() = floService.getSong()
}