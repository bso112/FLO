package com.manta.flo.network

import com.manta.flo.model.SongResponse
import com.manta.flo.utill.Constants.BASE_URL
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FloClientTest {

    private lateinit var mFloClient: FloClient

    @Before
    fun init(){
        mFloClient = FloClient(createService())
    }

    @Test
    fun getSongFromNetworkTest() = runBlocking {
        val res : SongResponse = requireNotNull(mFloClient.getSong().body())
        checkResponse(res)
    }

    private fun checkResponse(response: SongResponse){
        assertEquals("챔버오케스트라", response.singer)
        assertEquals("캐롤 모음", response.album)
        assertEquals("We Wish You A Merry Christmas", response.title)
        assertEquals(198, response.duration)
        assertEquals("https://grepp-programmers-challenges.s3.ap-northeast-2.amazonaws.com/2020-flo/cover.jpg", response.imageUri)
        assertEquals("https://grepp-programmers-challenges.s3.ap-northeast-2.amazonaws.com/2020-flo/music.mp3", response.fileUri)
        assertEquals("[00:16:200]we wish you a merry christmas\n[00:18:300]we wish you a merry christmas\n[00:21:100]we wish you a merry christmas\n[00:23:600]and a happy new year\n[00:26:300]we wish you a merry christmas\n[00:28:700]we wish you a merry christmas\n[00:31:400]we wish you a merry christmas\n[00:33:600]and a happy new year\n[00:36:500]good tidings we bring\n[00:38:900]to you and your kin\n[00:41:500]good tidings for christmas\n[00:44:200]and a happy new year\n[00:46:600]Oh, bring us some figgy pudding\n[00:49:300]Oh, bring us some figgy pudding\n[00:52:200]Oh, bring us some figgy pudding\n[00:54:500]And bring it right here\n[00:57:000]Good tidings we bring \n[00:59:700]to you and your kin\n[01:02:100]Good tidings for Christmas \n[01:04:800]and a happy new year\n[01:07:400]we wish you a merry christmas\n[01:10:000]we wish you a merry christmas\n[01:12:500]we wish you a merry christmas\n[01:15:000]and a happy new year\n[01:17:700]We won't go until we get some\n[01:20:200]We won't go until we get some\n[01:22:800]We won't go until we get some\n[01:25:300]So bring some out here\n[01:29:800]연주\n[02:11:900]Good tidings we bring \n[02:14:000]to you and your kin\n[02:16:500]good tidings for christmas\n[02:19:400]and a happy new year\n[02:22:000]we wish you a merry christmas\n[02:24:400]we wish you a merry christmas\n[02:27:000]we wish you a merry christmas\n[02:29:600]and a happy new year\n[02:32:200]Good tidings we bring \n[02:34:500]to you and your kin\n[02:37:200]Good tidings for Christmas \n[02:40:000]and a happy new year\n[02:42:400]Oh, bring us some figgy pudding\n[02:45:000]Oh, bring us some figgy pudding\n[02:47:600]Oh, bring us some figgy pudding\n[02:50:200]And bring it right here\n[02:52:600]we wish you a merry christmas\n[02:55:300]we wish you a merry christmas\n[02:57:900]we wish you a merry christmas\n[03:00:500]and a happy new year",
                    response.lyrics)
    }


    private fun createService() : FloService{
       return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(FloService::class.java)
    }

}