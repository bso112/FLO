package com.manta.flo.repository

import com.manta.flo.network.FloClient
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val floClient: FloClient
){
    suspend fun getSong() = floClient.getSong();
}