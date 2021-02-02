package com.manta.flo.di

import com.manta.flo.utill.Constants
import com.manta.flo.network.FloClient
import com.manta.flo.network.FloService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Provides
    fun provideBaseUrl() = Constants.BASE_URL


    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(BASE_URL:String, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideFloService(retrofit: Retrofit) : FloService = retrofit.create(FloService::class.java)

    @Singleton
    @Provides
    fun provideFloClient(floService: FloService) : FloClient{
        return FloClient(floService)
    }

    
}
